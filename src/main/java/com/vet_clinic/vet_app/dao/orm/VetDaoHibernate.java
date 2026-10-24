package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.dao.VetDao;
import com.vet_clinic.vet_app.model.Vet;
import java.util.List;
import org.hibernate.Session;

public class VetDaoHibernate extends BaseHibernateDao implements VetDao {
    @Override
    public int createVet(Vet v) {
        return inTransaction(session -> {
            session.persist(v);
            return v.getVetId();
        });
    }

    @Override
    public Vet findById(int vetId) {
        try (Session session = openSession()) {
            return session.get(Vet.class, vetId);
        }
    }

    @Override
    public List<Vet> findBySpecialty(String specialty) {
        try (Session session = openSession()) {
            return session.createQuery("from Vet v where v.specialty = :specialty", Vet.class)
                    .setParameter("specialty", specialty)
                    .list();
        }
    }

    @Override
    public List<Vet> getAllVets() {
        try (Session session = openSession()) {
            return session.createQuery("from Vet v order by v.vetId", Vet.class).list();
        }
    }

    @Override
    public boolean updatePhone(int vetId, String phone) {
        return inTransaction(session -> {
            Vet v = session.get(Vet.class, vetId);
            if (v == null) {
                return false;
            }
            v.setPhone(phone);
            return true;
        });
    }

    @Override
    public boolean deleteVet(int vetId) {
        return inTransaction(session -> {
            Vet v = session.get(Vet.class, vetId);
            if (v == null) {
                return false;
            }
            session.remove(v);
            return true;
        });
    }
}