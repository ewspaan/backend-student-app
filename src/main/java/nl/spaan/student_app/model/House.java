package nl.spaan.student_app.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
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
    private Long id;
    private String houseName;
    private String accountNumber;

    @OneToMany(
            mappedBy = "house",
            fetch = FetchType.EAGER,
            cascade =  CascadeType.ALL)
    private Set<User> userList;


    public House() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Set<User> getUsers() {
        return userList;
    }

    public void setUsers(Set<User> users) {
        this.userList = users;
    }
}
