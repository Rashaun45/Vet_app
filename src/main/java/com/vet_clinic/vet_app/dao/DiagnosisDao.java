
package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.model.Diagnosis;
import java.sql.SQLException;
import java.util.List;

public interface DiagnosisDao {

    int createDiagnosis(Diagnosis d) throws SQLException;

    boolean deleteDiagnosis(int diagnosisId) throws SQLException;

    List<Diagnosis> findByAppointmentId(int appointmentId) throws SQLException;

    Diagnosis findById(int diagnosisId) throws SQLException;

    List<Diagnosis> getAllDiagnoses() throws SQLException;

    boolean updateNotes(int diagnosisId, String notes) throws SQLException;
    
}
