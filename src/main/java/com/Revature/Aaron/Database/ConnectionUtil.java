package com.Revature.Aaron.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    
    public static final String username = "postgres";
    private static final String password = "pass";
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}