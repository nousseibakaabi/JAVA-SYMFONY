package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class myBD {

    private final String URL = "jdbc:mysql://localhost:3306/integationwebjava";
    private final String USER = "root";
    private final String PASSWORD = "";
    private Connection connection;
    private static myBD instance;

    private myBD() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static myBD getInstance() {
        if(instance == null)
            instance = new myBD();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
