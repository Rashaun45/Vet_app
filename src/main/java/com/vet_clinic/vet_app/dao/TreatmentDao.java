
package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.model.Treatment;
import java.sql.SQLException;
import java.util.List;


public interface TreatmentDao {

    int createTreatment(Treatment t) throws SQLException;

    boolean deleteTreatment(int treatmentId) throws SQLException;

    List<Treatment> findByDiagnosisId(int diagnosisId) throws SQLException;

    Treatment findById(int treatmentId) throws SQLException;

    List<Treatment> getAllTreatments() throws SQLException;

    boolean updateDosage(int treatmentId, String dosage) throws SQLException;
    
}
