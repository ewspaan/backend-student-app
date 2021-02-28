package nl.spaan.student_app.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name = "filesDB")
public class FileDB {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(columnDefinition = "serial")
    private Long id;
    private String nameFile;
    private String type;
    private String filePath;

    public FileDB() {
    }

    public FileDB(String name, String type, String filePath) {
        this.nameFile = name;
        this.type = type;
        this.filePath = filePath;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}