package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
public class MySQLConnection {

    private static Connection connection = null;
    private static final String URL = "jdbc:mysql://localhost:3306/e_commerce";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // Establish the connection
    public static Connection getConnection() {
       connection = null;
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connected to MySQL successfully!");
            }catch (SQLSyntaxErrorException e) {
                System.out.println("Connection failed!");
                
            }
            catch (SQLException e) {
                System.out.println("Connection failed!");
                
            }
        }
        return connection;
    }

    //  private static Connection connection = null;

    // private static final String HOST = "mysql-436bbed-student-f997.h.aivencloud.com";
    // private static final String PORT = "22721";
    // private static final String DATABASE_NAME = "e_commerce";
    // private static final String USERNAME = "avnadmin";
    // private static final String PASSWORD = "AVNS_ikPHseBNRutTggiBZ6w";
        
    // public static Connection getConnection() {
    //         try{
    //         connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE_NAME + "?sslmode=require", USERNAME, PASSWORD);
    //         System.out.println("mysql connection is succesful");
    //         }catch (SQLException d){
    //             System.out.println("ERROR: Could not connect to the server. Please check your network connection."+d.getMessage());
    //         }
    //     return connection;
    // }


    // Execute a query (SELECT)
    public static ResultSet executeQuery(String query) {
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Query execution failed!"+e.getMessage());
           
        }
        return null;
    }

    // Execute an update (INSERT, UPDATE, DELETE)
    public static int executeUpdate(String query) {
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Update execution failed!"+e.getMessage());
         
        }
        return 0;
    }

    // Close the connection
    // public static void closeConnection() {
    //     if (connection != null) {
    //         try {
    //             connection.close();
    //             connection = null;
    //             System.out.println("Connection closed.");
    //         } catch (SQLException e) {
    //             System.out.println("Failed to close the connection!");
              
    //         }
    //     }
    // }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        MySQLConnection.getConnection();
    }
    
}
