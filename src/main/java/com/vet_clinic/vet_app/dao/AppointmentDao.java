
package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.model.AppointmentVisit;
import java.sql.SQLException;
import java.util.List;


public interface AppointmentDao {

    int createAppointment(AppointmentVisit a) throws SQLException;

    boolean deleteAppointment(int appointmentId) throws SQLException;

    AppointmentVisit findById(int appointmentId) throws SQLException;

    List<AppointmentVisit> findByStatus(String status) throws SQLException;

    List<AppointmentVisit> getAllAppointments() throws SQLException;

    // MULTIPLE SELECT - JOIN across pet, owner, vet and room for a given status
    void printAppointmentsByStatus(String status) throws SQLException;

    boolean updateStatus(int appointmentId, String status) throws SQLException;
    
}
