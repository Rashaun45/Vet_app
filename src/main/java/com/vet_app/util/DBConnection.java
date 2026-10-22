package com.vet_app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Opens JDBC connections. */
public final class DBConnection {

    private static final Logger LOG = AppLogger.get(DBConnection.class);

    private static final String URL = "jdbc:mysql://localhost:3306/vet_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";   

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Could not open a database connection to " + URL, e);
            throw e;
        }
    }
}