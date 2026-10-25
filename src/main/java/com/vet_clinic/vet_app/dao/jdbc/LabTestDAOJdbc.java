package com.vet_clinic.vet_app.dao.jdbc;

import com.vet_clinic.vet_app.dao.LabTestDao;
import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.LabTest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Works on the "test" table (named LabTest in Java so it is not confused with unit tests)
public class LabTestDAOJdbc implements LabTestDao {

    private static final Logger LOGGER = LogManager.getLogger(LabTestDAOJdbc.class);

    @Override
    public int createTest(LabTest t) throws SQLException {
        String sql = "INSERT INTO test (appointment_id, diagnosis_id, test_type, ordered_by, performed_by, ordered_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getAppointmentId());
            if (t.getDiagnosisId() == null) {
                ps.setNull(2, Types.INTEGER);          // diagnosis is optional
            } else {
                ps.setInt(2, t.getDiagnosisId());
            }
            ps.setString(3, t.getTestType());
            ps.setInt(4, t.getOrderedBy());
            ps.setInt(5, t.getPerformedBy());
            ps.setTimestamp(6, t.getOrderedAt());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                int id = keys.next() ? keys.getInt(1) : -1;
                LOGGER.info("{} succeeded, new id = {}", "createTest", id);
                return id;
            }
        }
    }

    @Override
    public LabTest findById(int testId) throws SQLException {
        String sql = "SELECT * FROM test WHERE test_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, testId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOGGER.debug("{} found a row for id {}", "findById", testId);
                    return mapTest(rs);
                }
                LOGGER.warn("{} found no row for id {}", "findById", testId);
                return null;
            }
        }
    }

    @Override
    public List<LabTest> findByAppointmentId(int appointmentId) throws SQLException {
        String sql = "SELECT * FROM test WHERE appointment_id = ?";
        List<LabTest> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapTest(rs));
            }
        }
        LOGGER.debug("{} returned {} row(s)", "findByAppointmentId", list.size());
        return list;
    }

    @Override
    public List<LabTest> getAllTests() throws SQLException {
        String sql = "SELECT * FROM test ORDER BY test_id";
        List<LabTest> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapTest(rs));
        }
        LOGGER.debug("{} returned {} row(s)", "getAllTests", list.size());
        return list;
    }

    // UPDATE - record the result and stamp the completion time
    @Override
    public boolean recordResult(int testId, String result) throws SQLException {
        String sql = "UPDATE test SET result = ?, completed_at = NOW() WHERE test_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, result);
            ps.setInt(2, testId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "recordResult", testId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "recordResult", testId);
            }
            return changed;
        }
    }

    @Override
    public boolean deleteTest(int testId) throws SQLException {
        String sql = "DELETE FROM test WHERE test_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, testId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "deleteTest", testId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "deleteTest", testId);
            }
            return changed;
        }
    }

    private LabTest mapTest(ResultSet rs) throws SQLException {
        LabTest t = new LabTest();
        t.setTestId(rs.getInt("test_id"));
        t.setAppointmentId(rs.getInt("appointment_id"));
        int diagId = rs.getInt("diagnosis_id");
        t.setDiagnosisId(rs.wasNull() ? null : diagId);   // NULL stays null
        t.setTestType(rs.getString("test_type"));
        t.setOrderedBy(rs.getInt("ordered_by"));
        t.setPerformedBy(rs.getInt("performed_by"));
        t.setResult(rs.getString("result"));
        t.setResultFileUrl(rs.getString("result_file_url"));
        t.setOrderedAt(rs.getTimestamp("ordered_at"));
        t.setCompletedAt(rs.getTimestamp("completed_at"));
        return t;
    }
}