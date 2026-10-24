package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.dao.PaymentDao;
import com.vet_clinic.vet_app.model.AppointmentVisit;
import com.vet_clinic.vet_app.model.Owner;
import com.vet_clinic.vet_app.model.Payment;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.Session;

public class PaymentDaoHibernate extends BaseHibernateDao implements PaymentDao {
    @Override
    public int createPayment(Payment p) {
        return inTransaction(session -> {
            linkReferences(session, p);
            session.persist(p);
            return p.getPaymentId();
        });
    }

    @Override
    public Payment findById(int paymentId) {
        try (Session session = openSession()) {
            return session.get(Payment.class, paymentId);
        }
    }

    @Override
    public List<Payment> findByOwnerId(int ownerId) {
        try (Session session = openSession()) {
            return session.createQuery("from Payment p where p.ownerId = :ownerId", Payment.class)
                    .setParameter("ownerId", ownerId)
                    .list();
        }
    }

    @Override
    public List<Payment> getAllPayments() {
        try (Session session = openSession()) {
            return session.createQuery("from Payment p order by p.paymentId", Payment.class).list();
        }
    }

    @Override
    public double getTotalPaidByOwner(int ownerId) {
        try (Session session = openSession()) {
            Double total = session.createQuery(
                    "select sum(p.amount) from Payment p where p.ownerId = :ownerId and p.status = 'Paid'",
                    Double.class)
                    .setParameter("ownerId", ownerId)
                    .uniqueResult();
            return total == null ? 0 : total;
        }
    }

    @Override
    public boolean markPaid(int paymentId) {
        return inTransaction(session -> {
            Payment p = session.get(Payment.class, paymentId);
            if (p == null) {
                return false;
            }
            p.setStatus("Paid");
            p.setPaidAt(new Timestamp(System.currentTimeMillis()));
            return true;
        });
    }

    @Override
    public boolean deletePayment(int paymentId) {
        return inTransaction(session -> {
            Payment p = session.get(Payment.class, paymentId);
            if (p == null) {
                return false;
            }
            session.remove(p);
            return true;
        });
    }

    private void linkReferences(Session session, Payment p) {
        if (p.getAppointment() == null) p.setAppointment(session.getReference(AppointmentVisit.class, p.getAppointmentId()));
        if (p.getOwner() == null)       p.setOwner(session.getReference(Owner.class, p.getOwnerId()));
    }
}