package nl.spaan.student_app.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "houses")
public class House {

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
    private long id;
    private String houseName;

    @OneToMany(
            mappedBy = "house",
            fetch = FetchType.EAGER,
            cascade =  CascadeType.ALL)
    private Set<User> userList;

    @OneToMany(
            mappedBy = "house",
            fetch = FetchType.EAGER,
            cascade =  CascadeType.ALL)
    private Set<Declaration> declarations;

    @OneToOne(mappedBy = "house")
    @PrimaryKeyJoinColumn
    private Account account;

    @OneToMany(
            mappedBy = "house",
            fetch = FetchType.EAGER,
            cascade =  CascadeType.ALL)
    private List<BillHouse> billHouse;


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
    
    public Set<User> getUsers() {
        return userList;
    }

    public void setUsers(Set<User> users) {
        this.userList = users;
    }

    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Declaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(Set<Declaration> declarations) {
        this.declarations = declarations;
    }

    public List<BillHouse> getBillHouse() {
        return billHouse;
    }

    public void setBillHouse(List<BillHouse> billHouse) {
        this.billHouse = billHouse;
    }
}
