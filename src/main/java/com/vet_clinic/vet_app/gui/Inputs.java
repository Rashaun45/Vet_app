package com.vet_clinic.vet_app.gui;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Small helpers that turn the text typed in a form into numbers, dates and ids.
 * A bad value throws IllegalArgumentException with a message the user can read.
 */
public final class Inputs {

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Inputs() { }

    public static String str(Map<String, String> v, String key) {
        return v.getOrDefault(key, "").trim();
    }

    /** Text that may be left empty: an empty box becomes null. */
    public static String optStr(Map<String, String> v, String key) {
        String s = str(v, key);
        return s.isEmpty() ? null : s;
    }

    /** A whole number greater than 0, used for ids that point at other tables. */
    public static int id(Map<String, String> v, String key) {
        try {
            int n = Integer.parseInt(str(v, key));
            if (n <= 0) {
                throw new NumberFormatException();
            }
            return n;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " must be a whole number greater than 0.");
        }
    }

    public static Integer optId(Map<String, String> v, String key) {
        return str(v, key).isEmpty() ? null : id(v, key);
    }

    public static double number(String label, String value) {
        try {
            double d = Double.parseDouble(value.trim());
            if (d < 0) {
                throw new NumberFormatException();
            }
            return d;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " must be a number, for example 12.50.");
        }
    }

    public static double number(Map<String, String> v, String key) {
        return number(key, str(v, key));
    }

    /** A number that may be left empty: an empty box becomes 0. */
    public static double optNumber(Map<String, String> v, String key) {
        return str(v, key).isEmpty() ? 0 : number(v, key);
    }

    public static Timestamp timestamp(Map<String, String> v, String key) {
        try {
            return Timestamp.valueOf(LocalDateTime.parse(str(v, key), DATE_TIME));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(key + " must look like 2026-11-20 14:30.");
        }
    }

    /** Like timestamp(), but an empty box means "right now". */
    public static Timestamp timestampOrNow(Map<String, String> v, String key) {
        return str(v, key).isEmpty() ? new Timestamp(System.currentTimeMillis()) : timestamp(v, key);
    }

    public static Date optDate(Map<String, String> v, String key) {
        if (str(v, key).isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(LocalDate.parse(str(v, key)));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(key + " must look like 2026-11-20.");
        }
    }

    // ---- turning values back into text for the form and the table ----
    public static String fmt(Timestamp t) {
        return t == null ? "" : t.toLocalDateTime().format(DATE_TIME);
    }

    public static String fmt(Date d) {
        return d == null ? "" : d.toString();
    }

    public static String fmt(Object o) {
        return o == null ? "" : o.toString();
    }
}