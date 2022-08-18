package Controller;

import Main.MessageInterface;
import Model.*;
import Utility.Connections;
import Utility.DBConnection;
import Utility.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;


/**
 * Monthly report. Lambda Expression used as an example to display a line of text in the user interface.
 */
public class CountryReport implements Initializable {

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
    private RadioButton month;

    @FXML
    private RadioButton week;

    @FXML
    private RadioButton all;

    /**
     * Month combo box
     */
    @FXML
    private ComboBox<Month> chooseMonth;

    /**
     * Type combo box
     */
    @FXML
    private ComboBox<Appointment> chooseType;

    @FXML
    private ComboBox<Countries> chooseCountry;

    @FXML
    private Button genButton;
    /**
     * Number of reports text
     */
    @FXML
    private Label numberOfReportsTXT;

    /**
     * Button takes you back to the main screen.
     */
    @FXML
    void manageApptBTN(ActionEvent event) throws IOException, SQLException {
        Schedule.allAppointments.clear();
        Connections.appointments();
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Screens/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }
    /**
     * @param event Generates report by pulling month and type and displaying matching entries in the table view
     * Alert is generated if either month or type isn't selected
     * Report text is generated with number of entries in the tableview
     * Lambda Expression used as an example to display a line of text in the user interface.
     */
    @FXML
    void genButton(ActionEvent event) throws SQLException {
        Schedule.allCustomers.clear();
        Connections.customer();
        String countryName = chooseCountry.getValue().getCountry();

        FilteredList<Customer> filteredList = new FilteredList<>(Schedule.getAllCustomers());
        filteredList.setPredicate(row -> {
            String rowCountryName = row.getCountry();
            return rowCountryName.equals(countryName); } );

        customerTable.setItems(filteredList);
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        zipCode.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        firstLevelDivision.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


        int size = customerTable.getItems().size();
        System.out.println("Items in table = "+size);
        MessageInterface message = n -> "Total number of appointments "+ n;
        numberOfReportsTXT.setText(message.getMessage(size));

    }

    /**
     * Initialize. Countries combo box set.
     */
    public void initialize(URL location, ResourceBundle resources) {

        chooseCountry.setItems(Schedule.getAllCountries());

    }
}
