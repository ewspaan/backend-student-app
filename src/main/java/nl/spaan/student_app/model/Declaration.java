package nl.spaan.student_app.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "declarations")
public class Declaration {

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
    private double groceriesAmount;
    private int day;
    private int month;
    private int year;
    private boolean checked;
    private boolean correct;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    public Declaration() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getGroceriesAmount() {
        return groceriesAmount;
    }

    public void setGroceriesAmount(double groceriesAmount) {
        this.groceriesAmount = groceriesAmount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
