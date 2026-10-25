package com.vet_clinic.vet_app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseConnection {

    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnection.class);

    // JDBC URL: where the database is, the port number, user and password.
    private static final String URL = "jdbc:mysql://localhost:3306/vet_clinic?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Returns a connection to the database.
    // If the connection fails it logs the problem and throws an SQLException,
    // which the calling code must handle.
    public static Connection getConnection() throws SQLException {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            LOGGER.debug("Opened a database connection");
            return con;
        } catch (SQLException e) {
            LOGGER.error("Could not open a database connection to {}", URL, e);
            throw e;
        }
    }
}