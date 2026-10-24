/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.model.LabTest;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Rasha
 */
public interface LabTestDao {

    int createTest(LabTest t) throws SQLException;

    boolean deleteTest(int testId) throws SQLException;

    List<LabTest> findByAppointmentId(int appointmentId) throws SQLException;

    LabTest findById(int testId) throws SQLException;

    List<LabTest> getAllTests() throws SQLException;

    // UPDATE - record the result and stamp the completion time
    boolean recordResult(int testId, String result) throws SQLException;
    
}
