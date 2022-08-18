package Controller;

import Model.Appointment;
import Model.Countries;
import Model.Divisions;
import Model.Schedule;
import Utility.Connections;
import Utility.DBConnection;
import Utility.DBQuery;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

/**
 * Add Customer class
 */
public class AddCustomer implements Initializable {
    Stage stage;
    Parent scene;

    /**
     * Customer ID text field
     */
    @FXML
    private TextField inputCustomerID;

    /**
     * Name text field
     */
    @FXML
    private TextField inputName;

    /**
     *Address text field
     */
    @FXML
    private TextField inputAddress;

    /**
     * Zip code text field
     */
    @FXML
    private TextField inputZipCode;

    /**
     * Phone text field
     */
    @FXML
    private TextField inputPhone;

    /**
     * Sate combo box
     */
    @FXML
    private ComboBox<Divisions> inputState;

    /**
     * Country combo box
     */
    @FXML
    private ComboBox<Countries> inputCountry;

    /**
     * When a state is selected, the Country combo box selection is changed to the matching country
     */
    @FXML
    void onActionState(ActionEvent event) {
    }

    @FXML
    void onActionCountry(ActionEvent event) {
        int country = inputCountry.getSelectionModel().getSelectedItem().getCountryID();
        FilteredList<Divisions> filteredList = new FilteredList<>(Schedule.getAllDivisions());
        filteredList.setPredicate(row -> {
            int rowCountryID = row.getCountryID();
            return rowCountryID == country; } );
        inputState.setItems(filteredList);

    }

    /**
     * Save button
     */
    @FXML
    private Button buttonSave;

    /**
     * SQL insert statement adds a new customer to the customers table
     */
    @FXML
    void save(ActionEvent event) throws SQLException, IOException {

        Divisions state = inputState.getSelectionModel().getSelectedItem();

        Connection conn = DBConnection.startConnection();
        String insertStatement = "INSERT INTO customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?,?,?,?,?,?,?,?,?,?)";
        DBQuery.setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        String Customer_ID = "0";
        String Customer_Name = inputName.getText();
        String Address = inputAddress.getText();
        String Postal_Code = inputZipCode.getText();
        String Phone = inputPhone.getText();
        String Create_Date = LocalDateTime.now().toString();
        String Created_By = "Sean";
        String Last_Update = LocalDateTime.now().toString();
        String Last_Updated_By = "Sean";
        String Division_ID = String.valueOf(state.getDivisionID());

        ps.setString(1,Customer_ID);
        ps.setString(2,Customer_Name);
        ps.setString(3,Address);
        ps.setString(4,Postal_Code);
        ps.setString(5,Phone);
        ps.setString(6,Create_Date);
        ps.setString(7,Created_By);
        ps.setString(8,Last_Update);
        ps.setString(9,Last_Updated_By);
        ps.setString(10,Division_ID);

        ps.execute();

        Schedule.allCustomers.clear();
        Connections.customer();
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Screens/ManageCustomers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Returns user to Manage Customers screen
     */
    @FXML
    void cancel(ActionEvent event) throws IOException, SQLException {
        Schedule.allCustomers.clear();
        Connections.customer();
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Screens/ManageCustomers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * State and Country combo items are set
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inputState.setItems(Schedule.getAllDivisions());
        inputCountry.setItems(Schedule.getAllCountries());

        Divisions state = inputState.getSelectionModel().getSelectedItem();
        Countries country = inputCountry.getValue();



}


    }
