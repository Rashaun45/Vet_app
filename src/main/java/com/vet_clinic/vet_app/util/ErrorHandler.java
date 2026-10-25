package com.vet_clinic.vet_app.util;

import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * One place that logs every SQLException the application handles.
 * ERROR  = an operation failed but the application can carry on.
 * FATAL  = the database cannot be reached, so the application cannot continue.
 */
public final class ErrorHandler {

    private static final Logger LOGGER = LogManager.getLogger(ErrorHandler.class);

    private ErrorHandler() {
        // utility class: no instances
    }

    public static void handle(String action, SQLException e) {
        if (isConnectionProblem(e)) {
            LOGGER.fatal("{} - the database cannot be reached (SQLState {}, ErrorCode {})",
                    action, e.getSQLState(), e.getErrorCode(), e);
        } else {
            LOGGER.error("{} failed (SQLState {}, ErrorCode {})",
                    action, e.getSQLState(), e.getErrorCode(), e);
        }
    }

    // SQLState values starting with "08" mean a connection problem
    public static boolean isConnectionProblem(SQLException e) {
        String state = e.getSQLState();
        return state != null && state.startsWith("08");
    }
}