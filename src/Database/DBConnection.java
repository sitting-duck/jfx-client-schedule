package Database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class opens and closes the connection to the database.
 */
public class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost:3306/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /**
     * Connects to the database so we can use it.
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, "root", "root"); // Reference Connection object
            //connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the database when we exit the program.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Returns the Connection object
     * @return Connection connection - the connection object for the MySQL Database.
     */
    public static Connection getConnection() {
        return connection;
    }

}
