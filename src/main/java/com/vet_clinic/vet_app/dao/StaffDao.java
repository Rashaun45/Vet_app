
package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.model.Staff;
import java.sql.SQLException;
import java.util.List;


public interface StaffDao {

    int createStaff(Staff s) throws SQLException;

    boolean deleteStaff(int staffId) throws SQLException;

    Staff findById(int staffId) throws SQLException;

    List<Staff> findByRole(String role) throws SQLException;

    List<Staff> getAllStaff() throws SQLException;

    boolean updateRole(int staffId, String role) throws SQLException;
    
}
