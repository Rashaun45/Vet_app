package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.dao.TreatmentDao;
import com.vet_clinic.vet_app.model.AppointmentVisit;
import com.vet_clinic.vet_app.model.Diagnosis;
import com.vet_clinic.vet_app.model.Staff;
import com.vet_clinic.vet_app.model.Treatment;
import java.util.List;
import org.hibernate.Session;

public class TreatmentDaoHibernate extends BaseHibernateDao implements TreatmentDao {
    @Override
    public int createTreatment(Treatment t) {
        return inTransaction(session -> {
            linkReferences(session, t);
            session.persist(t);
            return t.getTreatmentId();
        });
    }

    @Override
    public Treatment findById(int treatmentId) {
        try (Session session = openSession()) {
            return session.get(Treatment.class, treatmentId);
        }
    }

    @Override
    public List<Treatment> findByDiagnosisId(int diagnosisId) {
        try (Session session = openSession()) {
            return session.createQuery("from Treatment t where t.diagnosisId = :id", Treatment.class)
                    .setParameter("id", diagnosisId)
                    .list();
        }
    }

    @Override
    public List<Treatment> getAllTreatments() {
        try (Session session = openSession()) {
            return session.createQuery("from Treatment t order by t.treatmentId", Treatment.class).list();
        }
    }

    @Override
    public boolean updateDosage(int treatmentId, String dosage) {
        return inTransaction(session -> {
            Treatment t = session.get(Treatment.class, treatmentId);
            if (t == null) {
                return false;
            }
            t.setDosage(dosage);
            return true;
        });
    }

    @Override
    public boolean deleteTreatment(int treatmentId) {
        return inTransaction(session -> {
            Treatment t = session.get(Treatment.class, treatmentId);
            if (t == null) {
                return false;
            }
            session.remove(t);
            return true;
        });
    }

    private void linkReferences(Session session, Treatment t) {
        if (t.getDiagnosis() == null)           t.setDiagnosis(session.getReference(Diagnosis.class, t.getDiagnosisId()));
        if (t.getAppointment() == null)         t.setAppointment(session.getReference(AppointmentVisit.class, t.getAppointmentId()));
        if (t.getAdministeredByStaff() == null) t.setAdministeredByStaff(session.getReference(Staff.class, t.getAdministeredBy()));
    }
}