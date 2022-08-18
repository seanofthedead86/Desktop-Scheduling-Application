package Utility;

import Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Connections Class
 */
public class Connections {

    /**
     * Customer select statement
     */
    public static void customer () throws SQLException {

        Connection conn = DBConnection.startConnection();
        String selectStatement = "SELECT * FROM (( customers INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID) INNER JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID)";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        ps.execute();

        ResultSet rs = ps.getResultSet();
            while (rs.next()){
            int customerID = rs.getInt("customers.Customer_ID");
            String name = rs.getString("customers.Customer_Name");
            String address = rs.getString("customers.Address");
            String zipCode = rs.getString("customers.Postal_Code");
            String phone = rs.getString("customers.Phone");
            LocalDateTime createDate = rs.getTimestamp("customers.Create_Date").toLocalDateTime();
            String createdBy = rs.getString("customers.Created_By");
            LocalDateTime lastUpdate = rs.getTimestamp("customers.Last_Update").toLocalDateTime();
            String lastUpdatedBy = rs.getString("customers.Last_Updated_By");
            int divisionID = rs.getInt("customers.Division_ID");
            String country = rs.getString("countries.Country");

            Customer customer = new Customer(customerID, name, address, zipCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID, country);
            Schedule.addNewCustomer(customer);
        }
    }



    /**
     * Countries select statement
     */
    public static void countries () throws SQLException {
        Connection conn = DBConnection.startConnection();
        String selectCountriesStatement = "SELECT * FROM countries";
        DBQuery.setPreparedStatement(conn, selectCountriesStatement);
        PreparedStatement psCountries = DBQuery.getPrepareStatement();
        psCountries.execute();
        ResultSet rsCountries = psCountries.getResultSet();

        while(rsCountries.next()){
            int countryID = rsCountries.getInt("Country_ID");
            String countryName = rsCountries.getString("Country");
            LocalDateTime createDate = rsCountries.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rsCountries.getString("Created_By");
            LocalDateTime lastUpdate = rsCountries.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = rsCountries.getString("Last_Updated_By");

            Countries country = new Countries(countryID, countryName, createDate, createdBy, lastUpdate, lastUpdatedBy);
            Schedule.addNewCountry(country);
        }
    }

    /**
     * Contacts select statement
     */
    public static void contacts () throws SQLException {
        Connection conn = DBConnection.startConnection();
        String selectContactsStatement = "SELECT * FROM contacts";
        DBQuery.setPreparedStatement(conn, selectContactsStatement);
        PreparedStatement psContacts = DBQuery.getPrepareStatement();
        psContacts.execute();
        ResultSet rsContacts = psContacts.getResultSet();

        while(rsContacts.next()){
            int contactID = rsContacts.getInt("Contact_ID");
            String contactName = rsContacts.getString("Contact_Name");
            String email = rsContacts.getString("Email");

            Contacts contact = new Contacts(contactID, contactName, email);
            Schedule.addNewContact(contact);
        }
    }

    /**
     * First level division select statement
     */
    public static void firstLevelDivision () throws SQLException {
        Connection conn = DBConnection.startConnection();
        String selectDivisionsStatement = "SELECT * FROM first_level_divisions";
        DBQuery.setPreparedStatement(conn, selectDivisionsStatement);
        PreparedStatement psDivisions = DBQuery.getPrepareStatement();
        psDivisions.execute();
        ResultSet rsDivisions = psDivisions.getResultSet();

        while(rsDivisions.next()){
            int divisionID = rsDivisions.getInt("Division_ID");
            String division = rsDivisions.getString("division");
            LocalDateTime createDate = rsDivisions.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rsDivisions.getString("Created_By");
            LocalDateTime lastUpdate = rsDivisions.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = rsDivisions.getString("Last_Updated_By");
            int countryID = rsDivisions.getInt("COUNTRY_ID");

            Divisions divisions = new Divisions(divisionID, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryID);
            Schedule.addNewDivision(divisions);
        }
    }

    /**
     * Appointments select statement
     */
    public static void appointments () throws SQLException {
        Connection conn = DBConnection.startConnection();
        String selectStatement = "SELECT * FROM appointments";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        ps.execute();

        ResultSet rs = ps.getResultSet();


        while(rs.next()) {
            int appointmentID = appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");

            String description = rs.getString("Description");

            String locationA = rs.getString("Location");

            String type = rs.getString("Type");

            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();

            LocalDateTime endTime = rs.getTimestamp("End").toLocalDateTime();

            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();

            String createdBy = rs.getString("Created_By");

            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();

            String lastUpdatedBy = rs.getString("Last_Updated_By");

            int customerID = rs.getInt("Customer_ID");

            int userID = rs.getInt("User_ID");

            int contactID = rs.getInt("Contact_ID");

            String contactName = null;
            for (int i = 0; i < Schedule.getAllContacts().size(); i++) {
                if (Schedule.getAllContacts().get(i).getContactID() == contactID) {
                    contactName = Schedule.getAllContacts().get(i).getContactName();
                }
            }

            ZoneId zoneUTC = ZoneId.of("UTC");
            ZoneId zoneLocal = ZoneId.systemDefault();
            ZonedDateTime zoneStart = ZonedDateTime.of(startTime, zoneUTC);
            ZonedDateTime startLocal = ZonedDateTime.ofInstant(zoneStart.toInstant(), zoneLocal);
            LocalDateTime startLDT = startLocal.toLocalDateTime();

            ZonedDateTime zoneEnd = ZonedDateTime.of(endTime, zoneUTC);
            ZonedDateTime endLocal = ZonedDateTime.ofInstant(zoneEnd.toInstant(), zoneLocal);
            LocalDateTime endLDT = endLocal.toLocalDateTime();

            ZonedDateTime zoneCreated = ZonedDateTime.of(createDate, zoneUTC);
            ZonedDateTime createdLocal = ZonedDateTime.ofInstant(zoneCreated.toInstant(), zoneLocal);
            LocalDateTime createdLDT = createdLocal.toLocalDateTime();

            ZonedDateTime zoneUpdated = ZonedDateTime.of(lastUpdate, zoneUTC);
            ZonedDateTime updatedLocal = ZonedDateTime.ofInstant(zoneUpdated.toInstant(), zoneLocal);
            LocalDateTime updatedLDT = updatedLocal.toLocalDateTime();

            Appointment appointment = new Appointment(appointmentID, title, description, locationA, type, startLDT, endLDT, createdLDT, createdBy, updatedLDT, lastUpdatedBy, customerID, userID, contactID, contactName);
            Schedule.addNewAppointment(appointment);

        }
    }
}
