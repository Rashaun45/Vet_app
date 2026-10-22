package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.AppointmentVisit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public int createAppointment(AppointmentVisit a) throws SQLException {
        String sql = "INSERT INTO appointment_visit (pet_id, vet_id, room_id, created_by, scheduled_at, status, reason_for_visit) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getPetId());
            ps.setInt(2, a.getVetId());
            ps.setInt(3, a.getRoomId());
            ps.setInt(4, a.getCreatedBy());
            ps.setTimestamp(5, a.getScheduledAt());
            ps.setString(6, a.getStatus());
            ps.setString(7, a.getReasonForVisit());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public AppointmentVisit findById(int appointmentId) throws SQLException {
        String sql = "SELECT * FROM appointment_visit WHERE appointment_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapAppointment(rs) : null;
            }
        }
    }

    public List<AppointmentVisit> findByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM appointment_visit WHERE status = ? ORDER BY scheduled_at";
        List<AppointmentVisit> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapAppointment(rs));
            }
        }
        return list;
    }

    public List<AppointmentVisit> getAllAppointments() throws SQLException {
        String sql = "SELECT * FROM appointment_visit ORDER BY appointment_id";
        List<AppointmentVisit> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapAppointment(rs));
        }
        return list;
    }

    // MULTIPLE SELECT - JOIN across pet, owner, vet and room for a given status
    public void printAppointmentsByStatus(String status) throws SQLException {
        String sql = "SELECT a.appointment_id, a.scheduled_at, a.status, "
                + "p.name AS pet_name, o.first_name, o.last_name, "
                + "v.last_name AS vet_last, r.room_number "
                + "FROM appointment_visit a "
                + "JOIN pet   p ON a.pet_id   = p.pet_id "
                + "JOIN owner o ON p.owner_id = o.owner_id "
                + "JOIN vet   v ON a.vet_id   = v.vet_id "
                + "JOIN room  r ON a.room_id  = r.room_id "
                + "WHERE a.status = ? ORDER BY a.scheduled_at";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("  #" + rs.getInt("appointment_id")
                            + " | " + rs.getTimestamp("scheduled_at")
                            + " | " + rs.getString("pet_name")
                            + " (owner: " + rs.getString("first_name") + " " + rs.getString("last_name") + ")"
                            + " | Dr. " + rs.getString("vet_last")
                            + " | " + rs.getString("room_number")
                            + " | " + rs.getString("status"));
                }
            }
        }
    }

    public boolean updateStatus(int appointmentId, String status) throws SQLException {
        String sql = "UPDATE appointment_visit SET status = ? WHERE appointment_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM appointment_visit WHERE appointment_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        }
    }

    private AppointmentVisit mapAppointment(ResultSet rs) throws SQLException {
        AppointmentVisit a = new AppointmentVisit();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setPetId(rs.getInt("pet_id"));
        a.setVetId(rs.getInt("vet_id"));
        a.setRoomId(rs.getInt("room_id"));
        a.setCreatedBy(rs.getInt("created_by"));
        a.setScheduledAt(rs.getTimestamp("scheduled_at"));
        a.setCheckedInAt(rs.getTimestamp("checked_in_at"));
        a.setCompletedAt(rs.getTimestamp("completed_at"));
        a.setStatus(rs.getString("status"));
        a.setReasonForVisit(rs.getString("reason_for_visit"));
        return a;
    }
}