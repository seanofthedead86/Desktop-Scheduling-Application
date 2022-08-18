package Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBQuery class
 */
public class DBQuery {

    //private static Statement InsertStatement; // Statement reference

    private static PreparedStatement statement; // Statement reference

    //Create statement object
    //public static void setStatement (Connection conn) throws SQLException {
    //    InsertStatement = conn.createStatement();
    //}

    //Get statement object
    public static Statement getStatement() {
        return statement;
    }

    //Prepared Statement Object
    public static void setPreparedStatement (Connection conn, String sqlStatement) throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }

    //Get prepare statement object
    public static PreparedStatement getPrepareStatement() {
        return statement;
    }

    //public static void setStatement(Connection conn) {
    //}
}
