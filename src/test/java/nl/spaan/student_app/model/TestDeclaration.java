package nl.spaan.student_app.model;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDeclaration {

    private Declaration declaration;

    @Before
    public void setUp() {
        this.declaration = new Declaration();
        declaration.setGroceriesAmount(12.34);
        declaration.setYear(2019);
    }

    @Test
    public void testGetGroceriesAmount() {
        double expectedAmount = 12.34;
        double actualAmount = this.declaration.getGroceriesAmount();
        assertThat(actualAmount).isEqualTo(expectedAmount);
    }

    @Test
    public void testSetYear() {
        int expectedYear = 2020;
        this.declaration.setYear(2020);
        int actualYear = this.declaration.getYear();
        assertThat(actualYear).isEqualTo(expectedYear);
    }
}
