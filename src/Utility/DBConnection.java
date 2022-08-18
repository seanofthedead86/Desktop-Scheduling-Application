package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection class
 */
public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipaddress = "//wgudb.ucertify.com/WJ06Bqj";
    private static final String jdburl = protocol + vendorName + ipaddress;

    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;
    private static final String username = "U06Bqj";
    private static final String password = "53688715997";

    /**
     * Start connection. Used to connect to the database.
     */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdburl, username, password);
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void closeConnection() throws SQLException {
        conn.close();
        System.out.println("Connection Closed");

    }
}
