package Controller;
import Model.Schedule;
import Model.Users;
import Utility.Connections;
import Utility.DBConnection;
import Utility.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Login Screen implements Initializable.
 */
public class LoginScreen implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * Username Input
     */
    @FXML
    private TextField usernameInput;

    /**
     * Password Input
     */
    @FXML
    private TextField passwordInput;

    /**
     * Submit Button
     */
    @FXML
    private Button submitButton;

    /**
     * Username Text
     */
    @FXML
    private Label UsernameText;

    /**
     * Password Text
     */
    @FXML
    private Label PasswordText;

    /**
     * Location Text
     */
    @FXML
    private Label LocationText;

    /**
     * Location Here Text
     */
    @FXML
    private Label locationHereText;

    /**
     * Welcome Text
     */
    @FXML
    private Label WelcomeText;

    /**
     * Submit Button action grabs the username and password user inputs and compares them to the users table in the database.
     * If the username = password, the user is moved to the next screen. False displays an error message that is
     * translated to either english or french based on the location of the user.
     */
    @FXML
    void submitButton(ActionEvent event) throws SQLException, IOException {

        ResourceBundle rb = ResourceBundle.getBundle("Main/C195", Locale.getDefault());

        String UN = usernameInput.getText();
        String PW = passwordInput.getText();

        Connection conn = DBConnection.startConnection();
        String selectStatement = "SELECT * FROM users";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPrepareStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();



        String fileName = "login_activity.txt", item;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        ZoneId zoneID = ZoneId.systemDefault();

        FileWriter fileWriter = new FileWriter(fileName, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);

        while(rs.next()){
            int userID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");


            outputFile.print("Login Attempt by " + UN + " on "+date + " at "+time+". ");


            if (UN.equals(userName) && PW.equals(password)){
                Users user = new Users(userID, userName);
                outputFile.print("Attempt was successful");
                outputFile.close();
                Schedule.addNewUser(user);
                Connections.appointments();
                //System.out.println(userName + " , " + password);
                stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/Screens/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                for (int i = 0; i < Schedule.getAllAppointments().size(); i++) {
                    if (Schedule.getAllAppointments().get(i).getUserID() == Schedule.getAllUsers().get(0).getUserID()){
                        System.out.println(Schedule.getAllUsers().get(0).getUserID());
                        LocalDateTime systemTime = LocalDateTime.now();
                        LocalDateTime startTime = Schedule.getAllAppointments().get(i).getStartTime();
                        LocalDate startDate = Schedule.getAllAppointments().get(i).getStartTime().toLocalDate();
                        LocalTime startTimeTime = Schedule.getAllAppointments().get(i).getStartTime().toLocalTime();
                        String locale = Schedule.getAllAppointments().get(i).getLocation();
                        Long timeDiff = ChronoUnit.MINUTES.between(startTime, systemTime);
                        Long interval = (timeDiff + -1) * -1;
                        if(interval > 0 && interval <=30){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("UPCOMING MEETING");
                            alert.setContentText("YOU HAVE AN UPCOMING MEETING IN "+interval+" MINUTES \nAppointment ID: " +Schedule.getAllAppointments().get(i).getAppointmentID()+"\nLocation: "+locale +"\nWhen: "+startDate+" at "+startTimeTime);
                            alert.showAndWait();
                            return;
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("UPCOMING MEETING");
                            alert.setContentText("You have no upcoming appointments");
                            alert.showAndWait();
                            return;
                        }
                    }
                    else {

                    }
                }
                return;
            }
            else {
                outputFile.print("Attempt failed.\r\n");
                outputFile.close();
            }
        }

        Alert alert = new Alert(Alert.AlertType.WARNING, rb.getString("AlertUNPW"));
        Optional<ButtonType> result = alert.showAndWait();


    }

    /**
     * Initialize statement. On opening of the screen the language resource bundle translates the text on the login screen
     * to either English or French based on the users locale. Login screen also uses the ZoneID to display the user's
     * location.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResourceBundle rb = ResourceBundle.getBundle("Main/C195", Locale.getDefault());
        WelcomeText.setText(rb.getString("Welcome"));
        UsernameText.setText(rb.getString("Username"));
        submitButton.setText(rb.getString("Login"));
        PasswordText.setText(rb.getString("Password"));
        LocationText.setText(rb.getString("Location"));
        locationHereText.setText(ZoneId.systemDefault().toString());
    }

}