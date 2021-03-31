package nl.spaan.student_app.storage;

import nl.spaan.student_app.model.Declaration;
import nl.spaan.student_app.model.FileDB;
import nl.spaan.student_app.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FileStorageServiceImpl implements FileStorageService {

	private FileDBRepository fileDBRepository;

	@Autowired
	public void setFileDBRepository(FileDBRepository fileDBRepository) {
		this.fileDBRepository = fileDBRepository;
	}

	@Override
	public ResponseEntity<?> store(String file, String token, Declaration declaration)  {


		FileDB fileDB = new FileDB();

		fileDB.setNameFile(file);
		fileDB.setDeclaration(declaration);
		fileDBRepository.save(fileDB);
		return ResponseEntity.ok("File opgeslagen");
	}





}
