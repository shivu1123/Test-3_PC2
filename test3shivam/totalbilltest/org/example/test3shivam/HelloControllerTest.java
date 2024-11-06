package org.example.test3shivam;

import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    private HelloController controller;

    @BeforeEach
    void setUp() {
        controller = new HelloController();


        controller.pizzaSizeField = new TextField("L");
        controller.numberOfToppingsField = new TextField("3");
    }

    @Test
    void calculateTotalBill_SuccessCase() {


        double expectedTotal = 18.98;

        double actualTotal = controller.calculateTotalBill();


        System.out.println("Expected Total: " + expectedTotal);
        System.out.println("Actual Total: " + actualTotal);


        assertEquals(expectedTotal, actualTotal, 0.01,
                "The calculated total bill should be $18.98.");
    }
}
