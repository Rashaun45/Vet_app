package com.vet_app.exception;

import java.sql.SQLException;

/** Wraps any SQLException so the rest of the app only deals with one checked exception type. */
public class DatabaseException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String sqlState;
    private final int errorCode;

    public DatabaseException(String message, SQLException cause) {
        super(message, cause);
        this.sqlState = cause == null ? null : cause.getSQLState();
        this.errorCode = cause == null ? 0 : cause.getErrorCode();
    }

    public String getSqlState() {
        return sqlState;
    }

    public int getErrorCode() {
        return errorCode;
    }
}