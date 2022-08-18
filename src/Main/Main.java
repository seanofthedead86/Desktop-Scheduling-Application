package Main;

import Model.*;
import Utility.Connections;
import Utility.DBConnection;
import Utility.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;


/**
 *Main Class extends Application
 */
public class Main extends Application {

    /**
     *Starts login screen for application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Screens/LoginScreen.fxml"));
        primaryStage.setTitle("Cooper Consulting Scheduler V 1.0");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Main. Connects to database and adds Countries, Contacts, First Level Divisions to observables lists.
     */
    public static void main(String[] args) throws SQLException {

        //Value returning lambda expression
        //GeneralInterface square = n -> n * n;
        //System.out.println(square.calculateSquare(5));

        //GeneralInterface message = n -> "Hello "+ n;
        //System.out.println(message.getMessage("Sean"));

        Connections.countries();

        Connections.contacts();

        Connections.firstLevelDivision();

        launch(args);
        DBConnection.closeConnection();
    }
}
