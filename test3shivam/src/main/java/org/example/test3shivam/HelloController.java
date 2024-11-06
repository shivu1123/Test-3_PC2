package org.example.test3shivam;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {

    @FXML
    private TextField customerIDField;
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField mobileNumberField;
    @FXML
    TextField pizzaSizeField;
    @FXML
    TextField numberOfToppingsField;
    @FXML
    private TableView<pizzaorderpage> orderTable;
    @FXML
    private TableColumn<pizzaorderpage, Integer> customerIDColumn;
    @FXML
    private TableColumn<pizzaorderpage, String> customerNameColumn, pizzaSizeColumn;
    @FXML
    private TableColumn<pizzaorderpage, Integer> mobileNumberColumn, numberOfToppingsColumn;
    @FXML
    private TableColumn<pizzaorderpage, Double> totalBillColumn;

    // Corrected database URL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db-test3"; // Use the correct DB name
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public void initialize() {
        // Initialize the table columns with PropertyValueFactory for each column
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        mobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        pizzaSizeColumn.setCellValueFactory(new PropertyValueFactory<>("pizzaSize"));
        numberOfToppingsColumn.setCellValueFactory(new PropertyValueFactory<>("totalToppings"));
        totalBillColumn.setCellValueFactory(new PropertyValueFactory<>("totalBill"));
    }

    @FXML
    private void handleInsertAction() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO pizzaorderpage (customerName, mobileNumber, pizzaSize, totalToppings, totalBill) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, customerNameField.getText());
            stmt.setInt(2, Integer.parseInt(mobileNumberField.getText())); // Parse mobile number as int
            stmt.setString(3, pizzaSizeField.getText());
            stmt.setInt(4, Integer.parseInt(numberOfToppingsField.getText()));
            stmt.setDouble(5, calculateTotalBill());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedCustomerID = generatedKeys.getInt(1);
                    customerIDField.setText(String.valueOf(generatedCustomerID));  // Set the auto-generated ID in the field
                }
            }
            showAlert("Insert", "Data inserted successfully!");
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to insert data: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteAction() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM pizzaorderpage WHERE customerID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(customerIDField.getText()));
            stmt.executeUpdate();
            showAlert("Delete", "Data deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete data: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateAction() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE pizzaorderpage SET customerName = ?, mobileNumber = ?, pizzaSize = ?, totalToppings = ?, totalBill = ? WHERE customerID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerNameField.getText());
            stmt.setInt(2, Integer.parseInt(mobileNumberField.getText())); // Parse mobile number as int
            stmt.setString(3, pizzaSizeField.getText());
            stmt.setInt(4, Integer.parseInt(numberOfToppingsField.getText()));
            stmt.setDouble(5, calculateTotalBill());
            stmt.setInt(6, Integer.parseInt(customerIDField.getText()));
            stmt.executeUpdate();
            showAlert("Update", "Data updated successfully!");
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update data: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoadAction() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM pizzaorderpage WHERE customerID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(customerIDField.getText()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                customerNameField.setText(rs.getString("customerName"));
                mobileNumberField.setText(String.valueOf(rs.getInt("mobileNumber")));
                pizzaSizeField.setText(rs.getString("pizzaSize"));
                numberOfToppingsField.setText(String.valueOf(rs.getInt("totalToppings")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load data: " + e.getMessage());
        }
    }

    @FXML
    private void handleFetchAction() {
        orderTable.getItems().clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM pizzaorderpage";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pizzaorderpage order = new pizzaorderpage(
                        rs.getInt("customerID"),
                        rs.getString("customerName"),
                        rs.getInt("mobileNumber"),
                        rs.getString("pizzaSize"),
                        rs.getInt("totalToppings"),
                        rs.getDouble("totalBill")
                );
                orderTable.getItems().add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch data: " + e.getMessage());
        }
    }

    @FXML
    private void handlePlaceOrderAction() {
        double totalBill = calculateTotalBill();
        handleInsertAction();
        pizzaorderpage order = new pizzaorderpage(
                0, // Temporary ID placeholder
                customerNameField.getText(),
                Integer.parseInt(mobileNumberField.getText()), // Parse mobile number as int
                pizzaSizeField.getText(),
                Integer.parseInt(numberOfToppingsField.getText()),
                totalBill
        );
        orderTable.getItems().add(order);
        // Show order details along with total bill in the alert
        showAlert("Place Order", "Order placed successfully!\n" +
                "Customer Name: " + customerNameField.getText() + "\n" +
                "Mobile Number: " + mobileNumberField.getText() + "\n" +
                "Pizza Size: " + pizzaSizeField.getText() + "\n" +
                "Toppings: " + numberOfToppingsField.getText() + "\n" +
                "Total Bill: $" + String.format("%.2f", totalBill)); // Formatting to two decimal places
    }

    // This method is now accessible for testing.
    public double calculateTotalBill() {
        double pizzaPrice;
        switch (pizzaSizeField.getText().toUpperCase()) {
            case "XL": pizzaPrice = 15.00; break;
            case "L": pizzaPrice = 12.00; break;
            case "M": pizzaPrice = 10.00; break;
            case "S": pizzaPrice = 8.00; break;
            default: pizzaPrice = 0.00; break;
        }
        int toppings = Integer.parseInt(numberOfToppingsField.getText());
        double total = pizzaPrice + (toppings * 1.50);
        return total * 1.15; // Including 15% HST
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Getter methods for testing
    public TextField getPizzaSizeField() {
        return pizzaSizeField;
    }

    public TextField getNumberOfToppingsField() {
        return numberOfToppingsField;
    }

    public TableView<pizzaorderpage> getOrderTable() {
        return orderTable;
    }
}
