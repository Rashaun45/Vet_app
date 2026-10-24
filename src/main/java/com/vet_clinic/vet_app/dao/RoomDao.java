
package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.model.Room;
import java.sql.SQLException;
import java.util.List;

public interface RoomDao {

    int createRoom(Room r) throws SQLException;

    boolean deleteRoom(int roomId) throws SQLException;

    Room findById(int roomId) throws SQLException;

    List<Room> findByStatus(String status) throws SQLException;

    List<Room> getAllRooms() throws SQLException;

    boolean updateStatus(int roomId, String status) throws SQLException;
    
}
