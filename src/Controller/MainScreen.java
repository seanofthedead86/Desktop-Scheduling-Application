package Controller;

import Main.NavigateInterface;
import Model.Appointment;
import Model.Schedule;
import Utility.Connections;
import Utility.DBConnection;
import Utility.DBQuery;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.util.EventObject;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import static javafx.application.Application.launch;


/**
 * Main Screen. Lambda expression defined line 40-44. Lambda Expression to cut down on three lines of code to one. Used to navigate to the login screen.
 */
public class MainScreen implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * Lambda Expression to cut down on lines of code used to navigate from the main page.
     */
    NavigateInterface getNavigate = n ->{
        scene = FXMLLoader.load(getClass().getResource(n));
        stage.setScene(new Scene(scene));
        stage.show();
    };

    /**
     * Table View
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
     * Title
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

    /**
     * Month
     */
    @FXML
    private RadioButton month;

    /**
     * Week
     */
    @FXML
    private RadioButton week;

    /**
     * All
     */
    @FXML
    private RadioButton all;


    /**
     * @param event Log out. Clears user observable list and takes user to login screen. Lambda Expression to cut down on three lines of code to one. Used to navigate to the login screen.
     * @throws IOException
     */
    @FXML
    void logOut(ActionEvent event) throws IOException {
        Schedule.allUsers.clear();
        Schedule.allAppointments.clear();
        Schedule.allCustomers.clear();

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        getNavigate.getNavigateMessage("/Screens/LoginScreen.fxml");

    }


    /**
     * @param event Takes you to Country Report Screen.Lambda Expression to cut down on three lines of code to one. Used to navigate to the country report screen.
     * @throws IOException
     */
    @FXML
    void countryReportButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();;
        getNavigate.getNavigateMessage("/Screens/CountryReport.fxml");

    }


    /**
     * @param event Takes you to Contact Report Screen. Lambda Expression to cut down on three lines of code to one. Used to navigate to the contact report screen.
     * @throws IOException
     */
    @FXML
    void contactReportButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        getNavigate.getNavigateMessage("/Screens/ContactReport.fxml");

    }


    /**
     * @param event Lambda Expression to cut down on three lines of code to one. Used to navigate to the monthly report screen.
     * @throws IOException
     */
    @FXML
    void monthlyReportButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        getNavigate.getNavigateMessage("/Screens/MonthlyReport.fxml");
    }

    /**
     * Shows All items in the appointment observable list
     */
    @FXML
    void filterAll(ActionEvent event) {
        all.setSelected(true);
        week.setSelected(false);
        month.setSelected(false);
        appointmentTable.setItems(Schedule.getAllAppointments());
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationC.setCellValueFactory(new PropertyValueFactory<>("location"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

    }

    /**
     * Filters table by current month
     */
    @FXML
    void filterMonth(ActionEvent event) {
        month.setSelected(true);
        all.setSelected(false);
        week.setSelected(false);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus = now.plusMonths(1);
        FilteredList<Appointment> filteredList = new FilteredList<>(Schedule.getAllAppointments());
        filteredList.setPredicate(row -> {
            LocalDateTime rowDate = row.getStartTime();
            return rowDate.isAfter(now) && rowDate.isBefore(nowPlus);
        });
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

    }

    /**
     * Filters table by a week (next 7 days)
     */
    @FXML
    void filterWeek(ActionEvent event) {
        week.setSelected(true);
        month.setSelected(false);
        all.setSelected(false);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus = now.plusDays(7);
        FilteredList<Appointment> filteredList = new FilteredList<>(Schedule.getAllAppointments());
        filteredList.setPredicate(row -> {
            LocalDateTime rowDate = row.getStartTime();
            return rowDate.isAfter(now) && rowDate.isBefore(nowPlus);
        });
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

    }

    /**
     * @param event Takes you to the manage customer screen. Lambda Expression to cut down on three lines of code to one. Used to navigate to the manage customer screen
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void manageCustomerButton(ActionEvent event) throws IOException, SQLException {

        Schedule.allCustomers.clear();
        Connections.customer();
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        getNavigate.getNavigateMessage("/Screens/ManageCustomers.fxml");
    }


    /**
     * @param event Takes you to add appointment screen. Lambda Expression to cut down on three lines of code to one. Used to navigate to the add appointment screen.
     * @throws IOException
     */
    @FXML
    void addAppointment(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        getNavigate.getNavigateMessage("/Screens/AddAppointment.fxml");
    }

    /**
     * Deletes selected appointment
     */
    @FXML
    void deleteAppointment(ActionEvent event) throws SQLException, IOException {

        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        Connection conn = DBConnection.startConnection();
        String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID ='"+appointment.getAppointmentID()+"'";
        DBQuery.setPreparedStatement(conn, deleteStatement);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        ps.execute();

        Schedule.allAppointments.remove(appointmentTable.getSelectionModel().getSelectedItem());

        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("ERROR");
        alert2.setContentText(appointment.getType() + " appointment number " + appointment.getAppointmentID() + " has been Deleted");
        alert2.showAndWait();
        return;

    }
    private static int index;
    public static int modifyIndex() {
        return index;
    }

    /**
     * Gets selected item in the tableview and navigates to the modify appointment screen where data is populated
     */
    @FXML
    void modifyAppointment(ActionEvent event) throws IOException {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        index = Schedule.getAllCustomers().indexOf(selectedAppointment);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Screens/ModifyAppointment.fxml"));
        loader.load();
        ModifyAppointment ADMController = loader.getController();
        ADMController.sendAppointment(appointmentTable.getSelectionModel().getSelectedItem());
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Populates table with appointments
     */
    public void initialize(URL location, ResourceBundle resources) {

        appointmentTable.setItems(Schedule.getAllAppointments());
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationC.setCellValueFactory(new PropertyValueFactory<>("location"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }
}
