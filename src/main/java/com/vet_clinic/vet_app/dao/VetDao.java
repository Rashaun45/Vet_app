
package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.model.Vet;
import java.sql.SQLException;
import java.util.List;


public interface VetDao {

    int createVet(Vet v) throws SQLException;

    boolean deleteVet(int vetId) throws SQLException;

    Vet findById(int vetId) throws SQLException;

    List<Vet> findBySpecialty(String specialty) throws SQLException;

    List<Vet> getAllVets() throws SQLException;

    boolean updatePhone(int vetId, String phone) throws SQLException;
    
}
