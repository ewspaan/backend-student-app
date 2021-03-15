package nl.spaan.student_app.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "bills_house")
public class BillHouse extends Bill{



        private double totalUtilities;
        private double totalDeclarations;


        @ManyToOne
        @JoinColumn(name = "house_id", nullable = false)
        private House house;


    public double getTotalUtilities() {
        return totalUtilities;
    }

    public void setTotalUtilities(double totalUtilities) {
        this.totalUtilities = totalUtilities;
    }

    public double getTotalDeclarations() {
        return totalDeclarations;
    }

    public void setTotalDeclarations(double totalDeclarations) {
        this.totalDeclarations = totalDeclarations;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
