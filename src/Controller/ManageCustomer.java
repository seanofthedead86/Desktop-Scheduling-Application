package Controller;

import Model.Appointment;
import Model.Customer;
import Model.Schedule;
import Utility.Connections;
import Utility.DBConnection;
import Utility.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

/**
 * Manage customers
 */
public class ManageCustomer implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> customerID;

    @FXML
    private TableColumn<Customer, String> name;

    @FXML
    private TableColumn<Customer, String> address;

    @FXML
    private TableColumn<Customer, String> zipCode;

    @FXML
    private TableColumn<Customer, String> firstLevelDivision;

    @FXML
    private TableColumn<Customer, String> country;

    @FXML
    private TableColumn<Customer, String> phoneNumber;

    @FXML
    private Button buttonAddCustomer;

    @FXML
    private Button buttonModifyCustomer;

    @FXML
    private Button buttonDeleteCustomer;

    @FXML
    private Button buttonMainScreen;

    /**
     * Moves to the add customer screen
     */
    @FXML
    void addCustomer(ActionEvent event) throws IOException {
        Schedule.allCustomers.clear();
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Screens/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Delete statement runs for selected customer ID.
     * Customer observable list is cleared and controller is reopened to populate the table with new data
     */
    @FXML
    void deleteCustomer(ActionEvent event) throws SQLException, IOException {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();

        Connection conn = DBConnection.startConnection();
        //String insertStatement = "INSERT INTO appointments(Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String insertStatement = "DELETE FROM customers WHERE Customer_ID ='"+customer.getCustomerID()+"'";
        DBQuery.setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ATTENTION!");
        alert.setContentText("Delete Customer "+customer.getCustomerName()+"?");
        alert.showAndWait();

        try {
            ps.execute();
        } catch (Exception e) {
            Alert alert3 = new Alert(Alert.AlertType.ERROR);
            alert3.setTitle("ATTENTION!");
            alert3.setContentText("Cannot Delete Customer " + customer.getCustomerName() + ".\r Customer has an appointment");
            alert3.showAndWait();
            return;
        }

        Schedule.allCustomers.clear();
        Connections.customer();
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Screens/ManageCustomers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("ATTENTION!");
        alert2.setContentText("Customer has been deleted");
        alert2.showAndWait();
        return;

    }

    /**
     * Navigates to main screen
     */
    @FXML
    void mainScreen(ActionEvent event) throws IOException, SQLException {
        Schedule.allAppointments.clear();
        Connections.appointments();
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Screens/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Index*/
    private static int index;

    /**Modify Index. Returns index*/
    public static int modifyIndex() {
        return index;
    }
    @FXML
    void modifyCustomer(ActionEvent event) throws IOException {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        index = Schedule.getAllCustomers().indexOf(selectedCustomer);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Screens/ModifyCustomer.fxml"));
        loader.load();

        ModifyCustomer ADMController = loader.getController();
        ADMController.sendCustomer(customerTable.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Initialize. Select statement with inner join retrieves info for customers and adds them to new object.
     * Customers are added to table view
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        customerTable.setItems(Schedule.getAllCustomers());
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        zipCode.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        firstLevelDivision.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


    }
}
