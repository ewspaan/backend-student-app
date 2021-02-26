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
    private byte[] data;
    private File uploadedFile;

    @OneToOne
    @MapsId
    @JoinColumn(name = "declaration_id")
    private Declaration declaration;

    public FileDB() {
    }

    public FileDB(String name, String type, String filePath, File uploadedFile, byte[] data) {
        this.nameFile = name;
        this.type = type;
        this.filePath = filePath;
        this.uploadedFile = uploadedFile;
        this.data = data;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public File getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(File uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}