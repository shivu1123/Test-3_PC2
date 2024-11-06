package org.example.test3shivam;

public class pizzaorderpage {
    private int customerID;
    private String customerName;
    private int mobileNumber; // Changed from String to int
    private String pizzaSize;
    private int totalToppings; // Already an int
    private double totalBill;

    public pizzaorderpage(int customerID, String customerName, int mobileNumber, String pizzaSize, int totalToppings, double totalBill) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.mobileNumber = mobileNumber;
        this.pizzaSize = pizzaSize;
        this.totalToppings = totalToppings;
        this.totalBill = totalBill;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(int mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public int getTotalToppings() {
        return totalToppings;
    }

    public void setTotalToppings(int totalToppings) {
        this.totalToppings = totalToppings;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }
}
