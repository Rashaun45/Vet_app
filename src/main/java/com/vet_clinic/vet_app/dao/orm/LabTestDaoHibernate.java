package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.dao.LabTestDao;
import com.vet_clinic.vet_app.model.AppointmentVisit;
import com.vet_clinic.vet_app.model.Diagnosis;
import com.vet_clinic.vet_app.model.LabTest;
import com.vet_clinic.vet_app.model.Staff;
import com.vet_clinic.vet_app.model.Vet;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.Session;

public class LabTestDaoHibernate extends BaseHibernateDao implements LabTestDao {
    @Override
    public int createTest(LabTest t) {
        return inTransaction(session -> {
            linkReferences(session, t);
            session.persist(t);
            return t.getTestId();
        });
    }

    @Override
    public LabTest findById(int testId) {
        try (Session session = openSession()) {
            return session.get(LabTest.class, testId);
        }
    }

    @Override
    public List<LabTest> findByAppointmentId(int appointmentId) {
        try (Session session = openSession()) {
            return session.createQuery("from LabTest t where t.appointmentId = :id", LabTest.class)
                    .setParameter("id", appointmentId)
                    .list();
        }
    }

    @Override
    public List<LabTest> getAllTests() {
        try (Session session = openSession()) {
            return session.createQuery("from LabTest t order by t.testId", LabTest.class).list();
        }
    }

    @Override
    public boolean recordResult(int testId, String result) {
        return inTransaction(session -> {
            LabTest t = session.get(LabTest.class, testId);
            if (t == null) {
                return false;
            }
            t.setResult(result);
            t.setCompletedAt(new Timestamp(System.currentTimeMillis()));
            return true;
        });
    }

    @Override
    public boolean deleteTest(int testId) {
        return inTransaction(session -> {
            LabTest t = session.get(LabTest.class, testId);
            if (t == null) {
                return false;
            }
            session.remove(t);
            return true;
        });
    }

    private void linkReferences(Session session, LabTest t) {
        if (t.getAppointment() == null) t.setAppointment(session.getReference(AppointmentVisit.class, t.getAppointmentId()));
        if (t.getDiagnosis() == null && t.getDiagnosisId() != null) t.setDiagnosis(session.getReference(Diagnosis.class, t.getDiagnosisId()));
        if (t.getOrderedByVet() == null)     t.setOrderedByVet(session.getReference(Vet.class, t.getOrderedBy()));
        if (t.getPerformedByStaff() == null) t.setPerformedByStaff(session.getReference(Staff.class, t.getPerformedBy()));
    }
}