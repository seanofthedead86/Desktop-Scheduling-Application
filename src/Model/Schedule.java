package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.time.LocalDateTime;

/**
 * Schedule class
 */
public class Schedule {

    //Appointments
    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    public static void addNewAppointment(Appointment newAppointment) {
        allAppointments.add(newAppointment);
    }

    public static void deleteAppointment (Appointment appointment){
        allAppointments.remove(appointment);
    }

    public static ObservableList<Appointment> getAllAppointments (){ return allAppointments; }

    public static LocalDateTime appointmentMonth (Appointment appointment) {
        return appointment.getEndTime().plusMonths(1);
    }

    //Customers
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public static void addNewCustomer(Customer newCustomer) {
        allCustomers.add(newCustomer);
    }

    public static void deleteCustomer (Customer customer){
        allCustomers.remove(customer);
    }

    public static ObservableList<Customer> getAllCustomers (){
        return allCustomers;
    }

    //Countries
    public static ObservableList<Countries> allCountries = FXCollections.observableArrayList();

    public static void addNewCountry(Countries newCountry) {
        allCountries.add(newCountry);
    }

    public static void deleteCountry (Countries country){
        allCountries.remove(country);
    }

    public static ObservableList<Countries> getAllCountries (){
        return allCountries;
    }

    //Contacts
    public static ObservableList<Contacts> allContacts = FXCollections.observableArrayList();

    public static void addNewContact(Contacts newContact) {
        allContacts.add(newContact);
    }

    public static void deleteContact (Contacts contact){
        allContacts.remove(contact);
    }

    public static ObservableList<Contacts> getAllContacts (){
        return allContacts;
    }

    //Divisions
    public static ObservableList<Divisions> allDivisions = FXCollections.observableArrayList();

    public static void addNewDivision(Divisions newDivision) {
        allDivisions.add(newDivision);
    }

    public static void deleteDivision (Divisions divisions){
        allDivisions.remove(divisions);
    }

    public static ObservableList<Divisions> getAllDivisions (){ return allDivisions; }

    //Users

    public static ObservableList<Users> allUsers = FXCollections.observableArrayList();
    public static void addNewUser (Users newUsers){allUsers.add(newUsers);}
    public static ObservableList<Users> getAllUsers () { return allUsers; }

}
