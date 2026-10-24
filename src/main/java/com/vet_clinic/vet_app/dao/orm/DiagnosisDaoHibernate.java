package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.dao.DiagnosisDao;
import com.vet_clinic.vet_app.model.AppointmentVisit;
import com.vet_clinic.vet_app.model.Diagnosis;
import com.vet_clinic.vet_app.model.Vet;
import java.util.List;
import org.hibernate.Session;

public class DiagnosisDaoHibernate extends BaseHibernateDao implements DiagnosisDao {
    @Override
    public int createDiagnosis(Diagnosis d) {
        return inTransaction(session -> {
            linkReferences(session, d);
            session.persist(d);
            return d.getDiagnosisId();
        });
    }

    @Override
    public Diagnosis findById(int diagnosisId) {
        try (Session session = openSession()) {
            return session.get(Diagnosis.class, diagnosisId);
        }
    }

    @Override
    public List<Diagnosis> findByAppointmentId(int appointmentId) {
        try (Session session = openSession()) {
            return session.createQuery("from Diagnosis d where d.appointmentId = :id", Diagnosis.class)
                    .setParameter("id", appointmentId)
                    .list();
        }
    }

    @Override
    public List<Diagnosis> getAllDiagnoses() {
        try (Session session = openSession()) {
            return session.createQuery("from Diagnosis d order by d.diagnosisId", Diagnosis.class).list();
        }
    }

    @Override
    public boolean updateNotes(int diagnosisId, String notes) {
        return inTransaction(session -> {
            Diagnosis d = session.get(Diagnosis.class, diagnosisId);
            if (d == null) {
                return false;
            }
            d.setNotes(notes);
            return true;
        });
    }

    @Override
    public boolean deleteDiagnosis(int diagnosisId) {
        return inTransaction(session -> {
            Diagnosis d = session.get(Diagnosis.class, diagnosisId);
            if (d == null) {
                return false;
            }
            session.remove(d);
            return true;
        });
    }

    private void linkReferences(Session session, Diagnosis d) {
        if (d.getAppointment() == null) d.setAppointment(session.getReference(AppointmentVisit.class, d.getAppointmentId()));
        if (d.getVet() == null)         d.setVet(session.getReference(Vet.class, d.getVetId()));
    }
}