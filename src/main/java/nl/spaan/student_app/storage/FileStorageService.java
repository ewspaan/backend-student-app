package nl.spaan.student_app.storage;

import nl.spaan.student_app.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileStorageService implements StorageService {


	@Value("${upload.path}")
	private String uploadPath;

	@PostConstruct
	public void init() {

		try {
			Files.createDirectories(Paths.get(uploadPath));
		} catch (IOException e) {
			System.out.println("root-error-> " + uploadPath);
			throw new RuntimeException("Could not create upload folder!");
		}
		System.out.println("root--> " + uploadPath);
	}

	//Sla foto van bon op.
	@Override
	public void store(MultipartFile file) {
		System.out.println("root--> " + uploadPath);
		try {
			Path root = Paths.get(uploadPath);
			if (!Files.exists(root)) {
				init();
			}
			Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	// Haal foto van bon op
	@Override
	public Resource getFile(String filename) {
		try {
			Path file = Paths.get(uploadPath)
					.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}



	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(uploadPath)
				.toFile());
	}
}
