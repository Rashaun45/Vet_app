package com.vet_clinic.vet_app.dao.jdbc;

import com.vet_clinic.vet_app.dao.OwnerDao;
import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.Owner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerDAOJdbc implements OwnerDao {

    // INSERT - returns the generated owner_id
    @Override
    public int createOwner(Owner o) throws SQLException {
        String sql = "INSERT INTO owner (first_name, last_name, phone, email, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getFirstName());
            ps.setString(2, o.getLastName());
            ps.setString(3, o.getPhone());
            ps.setString(4, o.getEmail());
            ps.setString(5, o.getAddress());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    // SELECT with a condition (one row)
    @Override
    public Owner findById(int ownerId) throws SQLException {
        String sql = "SELECT * FROM owner WHERE owner_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapOwner(rs) : null;
            }
        }
    }

    // SELECT with a condition (many rows)
    @Override
    public List<Owner> findByLastName(String lastName) throws SQLException {
        String sql = "SELECT * FROM owner WHERE last_name = ?";
        List<Owner> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lastName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapOwner(rs));
            }
        }
        return list;
    }

    // SELECT multiple rows
    @Override
    public List<Owner> getAllOwners() throws SQLException {
        String sql = "SELECT * FROM owner ORDER BY owner_id";
        List<Owner> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapOwner(rs));
        }
        return list;
    }

    // UPDATE
    @Override
    public boolean updatePhone(int ownerId, String phone) throws SQLException {
        String sql = "UPDATE owner SET phone = ? WHERE owner_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setInt(2, ownerId);
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE
    @Override
    public boolean deleteOwner(int ownerId) throws SQLException {
        String sql = "DELETE FROM owner WHERE owner_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            return ps.executeUpdate() > 0;
        }
    }

    // MANAGING RESULT SETS - scrollable ResultSet + metadata
    @Override
    public void showOwnersScrollable() throws SQLException {
        String sql = "SELECT owner_id, first_name, last_name, phone FROM owner ORDER BY owner_id";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql,
                     ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            System.out.println("Columns returned: " + meta.getColumnCount());
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                System.out.println("  " + i + ". " + meta.getColumnName(i) + " (" + meta.getColumnTypeName(i) + ")");
            }

            if (rs.last()) {
                System.out.println("Total rows: " + rs.getRow());
                System.out.println("Last owner : " + rs.getString("first_name") + " " + rs.getString("last_name"));
            }
            if (rs.first()) {
                System.out.println("First owner: " + rs.getString("first_name") + " " + rs.getString("last_name"));
            }
            rs.beforeFirst();
            System.out.println("All rows, top to bottom:");
            while (rs.next()) {
                System.out.println("  " + rs.getInt("owner_id") + " | " + rs.getString("first_name")
                        + " " + rs.getString("last_name") + " | " + rs.getString("phone"));
            }
        }
    }

    private Owner mapOwner(ResultSet rs) throws SQLException {
        Owner o = new Owner();
        o.setOwnerId(rs.getInt("owner_id"));
        o.setFirstName(rs.getString("first_name"));
        o.setLastName(rs.getString("last_name"));
        o.setPhone(rs.getString("phone"));
        o.setEmail(rs.getString("email"));
        o.setAddress(rs.getString("address"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }
}