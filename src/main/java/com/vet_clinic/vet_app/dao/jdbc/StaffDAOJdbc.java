package com.vet_clinic.vet_app.dao.jdbc;

import com.vet_clinic.vet_app.dao.StaffDao;
import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StaffDAOJdbc implements StaffDao {

    private static final Logger LOGGER = LogManager.getLogger(StaffDAOJdbc.class);

    @Override
    public int createStaff(Staff s) throws SQLException {
        String sql = "INSERT INTO staff (role, first_name, last_name, phone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getRole());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                int id = keys.next() ? keys.getInt(1) : -1;
                LOGGER.info("{} succeeded, new id = {}", "createStaff", id);
                return id;
            }
        }
    }

    @Override
    public Staff findById(int staffId) throws SQLException {
        String sql = "SELECT * FROM staff WHERE staff_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOGGER.debug("{} found a row for id {}", "findById", staffId);
                    return mapStaff(rs);
                }
                LOGGER.warn("{} found no row for id {}", "findById", staffId);
                return null;
            }
        }
    }

    @Override
    public List<Staff> findByRole(String role) throws SQLException {
        String sql = "SELECT * FROM staff WHERE role = ?";
        List<Staff> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapStaff(rs));
            }
        }
        LOGGER.debug("{} returned {} row(s)", "findByRole", list.size());
        return list;
    }

    @Override
    public List<Staff> getAllStaff() throws SQLException {
        String sql = "SELECT * FROM staff ORDER BY staff_id";
        List<Staff> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapStaff(rs));
        }
        LOGGER.debug("{} returned {} row(s)", "getAllStaff", list.size());
        return list;
    }

    @Override
    public boolean updateRole(int staffId, String role) throws SQLException {
        String sql = "UPDATE staff SET role = ? WHERE staff_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setInt(2, staffId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "updateRole", staffId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "updateRole", staffId);
            }
            return changed;
        }
    }

    @Override
    public boolean deleteStaff(int staffId) throws SQLException {
        String sql = "DELETE FROM staff WHERE staff_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "deleteStaff", staffId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "deleteStaff", staffId);
            }
            return changed;
        }
    }

    private Staff mapStaff(ResultSet rs) throws SQLException {
        Staff s = new Staff();
        s.setStaffId(rs.getInt("staff_id"));
        s.setRole(rs.getString("role"));
        s.setFirstName(rs.getString("first_name"));
        s.setLastName(rs.getString("last_name"));
        s.setPhone(rs.getString("phone"));
        s.setEmail(rs.getString("email"));
        return s;
    }
}