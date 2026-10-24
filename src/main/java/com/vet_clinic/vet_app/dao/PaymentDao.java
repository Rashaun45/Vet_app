
package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.model.Payment;
import java.sql.SQLException;
import java.util.List;


public interface PaymentDao {

    int createPayment(Payment p) throws SQLException;

    boolean deletePayment(int paymentId) throws SQLException;

    Payment findById(int paymentId) throws SQLException;

    List<Payment> findByOwnerId(int ownerId) throws SQLException;

    List<Payment> getAllPayments() throws SQLException;

    // Aggregate SELECT with a condition - total an owner has paid
    double getTotalPaidByOwner(int ownerId) throws SQLException;

    // UPDATE - mark as paid and stamp the time
    boolean markPaid(int paymentId) throws SQLException;
    
}
