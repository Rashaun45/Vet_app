package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.dao.RoomDao;
import com.vet_clinic.vet_app.model.Room;
import java.util.List;
import org.hibernate.Session;

public class RoomDaoHibernate extends BaseHibernateDao implements RoomDao {
    @Override
    public int createRoom(Room r) {
        return inTransaction(session -> {
            session.persist(r);
            return r.getRoomId();
        });
    }

    @Override
    public Room findById(int roomId) {
        try (Session session = openSession()) {
            return session.get(Room.class, roomId);
        }
    }

    @Override
    public List<Room> findByStatus(String status) {
        try (Session session = openSession()) {
            return session.createQuery("from Room r where r.status = :status", Room.class)
                    .setParameter("status", status)
                    .list();
        }
    }

    @Override
    public List<Room> getAllRooms() {
        try (Session session = openSession()) {
            return session.createQuery("from Room r order by r.roomId", Room.class).list();
        }
    }

    @Override
    public boolean updateStatus(int roomId, String status) {
        return inTransaction(session -> {
            Room r = session.get(Room.class, roomId);
            if (r == null) {
                return false;
            }
            r.setStatus(status);
            return true;
        });
    }

    @Override
    public boolean deleteRoom(int roomId) {
        return inTransaction(session -> {
            Room r = session.get(Room.class, roomId);
            if (r == null) {
                return false;
            }
            session.remove(r);
            return true;
        });
    }
}