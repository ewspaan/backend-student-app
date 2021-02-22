package nl.spaan.student_app.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;


@Service
@Validated
public interface StorageService {

	void init();

	void store(MultipartFile file);

	Resource getFile(String filename);

	void deleteAll();
}
