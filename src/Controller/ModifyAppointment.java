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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

/**
 * Modify Appointment
 */
public class ModifyAppointment implements Initializable{
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
     * Execution of the update SQL command
     */
    @FXML
    void save(ActionEvent event) throws SQLException, IOException {

        ZoneId zoneUTC = ZoneId.of("UTC");
        ZoneId zoneID = ZoneId.of("America/New_York");
        ZoneId zoneSys = ZoneId.systemDefault();
        LocalDate startDate = inputStartDate.getValue();
        LocalTime startTime = inputStartTime.getValue();
        LocalDate endDate = inputEndDate.getValue();
        LocalTime endTime = inputEndTime.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
        ZonedDateTime zonedStart = ZonedDateTime.of(startDateTime, zoneSys);
        ZonedDateTime zonedEnd = ZonedDateTime.of(endDateTime, zoneSys);
        ZonedDateTime startZoneDateTime = ZonedDateTime.ofInstant(zonedStart.toInstant(), zoneUTC);
        ZonedDateTime endZoneDateTime = ZonedDateTime.ofInstant(zonedEnd.toInstant(), zoneUTC);
        LocalTime localZonedStartTime = startZoneDateTime.toLocalTime();
        LocalDate localZonedStartDate = startZoneDateTime.toLocalDate();
        LocalTime localZonedEndTime = endZoneDateTime.toLocalTime();
        LocalDate localZonedEndDate = endZoneDateTime.toLocalDate();

        LocalDateTime updatedLocal = LocalDateTime.now();
        ZonedDateTime zoneUpdated = ZonedDateTime.of(updatedLocal, zoneID);
        ZonedDateTime localUpdated = ZonedDateTime.ofInstant(zoneUpdated.toInstant(), zoneUTC);
        LocalDateTime updatedLDT = localUpdated.toLocalDateTime();

        System.out.println(localZonedStartDate);
        System.out.println(localZonedStartTime);

        Contacts contact = inputContact.getSelectionModel().getSelectedItem();

        String Appointment_ID = inputAppointmentID.getText();
        String Title = inputTitle.getText();
        String Description = inputDescription.getText();
        String Location = inputLocation.getText();
        String Type = inputType.getText();

        //String Start = startZoneDateTime.toString();
        //String End = endZoneDateTime.toString();
        String Start = localZonedStartDate.toString() + "T" + localZonedStartTime.toString() + ":00";
        String End = localZonedEndDate.toString() + "T" + localZonedEndTime.toString() + ":00";
        //String Create_Date = LocalDateTime.now().toString();
        //String Created_By = "Admin";
        String Last_Update = updatedLDT.toString();
        String Last_Updated_By = "Admin";
        String Customer_ID = inputCustomerID.getText();
        String User_ID = inputUserID.getText();
        String Contact_ID;
        try {
            Contact_ID = String.valueOf(contact.getContactID());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please Choose a Contact");
            alert.showAndWait();
            return;
        }

        Connection conn = DBConnection.startConnection();
        String updateStatement = "UPDATE appointments SET Title = '" + Title + "', Description='" + Description + "', Location='" + Location + "', Type='" + Type + "', Start='" + Start + "', End='" + End + "', Last_Update='" + Last_Update + "', Last_Updated_By='" + Last_Updated_By + "', Customer_ID='" + Customer_ID + "', Contact_ID='" + Contact_ID + "' , User_ID='" + User_ID + "' WHERE Appointment_ID ='" + Appointment_ID + "'";
        DBQuery.setPreparedStatement(conn, updateStatement);
        PreparedStatement ps = DBQuery.getPrepareStatement();

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

        //LocalTime startTimeCheck = inputStartTime.getValue();
        LocalDate refDate = LocalDate.now();
        //ZoneId zoneID = ZoneId.of("America/New_York");
        LocalTime openTime = LocalTime.of(8,00);
        LocalDateTime refOpenDateTime = LocalDateTime.of(refDate, openTime);
        ZonedDateTime refStartTime = ZonedDateTime.of(refOpenDateTime, zoneID);
        //LocalTime refStartTime = LocalTime.of(8,00);
        if (startTime.isBefore(refStartTime.toLocalTime())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Start time is outside of normal business hours 8AM to 10PM EST");
            alert.showAndWait();
            return;
        }

        //LocalTime endTime = inputEndTime.getValue();
        LocalTime closeTime = LocalTime.of(22,00);
        LocalDateTime refEndDateTime = LocalDateTime.of(refDate, closeTime);
        ZonedDateTime refEndTime = ZonedDateTime.of(refEndDateTime, zoneID);
        //LocalTime refEndTime = ZonedDateTime.of(22,00);
        if (endTime.isAfter(refEndTime.toLocalTime())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("End time is outside of normal business hours 8AM to 10PM EST");
            alert.showAndWait();
            return;
        }

        if (startDate.isAfter(endDate)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Start Date is After End Date");
            alert.showAndWait();
            return;
        }
        if (endDate.isBefore(startDate)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("End Date is Before Start Date");
            alert.showAndWait();
            return;
        }

        for (int i = 0; i < Schedule.getAllAppointments().size(); i++) {
            LocalDateTime startDateTimeA = LocalDateTime.of(startDate, startTime);
            LocalDateTime endDateTimeA = LocalDateTime.of(endDate, endTime);
            int custID = Integer.parseInt(inputCustomerID.getText());
            int apptID = Integer.parseInt(inputAppointmentID.getText());
            if (Schedule.getAllAppointments().get(i).getAppointmentID() != apptID){
                if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                    if (startDateTimeA.isAfter(Schedule.getAllAppointments().get(i).getStartTime()) && startDateTimeA.isBefore(Schedule.getAllAppointments().get(i).getEndTime())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Customer has an appointment scheduled at the same time");
                        alert.showAndWait();
                        return;
                    }
                    if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                        if (endDateTimeA.isAfter(Schedule.getAllAppointments().get(i).getStartTime()) && endDateTimeA.isBefore(Schedule.getAllAppointments().get(i).getEndTime())){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setContentText("Customer has an appointment scheduled at the same time");
                            alert.showAndWait();
                            return;
                        }
                    }
                    if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                        if (startDateTimeA.equals(Schedule.getAllAppointments().get(i).getStartTime())){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setContentText("Customer has an appointment scheduled at the same time");
                            alert.showAndWait();
                            return;
                        }
                    }
                    if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                        if (startDateTimeA.equals(Schedule.getAllAppointments().get(i).getEndTime())){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setContentText("Customer has an appointment scheduled at the same time");
                            alert.showAndWait();
                            return;
                        }
                    }
                    if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                        if (endDateTimeA.equals(Schedule.getAllAppointments().get(i).getEndTime())){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setContentText("Customer has an appointment scheduled at the same time");
                            alert.showAndWait();
                            return;
                        }
                    }
                    if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                        if (endDateTimeA.equals(Schedule.getAllAppointments().get(i).getStartTime())){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setContentText("Customer has an appointment scheduled at the same time");
                            alert.showAndWait();
                            return;
                        }
                    }
                    if (custID == Schedule.getAllAppointments().get(i).getCustomerID()) {
                        if (startDateTimeA.isBefore(Schedule.getAllAppointments().get(i).getStartTime()) && endDateTimeA.isAfter(Schedule.getAllAppointments().get(i).getEndTime())){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setContentText("Customer has an appointment scheduled at the same time");
                            alert.showAndWait();
                            return;
                        }
                    }
                }
            }
            else {

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
     * Receives selected appointment information from main screen and populates it in the information fields
     */
    public void sendAppointment (Appointment appointment) {

        LocalDate dateStart = appointment.getStartTime().toLocalDate();
        LocalTime timeStart = appointment.getStartTime().toLocalTime();
        LocalDate dateEnd = appointment.getEndTime().toLocalDate();
        LocalTime timeEnd = appointment.getEndTime().toLocalTime();
        int contactName = appointment.getContactID();

        inputAppointmentID.setText(String.valueOf(appointment.getAppointmentID()));
        inputTitle.setText(String.valueOf(appointment.getTitle()));
        inputDescription.setText(String.valueOf(appointment.getDescription()));
        inputLocation.setText(String.valueOf(appointment.getLocation()));
        inputType.setText(String.valueOf(appointment.getType()));
        inputCustomerID.setText(String.valueOf(appointment.getCustomerID()));
        inputUserID.setText(String.valueOf(appointment.getUserID()));
        inputEndDate.setValue(dateEnd);
        //inputContact.setText(String.valueOf(appointment.getType()));
        inputStartTime.setValue(timeStart);
        inputStartDate.setValue(dateStart);
        inputEndTime.setValue(timeEnd);

        for (int i = 0; i < Schedule.getAllContacts().size(); i++) {
            if (appointment.getContactID() == Schedule.getAllContacts().get(i).getContactID()) {
                Contacts contact = Schedule.getAllContacts().get(i);
                inputContact.setValue(contact);
            }
        }
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
