
package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.model.Owner;
import java.sql.SQLException;
import java.util.List;


public interface OwnerDao {

    // INSERT - returns the generated owner_id
    int createOwner(Owner o) throws SQLException;

    // DELETE
    boolean deleteOwner(int ownerId) throws SQLException;

    // SELECT with a condition (one row)
    Owner findById(int ownerId) throws SQLException;

    // SELECT with a condition (many rows)
    List<Owner> findByLastName(String lastName) throws SQLException;

    // SELECT multiple rows
    List<Owner> getAllOwners() throws SQLException;

    // MANAGING RESULT SETS - scrollable ResultSet + metadata
    void showOwnersScrollable() throws SQLException;

    // UPDATE
    boolean updatePhone(int ownerId, String phone) throws SQLException;
    
}
