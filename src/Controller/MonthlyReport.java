package Controller;

import Main.MessageInterface;
import Model.Appointment;
import Model.Schedule;
import Utility.Connections;
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
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;


/**
 * Monthly report. Lambda Expression used as an example to display a line of text in the user interface.
 */
public class MonthlyReport implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * Appointment Table
     */
    @FXML
    private TableView<Appointment> appointmentTable;

    /**
     * Appointment ID
     */
    @FXML
    private TableColumn<Appointment, Integer> appointmentID;

    /**
     * Title
     */
    @FXML
    private TableColumn<Appointment,String> title;

    /**
     * Description
     */
    @FXML
    private TableColumn<Appointment, String> description;

    /**
     * Location
     */
    @FXML
    private TableColumn<Appointment, String> locationC;

    /**
     * Contact
     */
    @FXML
    private TableColumn<Appointment, String> contact;

    /**
     * Type
     */
    @FXML
    private TableColumn<Appointment, String> type;

    /**
     * Start
     */
    @FXML
    private TableColumn<Appointment, String> start;

    /**
     * End
     */
    @FXML
    private TableColumn<Appointment, String> end;

    /**
     * Customer ID
     */
    @FXML
    private TableColumn<Appointment, String> customerID;

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
    void genButton(ActionEvent event) {

        Month month = chooseMonth.getValue();
        if(month == null){
            Alert alert3 = new Alert(Alert.AlertType.ERROR);
            alert3.setTitle("ATTENTION!");
            alert3.setContentText("Please choose both month and type");
            alert3.showAndWait();
            return;
        }
        String typeVar = chooseType.getValue().toString();
        if(typeVar.isEmpty()){
            Alert alert3 = new Alert(Alert.AlertType.ERROR);
            alert3.setTitle("ATTENTION!");
            alert3.setContentText("Please choose both month and type");
            alert3.showAndWait();
            return;
        }
        FilteredList<Appointment> filteredList = new FilteredList<>(Schedule.getAllAppointments());
        filteredList.setPredicate(row -> {
            Month rowDate = row.getStartTime().getMonth();
            String rowType = row.getType();
            return rowDate == month && rowType.equals(typeVar); } );

        appointmentTable.setItems(filteredList);
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationC.setCellValueFactory(new PropertyValueFactory<>("location"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        int size = appointmentTable.getItems().size();
        MessageInterface message = n -> "Total number of appointments "+ n;
        numberOfReportsTXT.setText(message.getMessage(size));
    }

    /**
     * Initialize. Month observable list is created and combo boxes are set
     */
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Month> monthList = FXCollections.observableArrayList(
                Month.JANUARY,
                Month.FEBRUARY,
                Month.MARCH,
                Month.APRIL,
                Month.MAY,
                Month.JUNE,
                Month.JULY,
                Month.AUGUST,
                Month.SEPTEMBER,
                Month.OCTOBER,
                Month.NOVEMBER,
                Month.DECEMBER
        );

        chooseMonth.setItems(monthList);
        chooseType.setItems(Schedule.getAllAppointments());
    }
}
