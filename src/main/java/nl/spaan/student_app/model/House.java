package nl.spaan.student_app.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "houses")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String houseName;

    @OneToMany(
            mappedBy = "house",
            fetch = FetchType.EAGER)
    private List<User> userList;

    @OneToMany(
            mappedBy = "house",
            fetch = FetchType.LAZY)
    private List<Declaration> declarations;

    @OneToOne(mappedBy = "house")
    @PrimaryKeyJoinColumn
    private Account account;

    @OneToMany(
            mappedBy = "house",
            fetch = FetchType.LAZY)
    private List<Bill> billHouse;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Bill> getBillHouse() {
        return billHouse;
    }

    public void setBillHouse(List<Bill> billHouse) {
        this.billHouse = billHouse;
    }
}
