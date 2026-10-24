package com.vet_clinic.vet_app.dao.jdbc;

import com.vet_clinic.vet_app.dao.VetDao;
import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.Vet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VetDAOJdbc implements VetDao {

    @Override
    public int createVet(Vet v) throws SQLException {
        String sql = "INSERT INTO vet (first_name, last_name, specialty, license_number, phone, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getFirstName());
            ps.setString(2, v.getLastName());
            ps.setString(3, v.getSpecialty());
            ps.setString(4, v.getLicenseNumber());
            ps.setString(5, v.getPhone());
            ps.setString(6, v.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    @Override
    public Vet findById(int vetId) throws SQLException {
        String sql = "SELECT * FROM vet WHERE vet_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vetId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapVet(rs) : null;
            }
        }
    }

    @Override
    public List<Vet> findBySpecialty(String specialty) throws SQLException {
        String sql = "SELECT * FROM vet WHERE specialty = ?";
        List<Vet> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, specialty);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapVet(rs));
            }
        }
        return list;
    }

    @Override
    public List<Vet> getAllVets() throws SQLException {
        String sql = "SELECT * FROM vet ORDER BY vet_id";
        List<Vet> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapVet(rs));
        }
        return list;
    }

    @Override
    public boolean updatePhone(int vetId, String phone) throws SQLException {
        String sql = "UPDATE vet SET phone = ? WHERE vet_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setInt(2, vetId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteVet(int vetId) throws SQLException {
        String sql = "DELETE FROM vet WHERE vet_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vetId);
            return ps.executeUpdate() > 0;
        }
    }

    private Vet mapVet(ResultSet rs) throws SQLException {
        Vet v = new Vet();
        v.setVetId(rs.getInt("vet_id"));
        v.setFirstName(rs.getString("first_name"));
        v.setLastName(rs.getString("last_name"));
        v.setSpecialty(rs.getString("specialty"));
        v.setLicenseNumber(rs.getString("license_number"));
        v.setPhone(rs.getString("phone"));
        v.setEmail(rs.getString("email"));
        return v;
    }
}