package Controller;

import Model.*;
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
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Modify Customer Class
 */
public class ModifyCustomer implements Initializable {
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

    @FXML
    private Button buttonSave;

    /**
     * Performs SQL update to update the record in the customers table based on the customer ID.
     */
    @FXML
    void save(ActionEvent event) throws SQLException, IOException {
        Divisions state = inputState.getSelectionModel().getSelectedItem();
        String Customer_ID = inputCustomerID.getText();
        String Customer_Name = inputName.getText();
        String Address = inputAddress.getText();
        String Postal_Code = inputZipCode.getText();
        String Phone = inputPhone.getText();
        String Last_Update = LocalDateTime.now().toString();
        String Last_Updated_By = "Sean";
        String Division_ID = String.valueOf(state.getDivisionID());


        Connection conn = DBConnection.startConnection();
        String updateStatement = "UPDATE customers SET Customer_Name = '"+ Customer_Name + "', Address='" + Address +"', Postal_Code='"+Postal_Code+"', Phone='"+Phone+"', Last_Update='"+Last_Update+"', Last_Updated_By='"+Last_Updated_By+"', Division_ID='"+Division_ID+"' WHERE Customer_ID ='"+Customer_ID+"'";
        DBQuery.setPreparedStatement(conn, updateStatement);
        PreparedStatement ps = DBQuery.getPrepareStatement();

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
        //inputState.setPromptText("State/Province");

        inputCountry.setItems(Schedule.getAllCountries());
        //inputCountry.setPromptText("Country");

        Divisions state = inputState.getSelectionModel().getSelectedItem();
        Countries country = inputCountry.getValue();
    }
    /**
     * Receives selected customer information from manage customer screen and populates the information in the input fields
     */
    public void sendCustomer (Customer customer) {

        int divID = customer.getDivisionID();
        //Divisions state = inputState.getItems().get(divID-1);

        inputCustomerID.setText(String.valueOf(customer.getCustomerID()));
        inputName.setText(String.valueOf(customer.getCustomerName()));
        inputAddress.setText(String.valueOf(customer.getAddress()));
        inputZipCode.setText(String.valueOf(customer.getZipCode()));
        inputPhone.setText(String.valueOf(customer.getPhoneNumber()));
        //inputState.setValue(state);


        for (int i = 0; i < Schedule.getAllDivisions().size(); i++) {
            if (customer.getDivisionID() == Schedule.getAllDivisions().get(i).getDivisionID()) {
                Divisions firstLevelDivision = Schedule.getAllDivisions().get(i);
                inputState.setValue(firstLevelDivision);
            }
        }
        for (int i = 0; i < Schedule.getAllCountries().size(); i++) {
            Divisions div = inputState.getValue();
            if ( div.getCountryID() == Schedule.getAllCountries().get(i).getCountryID()) {
                Countries country = Schedule.getAllCountries().get(i);
                inputCountry.setValue(country);
            }
        }
    }
}
