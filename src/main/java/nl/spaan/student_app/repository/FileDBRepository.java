package nl.spaan.student_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.spaan.student_app.model.FileDB;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface FileDBRepository extends JpaRepository<FileDB, Long>  {


    FileDB findFileById(long id);
}
