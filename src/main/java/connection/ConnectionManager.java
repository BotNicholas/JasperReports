package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static connection.Constants.*;

public class ConnectionManager {
    private static Connection connection;

    public static Connection openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static Statement getStatement() throws SQLException {
        openConnection();
        return connection.createStatement();
    }

    public static boolean closeConnection() throws SQLException {
        if (!connection.isClosed() && connection != null){
            connection.close();
            return true;
        }
        return false;
    }
}
