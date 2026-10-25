package com.vet_clinic.vet_app.dao.jdbc;

import com.vet_clinic.vet_app.dao.PaymentDao;
import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaymentDAOJdbc implements PaymentDao {

    private static final Logger LOGGER = LogManager.getLogger(PaymentDAOJdbc.class);

    @Override
    public int createPayment(Payment p) throws SQLException {
        String sql = "INSERT INTO payment (appointment_id, owner_id, amount, payment_method, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getAppointmentId());
            ps.setInt(2, p.getOwnerId());
            ps.setDouble(3, p.getAmount());
            ps.setString(4, p.getPaymentMethod());
            ps.setString(5, p.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                int id = keys.next() ? keys.getInt(1) : -1;
                LOGGER.info("{} succeeded, new id = {}", "createPayment", id);
                return id;
            }
        }
    }

    @Override
    public Payment findById(int paymentId) throws SQLException {
        String sql = "SELECT * FROM payment WHERE payment_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOGGER.debug("{} found a row for id {}", "findById", paymentId);
                    return mapPayment(rs);
                }
                LOGGER.warn("{} found no row for id {}", "findById", paymentId);
                return null;
            }
        }
    }

    @Override
    public List<Payment> findByOwnerId(int ownerId) throws SQLException {
        String sql = "SELECT * FROM payment WHERE owner_id = ?";
        List<Payment> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapPayment(rs));
            }
        }
        LOGGER.debug("{} returned {} row(s)", "findByOwnerId", list.size());
        return list;
    }

    @Override
    public List<Payment> getAllPayments() throws SQLException {
        String sql = "SELECT * FROM payment ORDER BY payment_id";
        List<Payment> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapPayment(rs));
        }
        LOGGER.debug("{} returned {} row(s)", "getAllPayments", list.size());
        return list;
    }

    // Aggregate SELECT with a condition - total an owner has paid
    @Override
    public double getTotalPaidByOwner(int ownerId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM payment WHERE owner_id = ? AND status = 'Paid'";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                double total = rs.next() ? rs.getDouble("total") : 0;
                LOGGER.debug("{} for owner {} = {}", "getTotalPaidByOwner", ownerId, total);
                return total;
            }
        }
    }

    // UPDATE - mark as paid and stamp the time
    @Override
    public boolean markPaid(int paymentId) throws SQLException {
        String sql = "UPDATE payment SET status = 'Paid', paid_at = NOW() WHERE payment_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "markPaid", paymentId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "markPaid", paymentId);
            }
            return changed;
        }
    }

    @Override
    public boolean deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM payment WHERE payment_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "deletePayment", paymentId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "deletePayment", paymentId);
            }
            return changed;
        }
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setAppointmentId(rs.getInt("appointment_id"));
        p.setOwnerId(rs.getInt("owner_id"));
        p.setAmount(rs.getDouble("amount"));
        p.setPaymentMethod(rs.getString("payment_method"));
        p.setStatus(rs.getString("status"));
        p.setPaidAt(rs.getTimestamp("paid_at"));
        return p;
    }
}