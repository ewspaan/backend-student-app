package nl.spaan.student_app.storage;

import nl.spaan.student_app.model.Declaration;
import nl.spaan.student_app.model.FileDB;
import nl.spaan.student_app.model.User;
import nl.spaan.student_app.payload.response.FileStorageException;
import nl.spaan.student_app.repository.DeclarationRepository;
import nl.spaan.student_app.repository.FileDBRepository;
import nl.spaan.student_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class FileStorageServiceImpl implements FileStorageService {

	private FileDBRepository fileDBRepository;

	private DeclarationRepository declarationRepository;

	private UserService userService;


	private final Path fileStorageLocation = Paths.get("C:\\uploads\\");

	@Override
	public void init() {
		try {
			Files.createDirectory(fileStorageLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Autowired
	public void setFileDBRepository(FileDBRepository fileDBRepository) {
		this.fileDBRepository = fileDBRepository;
	}

	@Autowired
	public void setDeclarationRepository(DeclarationRepository declarationRepository) {
		this.declarationRepository = declarationRepository;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	@Override
	public void store(MultipartFile file, String amount, String date, String token) throws IOException {

		User user = userService.findUserNameFromToken(token);
		Declaration declaration = new Declaration();
		if  (file.isEmpty()) {
			System.out.println("bla1 empty");
			throw new StorageException("Failed to store empty file.");
		}

		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		Path targetLocation = this.fileStorageLocation.resolve(fileName);
		System.out.println("bla1" + targetLocation);
		Files.createDirectories(this.fileStorageLocation);
		try {
			// check of de file een geen onjuiste tekens bevat
			if(fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			// Copy file to the target location (Replacing existing file with the same name)
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
		File fileToSave = new File("C:\\uploads\\" + file);
		//Files.copy(file.getInputStream(), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
		String fileUrl = "C:\\uploads\\" + fileName;
		FileDB fileDB = new FileDB(
								fileName,
								file.getContentType(),
								fileUrl,
								fileToSave,
								file.getBytes());

		System.out.println("bla4" + targetLocation.toUri().toString());

		declaration.setUser(user);
		declaration.setFileDB(fileDB);
		//TODO Dit netter maken
		declaration.setGroceriesAmount(Double.parseDouble(amount));
		List<Integer> decalDate = stringToIntegerArray(date);
		declaration.setDay(decalDate.get(0));
		declaration.setMonth(decalDate.get(1));
		declaration.setYear(decalDate.get(2));
		declaration.setChecked(false);
		declaration.setCorrect(false);
		fileDB.setDeclaration(declaration);

		fileDB.setNameFile(fileName);
		fileDB.setType(file.getContentType());

		declarationRepository.save(declaration);
		fileDBRepository.save(fileDB);
	}

	private List<Integer> stringToIntegerArray(String date) {
		String[] items = date.replaceAll("day", "-").replaceAll("month", "-").replaceAll("year", "-").split("-");
		List<Integer> dateInt = new ArrayList<Integer>();
		for (String item : items) {
			if (!item.equals("")) {
				dateInt.add(Integer.parseInt(item));
				System.out.println("items--> " + dateInt.toString());
			}
		}
		return dateInt;
	}

	@Override
	public ResponseEntity<?> getFile(long id, HttpServletRequest request) {
		System.out.println("Headers-->  " + id);
		if(fileDBExists(id)){
			FileDB fileDB = fileDBRepository.findFileById(id);
			Resource resource;
			try {
				Path filePath = this.fileStorageLocation.resolve(fileDB.getNameFile()).normalize();
				resource = new UrlResource(filePath.toUri());
				System.out.println("Resource-->  " + resource.toString().getBytes(StandardCharsets.US_ASCII));

			} catch (MalformedURLException ex) {
				throw new FileStorageException ("File not found " + ex);
			}
			String contentType = null;
			try {
				contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			} catch (IOException ex) {
				ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Er is geen bonnetje bij de declaratie");
			}

			// Fallback to the default content type if type could not be determined
			if(contentType == null) {
				contentType = "application/octet-stream";
			}


			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		}

		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Er is geen bonnetje bij de declaratie");
	}

	private boolean fileDBExists(long id) {
		return fileDBRepository.existsById(id);
	}

}
