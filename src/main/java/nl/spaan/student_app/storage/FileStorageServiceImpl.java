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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Transactional
@Service
public class FileStorageServiceImpl implements FileStorageService {

	private FileDBRepository fileDBRepository;

	private DeclarationRepository declarationRepository;

	private UserService userService;

	private final Path fileStorageLocation = Paths.get("uploads");

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
	public ResponseEntity<?> store(MultipartFile file, String token)  {

		if  (file.isEmpty()) {
			throw new StorageException("Failed to store empty file.");
		}

		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		String fileNameCustom = userService.findUserNameFromToken(token).getUsername();
		fileNameCustom = fileNameCustom + java.time.LocalDateTime.now();
		fileNameCustom = fileNameCustom.replace(":","_")
				.replace(" ", "")
				.replace(".", "_")+fileName;

		Path targetLocation = this.fileStorageLocation.resolve(fileNameCustom);

		try {
			Files.createDirectories(this.fileStorageLocation);
			// check of de file een geen onjuiste tekens bevat
			if(fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}
			// Sla file op vervang als het dezelfde naam heeft
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
		FileDB fileDB = new FileDB();
		fileDB.setFilePath(targetLocation.toString());
		fileDB.setNameFile(fileNameCustom);
		fileDBRepository.save(fileDB);
		return ResponseEntity.ok(fileNameCustom);
	}

}
