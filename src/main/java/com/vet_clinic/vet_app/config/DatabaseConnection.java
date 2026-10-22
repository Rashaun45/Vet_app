package com.vet_clinic.vet_app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // JDBC URL: where the database is, the port number, user and password.
    private static final String URL = "jdbc:mysql://localhost:3306/vet_clinic?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Returns a connection to the database.
    // If the connection fails it throws an SQLException, which the calling code must handle.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}