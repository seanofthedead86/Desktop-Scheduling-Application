package Controller;

import Model.*;
import Utility.Connections;
import Utility.DBConnection;
import Utility.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Appointment class
 */
public class AddAppointment implements Initializable{
    Stage stage;
    Parent scene;

    /**
     * Appointment ID Text Field
     */
    @FXML
    private TextField inputAppointmentID;

    /**
     * Title text field
     */
    @FXML
    private TextField inputTitle;

    /**
     * Description Text Field
     */
    @FXML
    private TextField inputDescription;

    /**
     *Location text field
     */
    @FXML
    private TextField inputLocation;

    /**
     * Type text field
     */
    @FXML
    private TextField inputType;

    /**
     * Customer ID text field
     */
    @FXML
    private TextField inputCustomerID;

    /**
     * User ID text field
     */
    @FXML
    private TextField inputUserID;

    /**
     * End date date picker
     */
    @FXML
    private DatePicker inputEndDate;

    /**
     * Contact combo box
     */
    @FXML
    private ComboBox<Contacts> inputContact;

    /**
     * Start date date picker
     */
    @FXML
    private DatePicker inputStartDate;

    /**
     * Start Time combo box
     */
    @FXML
    private ComboBox<LocalTime> inputStartTime;

    /**
     *End time combo box
     */
    @FXML
    private ComboBox<LocalTime> inputEndTime;

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    /**
     *Cancel takes you back to the Main Screen
     */
    @FXML
    void cancel(ActionEvent event) throws IOException, SQLException {
        Schedule.allAppointments.clear();
        Connections.appointments();
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Screens/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void startTime(ActionEvent event) {
    }

    /**
     * Save button connects to database and take the information from the input fields
     * Dates are converted to UTC time
     * Fields are verified to ensure they are not empty
     * Dates are verified so that:
     * -Start date and time do not occur after the end date and time
     * -Date and time are selected
     * -That start and end dates and time do not occur and the same time as another appointment with the same customer
     * Execution on the insert SQL command
     */
    @FXML
    void save(ActionEvent event) throws SQLException, IOException {

        Contacts contact = inputContact.getSelectionModel().getSelectedItem();

        ZoneId zoneUTC = ZoneId.of("UTC");
        ZoneId zoneID = ZoneId.of("America/New_York");
        ZoneId zoneSys = ZoneId.systemDefault();
        LocalDate startDate = inputStartDate.getValue();
        LocalTime startTime = inputStartTime.getValue();
        LocalDate endDate = inputEndDate.getValue();
        LocalTime endTime = inputEndTime.getValue();
        LocalDate localZonedStartDate = null;
        LocalTime localZonedStartTime = null;
        LocalDateTime createdLocal = LocalDateTime.now();
        LocalDateTime updatedLocal = LocalDateTime.now();
        ZonedDateTime zoneCreated = ZonedDateTime.of(createdLocal, zoneID);
        ZonedDateTime localCreated = ZonedDateTime.ofInstant(zoneCreated.toInstant(), zoneUTC);
        LocalDateTime createdLDT = localCreated.toLocalDateTime();
        ZonedDateTime zoneUpdated = ZonedDateTime.of(updatedLocal, zoneID);
        ZonedDateTime localUpdated = ZonedDateTime.ofInstant(zoneUpdated.toInstant(), zoneUTC);
        LocalDateTime updatedLDT = localUpdated.toLocalDateTime();

        try {
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            ZonedDateTime zonedStart = ZonedDateTime.of(startDateTime, zoneSys);
            ZonedDateTime startZoneDateTime = ZonedDateTime.ofInstant(zonedStart.toInstant(), zoneUTC);
            localZonedStartTime = startZoneDateTime.toLocalTime();
            localZonedStartDate = startZoneDateTime.toLocalDate();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Start Date and/or Time is Empty");
            alert.showAndWait();
            return;
        }
        LocalDate localZonedEndDate;
        LocalTime localZonedEndTime;
        try {
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
            ZonedDateTime zonedEnd = ZonedDateTime.of(endDateTime, zoneSys);
            ZonedDateTime endZoneDateTime = ZonedDateTime.ofInstant(zonedEnd.toInstant(), zoneUTC);
            localZonedEndTime = endZoneDateTime.toLocalTime();
            localZonedEndDate = endZoneDateTime.toLocalDate();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("End Date and/or Time is Empty");
            alert.showAndWait();
            return;
        }

        Connection conn = DBConnection.startConnection();
        String insertStatement = "INSERT INTO appointments(Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        DBQuery.setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        String Appointment_ID = "0";
        String Title = inputTitle.getText();
        String Description = inputDescription.getText();
        String Location = inputLocation.getText();
        String Type = inputType.getText();
        String Start = localZonedStartDate.toString() + "T" + localZonedStartTime.toString() + ":00";
        ps.setString(6, Start);
        String End = localZonedEndDate.toString() + "T" + localZonedEndTime.toString() + ":00";
        ps.setString(7, End);
        String Create_Date = createdLDT.toString();
        String Created_By = "Admin";
        String Last_Update = updatedLDT.toString();
        String Last_Updated_By = "Admin";
        String Customer_ID = inputCustomerID.getText();
        String User_ID = inputUserID.getText();
        try {
            String Contact_ID = String.valueOf(contact.getContactID());
            ps.setString(14, Contact_ID);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please Choose a Contact");
            alert.showAndWait();
            return;
        }
        ps.setString(1, Appointment_ID);
        ps.setString(2, Title);
        ps.setString(3, Description);
        ps.setString(4, Location);
        ps.setString(5, Type);
        ps.setString(8, Create_Date);
        ps.setString(9, Created_By);
        ps.setString(10, Last_Update);
        ps.setString(11, Last_Updated_By);
        ps.setString(12, Customer_ID);
        ps.setString(13, User_ID);

        if (Title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Title is Empty");
            alert.showAndWait();
            return;
        }

        if (Description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Description is Empty");
            alert.showAndWait();
            return;
        }

        if (Location.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Location is Empty");
            alert.showAndWait();
            return;
        }

        if (Type.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Type is Empty");
            alert.showAndWait();
            return;
        }

        if (User_ID.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("User ID is Empty");
            alert.showAndWait();
            return;
        }

        LocalDate refDate = LocalDate.now();
        LocalTime openTime = LocalTime.of(8, 00);
        LocalDateTime refOpenDateTime = LocalDateTime.of(refDate, openTime);
        ZonedDateTime refStartTime = ZonedDateTime.of(refOpenDateTime, zoneID);
        if (startTime.isBefore(refStartTime.toLocalTime())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Start time is outside of normal business hours 8AM to 10PM EST");
            alert.showAndWait();
            return;
        }

        LocalTime closeTime = LocalTime.of(22, 00);
        LocalDateTime refEndDateTime = LocalDateTime.of(refDate, closeTime);
        ZonedDateTime refEndTime = ZonedDateTime.of(refEndDateTime, zoneID);
        if (endTime.isAfter(refEndTime.toLocalTime())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("End time is outside of normal business hours 8AM to 10PM EST");
            alert.showAndWait();
            return;
        }

        if (startDate.isAfter(endDate)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Start Date is After End Date");
            alert.showAndWait();
            return;
        }
        if (endDate.isBefore(startDate)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("End Date is Before Start Date");
            alert.showAndWait();
            return;
        }

        for (int i = 0; i < Schedule.getAllAppointments().size(); i++) {
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
            int custID = Integer.parseInt(inputCustomerID.getText());
            if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                if (startDateTime.isAfter(Schedule.getAllAppointments().get(i).getStartTime()) && startDateTime.isBefore(Schedule.getAllAppointments().get(i).getEndTime())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Customer has an appointment scheduled at the same time");
                    alert.showAndWait();
                    return;
                }
                if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                    if (endDateTime.isAfter(Schedule.getAllAppointments().get(i).getStartTime()) && endDateTime.isBefore(Schedule.getAllAppointments().get(i).getEndTime())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Customer has an appointment scheduled at the same time");
                        alert.showAndWait();
                        return;
                    }
                }
                if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                    if (startDateTime.equals(Schedule.getAllAppointments().get(i).getStartTime())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Customer has an appointment scheduled at the same time");
                        alert.showAndWait();
                        return;
                    }
                }
                if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                    if (startDateTime.equals(Schedule.getAllAppointments().get(i).getEndTime())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Customer has an appointment scheduled at the same time");
                        alert.showAndWait();
                        return;
                    }
                }
                if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                    if (endDateTime.equals(Schedule.getAllAppointments().get(i).getEndTime())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Customer has an appointment scheduled at the same time");
                        alert.showAndWait();
                        return;
                    }
                }
                if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                    if (endDateTime.equals(Schedule.getAllAppointments().get(i).getStartTime())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Customer has an appointment scheduled at the same time");
                        alert.showAndWait();
                        return;
                    }
                }
                if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                    if (startDateTime.isBefore(Schedule.getAllAppointments().get(i).getStartTime()) && endDateTime.isAfter(Schedule.getAllAppointments().get(i).getEndTime())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Customer has an appointment scheduled at the same time");
                        alert.showAndWait();
                        return;
                    }
                }
            }
        }

        ps.execute();

        Schedule.allAppointments.clear();
        Connections.appointments();
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Screens/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Contacts are set in the contacts combobox.
     * Observable list of times are created and set in the start and end times combo box
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){

        inputContact.setItems(Schedule.getAllContacts());

        ObservableList<LocalTime> time = FXCollections.observableArrayList(
                LocalTime.of(00,00),
                LocalTime.of(00,30),
                LocalTime.of(01,00),
                LocalTime.of(01,30),
                LocalTime.of(02,00),
                LocalTime.of(02,30),
                LocalTime.of(03,00),
                LocalTime.of(03,30),
                LocalTime.of(04,00),
                LocalTime.of(04,30),
                LocalTime.of(05,00),
                LocalTime.of(05,30),
                LocalTime.of(06,00),
                LocalTime.of(06,30),
                LocalTime.of(07,00),
                LocalTime.of(07,30),
                LocalTime.of(8,00),
                LocalTime.of(8,30),
                LocalTime.of(9,00),
                LocalTime.of(9,30),
                LocalTime.of(10,00),
                LocalTime.of(10,30),
                LocalTime.of(11,00),
                LocalTime.of(11,30),
                LocalTime.of(12,00),
                LocalTime.of(12,30),
                LocalTime.of(13,00),
                LocalTime.of(13,30),
                LocalTime.of(14,00),
                LocalTime.of(14,30),
                LocalTime.of(15,00),
                LocalTime.of(15,30),
                LocalTime.of(16,00),
                LocalTime.of(16,30),
                LocalTime.of(17,00),
                LocalTime.of(17,30),
                LocalTime.of(18,00),
                LocalTime.of(18,30),
                LocalTime.of(19,00),
                LocalTime.of(19,30),
                LocalTime.of(20,00),
                LocalTime.of(20,30),
                LocalTime.of(21,00),
                LocalTime.of(21,30),
                LocalTime.of(22,00),
                LocalTime.of(22,30),
                LocalTime.of(23,00),
                LocalTime.of(23,30)

        );

        inputStartTime.setItems(time);
        inputEndTime.setItems(time);

    }
}