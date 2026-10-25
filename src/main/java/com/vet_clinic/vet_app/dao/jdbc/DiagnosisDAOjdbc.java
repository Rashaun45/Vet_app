package com.vet_clinic.vet_app.dao.jdbc;

import com.vet_clinic.vet_app.dao.DiagnosisDao;
import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.Diagnosis;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DiagnosisDAOjdbc implements DiagnosisDao {

    private static final Logger LOGGER = LogManager.getLogger(DiagnosisDAOjdbc.class);

    @Override
    public int createDiagnosis(Diagnosis d) throws SQLException {
        String sql = "INSERT INTO diagnosis (appointment_id, vet_id, description, diagnosis_code, notes, diagnosed_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, d.getAppointmentId());
            ps.setInt(2, d.getVetId());
            ps.setString(3, d.getDescription());
            ps.setString(4, d.getDiagnosisCode());
            ps.setString(5, d.getNotes());
            ps.setTimestamp(6, d.getDiagnosedAt());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                int id = keys.next() ? keys.getInt(1) : -1;
                LOGGER.info("{} succeeded, new id = {}", "createDiagnosis", id);
                return id;
            }
        }
    }

    @Override
    public Diagnosis findById(int diagnosisId) throws SQLException {
        String sql = "SELECT * FROM diagnosis WHERE diagnosis_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, diagnosisId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOGGER.debug("{} found a row for id {}", "findById", diagnosisId);
                    return mapDiagnosis(rs);
                }
                LOGGER.warn("{} found no row for id {}", "findById", diagnosisId);
                return null;
            }
        }
    }

    @Override
    public List<Diagnosis> findByAppointmentId(int appointmentId) throws SQLException {
        String sql = "SELECT * FROM diagnosis WHERE appointment_id = ?";
        List<Diagnosis> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapDiagnosis(rs));
            }
        }
        LOGGER.debug("{} returned {} row(s)", "findByAppointmentId", list.size());
        return list;
    }

    @Override
    public List<Diagnosis> getAllDiagnoses() throws SQLException {
        String sql = "SELECT * FROM diagnosis ORDER BY diagnosis_id";
        List<Diagnosis> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapDiagnosis(rs));
        }
        LOGGER.debug("{} returned {} row(s)", "getAllDiagnoses", list.size());
        return list;
    }

    @Override
    public boolean updateNotes(int diagnosisId, String notes) throws SQLException {
        String sql = "UPDATE diagnosis SET notes = ? WHERE diagnosis_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, notes);
            ps.setInt(2, diagnosisId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "updateNotes", diagnosisId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "updateNotes", diagnosisId);
            }
            return changed;
        }
    }

    @Override
    public boolean deleteDiagnosis(int diagnosisId) throws SQLException {
        String sql = "DELETE FROM diagnosis WHERE diagnosis_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, diagnosisId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "deleteDiagnosis", diagnosisId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "deleteDiagnosis", diagnosisId);
            }
            return changed;
        }
    }

    private Diagnosis mapDiagnosis(ResultSet rs) throws SQLException {
        Diagnosis d = new Diagnosis();
        d.setDiagnosisId(rs.getInt("diagnosis_id"));
        d.setAppointmentId(rs.getInt("appointment_id"));
        d.setVetId(rs.getInt("vet_id"));
        d.setDescription(rs.getString("description"));
        d.setDiagnosisCode(rs.getString("diagnosis_code"));
        d.setNotes(rs.getString("notes"));
        d.setDiagnosedAt(rs.getTimestamp("diagnosed_at"));
        return d;
    }
}