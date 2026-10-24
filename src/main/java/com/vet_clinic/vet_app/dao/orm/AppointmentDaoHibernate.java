package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.dao.AppointmentDao;
import com.vet_clinic.vet_app.model.AppointmentVisit;
import com.vet_clinic.vet_app.model.Pet;
import com.vet_clinic.vet_app.model.Room;
import com.vet_clinic.vet_app.model.Staff;
import com.vet_clinic.vet_app.model.Vet;
import java.util.List;
import org.hibernate.Session;

public class AppointmentDaoHibernate extends BaseHibernateDao implements AppointmentDao {
    private static final String SELECT_WITH_DETAILS =
            "select a from AppointmentVisit a "
            + "join fetch a.pet p join fetch p.owner join fetch a.vet "
            + "join fetch a.room join fetch a.createdByStaff ";

    @Override
    public int createAppointment(AppointmentVisit a) {
        return inTransaction(session -> {
            linkReferences(session, a);
            session.persist(a);
            return a.getAppointmentId();
        });
    }

    @Override
    public AppointmentVisit findById(int appointmentId) {
        try (Session session = openSession()) {
            return session.createQuery(
                    SELECT_WITH_DETAILS + "where a.appointmentId = :id", AppointmentVisit.class)
                    .setParameter("id", appointmentId)
                    .uniqueResult();
        }
    }

    @Override
    public List<AppointmentVisit> findByStatus(String status) {
        try (Session session = openSession()) {
            return session.createQuery(
                    SELECT_WITH_DETAILS + "where a.status = :status order by a.scheduledAt",
                    AppointmentVisit.class)
                    .setParameter("status", status)
                    .list();
        }
    }

    @Override
    public List<AppointmentVisit> getAllAppointments() {
        try (Session session = openSession()) {
            return session.createQuery(
                    SELECT_WITH_DETAILS + "order by a.appointmentId", AppointmentVisit.class)
                    .list();
        }
    }

    @Override
    public void printAppointmentsByStatus(String status) {
        for (AppointmentVisit a : findByStatus(status)) {
            System.out.println("  #" + a.getAppointmentId()
                    + " | " + a.getScheduledAt()
                    + " | " + a.getPet().getName()
                    + " (owner: " + a.getPet().getOwner().getFirstName()
                    + " " + a.getPet().getOwner().getLastName() + ")"
                    + " | Dr. " + a.getVet().getLastName()
                    + " | " + a.getRoom().getRoomNumber()
                    + " | " + a.getStatus());
        }
    }

    @Override
    public boolean updateStatus(int appointmentId, String status) {
        return inTransaction(session -> {
            AppointmentVisit a = session.get(AppointmentVisit.class, appointmentId);
            if (a == null) {
                return false;
            }
            a.setStatus(status);
            return true;
        });
    }

    @Override
    public boolean deleteAppointment(int appointmentId) {
        return inTransaction(session -> {
            AppointmentVisit a = session.get(AppointmentVisit.class, appointmentId);
            if (a == null) {
                return false;
            }
            session.remove(a);
            return true;
        });
    }

    private void linkReferences(Session session, AppointmentVisit a) {
        if (a.getPet() == null)            a.setPet(session.getReference(Pet.class, a.getPetId()));
        if (a.getVet() == null)            a.setVet(session.getReference(Vet.class, a.getVetId()));
        if (a.getRoom() == null)           a.setRoom(session.getReference(Room.class, a.getRoomId()));
        if (a.getCreatedByStaff() == null) a.setCreatedByStaff(session.getReference(Staff.class, a.getCreatedBy()));
    }
}