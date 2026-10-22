package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.Treatment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO {

    public int createTreatment(Treatment t) throws SQLException {
        String sql = "INSERT INTO treatment (diagnosis_id, appointment_id, treatment_type, description, dosage, duration, "
                + "administered_by, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getDiagnosisId());
            ps.setInt(2, t.getAppointmentId());
            ps.setString(3, t.getTreatmentType());
            ps.setString(4, t.getDescription());
            ps.setString(5, t.getDosage());
            ps.setString(6, t.getDuration());
            ps.setInt(7, t.getAdministeredBy());
            ps.setDate(8, t.getStartDate());
            ps.setDate(9, t.getEndDate());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public Treatment findById(int treatmentId) throws SQLException {
        String sql = "SELECT * FROM treatment WHERE treatment_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, treatmentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapTreatment(rs) : null;
            }
        }
    }

    public List<Treatment> findByDiagnosisId(int diagnosisId) throws SQLException {
        String sql = "SELECT * FROM treatment WHERE diagnosis_id = ?";
        List<Treatment> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, diagnosisId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapTreatment(rs));
            }
        }
        return list;
    }

    public List<Treatment> getAllTreatments() throws SQLException {
        String sql = "SELECT * FROM treatment ORDER BY treatment_id";
        List<Treatment> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapTreatment(rs));
        }
        return list;
    }

    public boolean updateDosage(int treatmentId, String dosage) throws SQLException {
        String sql = "UPDATE treatment SET dosage = ? WHERE treatment_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dosage);
            ps.setInt(2, treatmentId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteTreatment(int treatmentId) throws SQLException {
        String sql = "DELETE FROM treatment WHERE treatment_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, treatmentId);
            return ps.executeUpdate() > 0;
        }
    }

    private Treatment mapTreatment(ResultSet rs) throws SQLException {
        Treatment t = new Treatment();
        t.setTreatmentId(rs.getInt("treatment_id"));
        t.setDiagnosisId(rs.getInt("diagnosis_id"));
        t.setAppointmentId(rs.getInt("appointment_id"));
        t.setTreatmentType(rs.getString("treatment_type"));
        t.setDescription(rs.getString("description"));
        t.setDosage(rs.getString("dosage"));
        t.setDuration(rs.getString("duration"));
        t.setAdministeredBy(rs.getInt("administered_by"));
        t.setStartDate(rs.getDate("start_date"));
        t.setEndDate(rs.getDate("end_date"));
        return t;
    }
}