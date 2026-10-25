package com.vet_clinic.vet_app.dao.jdbc;

import com.vet_clinic.vet_app.dao.RoomDao;
import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RoomDAOJdbc implements RoomDao {

    private static final Logger LOGGER = LogManager.getLogger(RoomDAOJdbc.class);

    @Override
    public int createRoom(Room r) throws SQLException {
        String sql = "INSERT INTO room (room_number, room_type, status) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getRoomNumber());
            ps.setString(2, r.getRoomType());
            ps.setString(3, r.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                int id = keys.next() ? keys.getInt(1) : -1;
                LOGGER.info("{} succeeded, new id = {}", "createRoom", id);
                return id;
            }
        }
    }

    @Override
    public Room findById(int roomId) throws SQLException {
        String sql = "SELECT * FROM room WHERE room_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOGGER.debug("{} found a row for id {}", "findById", roomId);
                    return mapRoom(rs);
                }
                LOGGER.warn("{} found no row for id {}", "findById", roomId);
                return null;
            }
        }
    }

    @Override
    public List<Room> findByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM room WHERE status = ?";
        List<Room> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRoom(rs));
            }
        }
        LOGGER.debug("{} returned {} row(s)", "findByStatus", list.size());
        return list;
    }

    @Override
    public List<Room> getAllRooms() throws SQLException {
        String sql = "SELECT * FROM room ORDER BY room_id";
        List<Room> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRoom(rs));
        }
        LOGGER.debug("{} returned {} row(s)", "getAllRooms", list.size());
        return list;
    }

    @Override
    public boolean updateStatus(int roomId, String status) throws SQLException {
        String sql = "UPDATE room SET status = ? WHERE room_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, roomId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "updateStatus", roomId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "updateStatus", roomId);
            }
            return changed;
        }
    }

    @Override
    public boolean deleteRoom(int roomId) throws SQLException {
        String sql = "DELETE FROM room WHERE room_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "deleteRoom", roomId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "deleteRoom", roomId);
            }
            return changed;
        }
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setRoomId(rs.getInt("room_id"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setRoomType(rs.getString("room_type"));
        r.setStatus(rs.getString("status"));
        return r;
    }
}