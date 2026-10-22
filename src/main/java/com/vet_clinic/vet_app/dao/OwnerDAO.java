package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.Owner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerDAO {

    // (a) INSERT: add a new owner and return the generated ID
    public int createOwner(Owner o) throws SQLException {
        String sql = "INSERT INTO owner (first_name, last_name, phone, email, address) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getFirstName());
            ps.setString(2, o.getLastName());
            ps.setString(3, o.getPhone());
            ps.setString(4, o.getEmail());
            ps.setString(5, o.getAddress());
            ps.executeUpdate();

            // getting the generated key (owner ID)
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // (b) SELECT with a condition: find one owner by ID (returns null if there is no such owner)
    public Owner findById(int id) throws SQLException {
        String sql = "SELECT * FROM owner WHERE owner_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapOwner(rs);
                }
            }
        }
        return null;
    }

    // (c) SELECT with a condition: find every owner with a given last name (can be many rows)
    public List<Owner> findByLastName(String lastName) throws SQLException {
        List<Owner> result = new ArrayList<>();
        String sql = "SELECT * FROM owner WHERE last_name = ? ORDER BY owner_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lastName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapOwner(rs));
                }
            }
        }
        return result;
    }

    // (d) SELECT multiple rows: list all owners
    public List<Owner> getAllOwners() throws SQLException {
        List<Owner> result = new ArrayList<>();
        String sql = "SELECT * FROM owner ORDER BY owner_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(mapOwner(rs));
            }
        }
        return result;
    }

    // (e) UPDATE: change an owner's phone number (returns false if no owner was changed)
    public boolean updatePhone(int id, String newPhone) throws SQLException {
        String sql = "UPDATE owner SET phone = ? WHERE owner_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPhone);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    // (f) DELETE: remove an owner (returns false if no owner was removed)
    public boolean deleteOwner(int id) throws SQLException {
        String sql = "DELETE FROM owner WHERE owner_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // (g) Managing a ResultSet: a scrollable ResultSet that we can move around in
    public void showOwnersScrollable() throws SQLException {
        String sql = "SELECT owner_id, first_name, last_name, phone FROM owner";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = st.executeQuery(sql)) {

            // The metadata describes the columns of the ResultSet
            ResultSetMetaData meta = rs.getMetaData();
            System.out.println("Columns in the result set: " + meta.getColumnCount());
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                System.out.println("  " + i + ". " + meta.getColumnName(i) + " (" + meta.getColumnTypeName(i) + ")");
            }

            // Jump to the last row to count the rows, then go back to the start
            rs.last();
            System.out.println("Rows in the result set: " + rs.getRow());
            rs.beforeFirst();

            while (rs.next()) {
                System.out.println("  " + rs.getInt("owner_id") + " | " + rs.getString("first_name")
                        + " " + rs.getString("last_name") + " | " + rs.getString("phone"));
            }

            // Move directly to the first and the last row
            rs.first();
            System.out.println("First row: " + rs.getString("last_name"));
            rs.last();
            System.out.println("Last row: " + rs.getString("last_name"));
        }
    }

    // Turns the current row of the ResultSet into an Owner object
    private Owner mapOwner(ResultSet rs) throws SQLException {
        Owner o = new Owner();
        o.setOwnerId(rs.getInt("owner_id"));
        o.setFirstName(rs.getString("first_name"));
        o.setLastName(rs.getString("last_name"));
        o.setPhone(rs.getString("phone"));
        o.setEmail(rs.getString("email"));
        o.setAddress(rs.getString("address"));
        return o;
    }
}