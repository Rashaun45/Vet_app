package com.vet_app;

import com.vet_clinic.vet_app.dao.jdbc.VetDAOJdbc;
import com.vet_clinic.vet_app.dao.jdbc.TreatmentDAOJdbc;
import com.vet_clinic.vet_app.dao.jdbc.StaffDAOJdbc;
import com.vet_clinic.vet_app.dao.jdbc.RoomDAOJdbc;
import com.vet_clinic.vet_app.dao.jdbc.PetDAOJdbc;
import com.vet_clinic.vet_app.dao.jdbc.PaymentDAOJdbc;
import com.vet_clinic.vet_app.dao.jdbc.OwnerDAOJdbc;
import com.vet_clinic.vet_app.dao.jdbc.LabTestDAOJdbc;
import com.vet_clinic.vet_app.dao.jdbc.DiagnosisDAOjdbc;
import com.vet_clinic.vet_app.dao.jdbc.AppointmentDAOJdbc;
import com.vet_clinic.vet_app.dao.*;
import com.vet_clinic.vet_app.model.*;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;
import com.vet_clinic.vet_app.util.ErrorHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Vet_app {

    private static final Logger LOGGER = LogManager.getLogger(Vet_app.class);

    public static void main(String[] args) {

        LOGGER.info("Vet app started");

        OwnerDAOJdbc ownerDAO = new OwnerDAOJdbc();
        PetDAOJdbc petDAO = new PetDAOJdbc();
        VetDAOJdbc vetDAO = new VetDAOJdbc();
        StaffDAOJdbc staffDAO = new StaffDAOJdbc();
        RoomDAOJdbc roomDAO = new RoomDAOJdbc();
        AppointmentDAOJdbc apptDAO = new AppointmentDAOJdbc();
        DiagnosisDAOjdbc diagDAO = new DiagnosisDAOjdbc();
        LabTestDAOJdbc testDAO = new LabTestDAOJdbc();
        TreatmentDAOJdbc treatDAO = new TreatmentDAOJdbc();
        PaymentDAOJdbc payDAO = new PaymentDAOJdbc();

        // ids of the rows this demo creates, so we can clean up at the end
        int ownerId = -1, petId = -1, vetId = -1, staffId = -1, roomId = -1;
        int apptId = -1, diagId = -1, testId = -1, treatId = -1, payId = -1;

        try {
            // ---------- 1. INSERT (create) ----------
            System.out.println("=== 1. INSERT ===");
            LOGGER.info("Section 1: INSERT");
            ownerId = ownerDAO.createOwner(new Owner("Demo", "Tester", "000-000-0001", "demo@example.com", "1 Demo Lane"));
            vetId = vetDAO.createVet(new Vet("Demo", "Vet", "Surgery", "LIC-DEMO", "000-000-0002", "vet@example.com"));
            staffId = staffDAO.createStaff(new Staff("Receptionist", "Demo", "Staff", "000-000-0003", "staff@example.com"));
            roomId = roomDAO.createRoom(new Room("Demo Room", "Outpatient", "Available"));
            petId = petDAO.createPet(new Pet(ownerId, "Buddy", "Dog", "Beagle", "Male",
                    Date.valueOf("2021-05-10"), 12.5, "MC-DEMO-1"));
            Timestamp now = new Timestamp(System.currentTimeMillis());
            apptId = apptDAO.createAppointment(new AppointmentVisit(petId, vetId, roomId, staffId,
                    now, "Scheduled", "Regular check-up"));
            diagId = diagDAO.createDiagnosis(new Diagnosis(apptId, vetId, "Ear infection", "EI-01", "Mild", now));
            testId = testDAO.createTest(new LabTest(apptId, null, "Swab", vetId, staffId, now));   // diagnosis is optional
            treatId = treatDAO.createTreatment(new Treatment(diagId, apptId, "Medication", "Ear drops",
                    "2 drops twice daily", "7 days", staffId, Date.valueOf("2026-01-01"), Date.valueOf("2026-01-08")));
            payId = payDAO.createPayment(new Payment(apptId, ownerId, 85.50, "Card", "Pending"));
            System.out.println("Created owner " + ownerId + ", pet " + petId + ", vet " + vetId + ", staff " + staffId
                    + ", room " + roomId + ", appointment " + apptId + ", diagnosis " + diagId
                    + ", test " + testId + ", treatment " + treatId + ", payment " + payId);

            // ---------- 2. SELECT with a condition ----------
            System.out.println("\n=== 2. SELECT WITH CONDITION ===");
            LOGGER.info("Section 2: SELECT WITH CONDITION");
            System.out.println("Owner by id      : " + ownerDAO.findById(ownerId));
            System.out.println("Owners by surname: " + ownerDAO.findByLastName("Tester").size() + " found");
            System.out.println("Pets of owner    : " + petDAO.findByOwnerId(ownerId));
            System.out.println("Dogs             : " + petDAO.findBySpecies("Dog").size() + " found");
            System.out.println("Surgeons         : " + vetDAO.findBySpecialty("Surgery").size() + " found");
            System.out.println("Receptionists    : " + staffDAO.findByRole("Receptionist").size() + " found");
            System.out.println("Available rooms  : " + roomDAO.findByStatus("Available").size() + " found");
            System.out.println("Diagnosis of visit: " + diagDAO.findByAppointmentId(apptId));
            System.out.println("Tests of visit    : " + testDAO.findByAppointmentId(apptId));
            System.out.println("Treatments        : " + treatDAO.findByDiagnosisId(diagId));
            System.out.println("Payments of owner : " + payDAO.findByOwnerId(ownerId));

            // ---------- 3. SELECT multiple rows / JOIN ----------
            System.out.println("\n=== 3. MULTIPLE SELECT ===");
            LOGGER.info("Section 3: MULTIPLE SELECT");
            List<Owner> owners = ownerDAO.getAllOwners();
            System.out.println("All owners: " + owners.size());
            System.out.println("All pets: " + petDAO.getAllPets().size()
                    + " | vets: " + vetDAO.getAllVets().size()
                    + " | staff: " + staffDAO.getAllStaff().size()
                    + " | rooms: " + roomDAO.getAllRooms().size());
            System.out.println("All appointments: " + apptDAO.getAllAppointments().size()
                    + " | diagnoses: " + diagDAO.getAllDiagnoses().size()
                    + " | tests: " + testDAO.getAllTests().size()
                    + " | treatments: " + treatDAO.getAllTreatments().size()
                    + " | payments: " + payDAO.getAllPayments().size());
            System.out.println("Scheduled appointments (JOIN of 5 tables):");
            apptDAO.printAppointmentsByStatus("Scheduled");

            // ---------- 4. UPDATE ----------
            System.out.println("\n=== 4. UPDATE ===");
            LOGGER.info("Section 4: UPDATE");
            System.out.println("Owner phone updated   : " + ownerDAO.updatePhone(ownerId, "999-999-9999"));
            System.out.println("Pet weight updated    : " + petDAO.updateWeight(petId, 13.1));
            System.out.println("Vet phone updated     : " + vetDAO.updatePhone(vetId, "888-888-8888"));
            System.out.println("Staff role updated    : " + staffDAO.updateRole(staffId, "Vet Technician"));
            System.out.println("Room status updated   : " + roomDAO.updateStatus(roomId, "Occupied"));
            System.out.println("Appointment status    : " + apptDAO.updateStatus(apptId, "Completed"));
            System.out.println("Diagnosis notes       : " + diagDAO.updateNotes(diagId, "Improving"));
            System.out.println("Test result recorded  : " + testDAO.recordResult(testId, "Bacterial"));
            System.out.println("Treatment dosage      : " + treatDAO.updateDosage(treatId, "3 drops twice daily"));
            System.out.println("Payment marked paid   : " + payDAO.markPaid(payId));
            System.out.println("Total paid by owner   : " + payDAO.getTotalPaidByOwner(ownerId));
            System.out.println("Owner after update    : " + ownerDAO.findById(ownerId).getPhone());

            // ---------- 5. MANAGING RESULT SETS ----------
            System.out.println("\n=== 5. RESULT SET MANAGEMENT ===");
            LOGGER.info("Section 5: RESULT SET MANAGEMENT");
            ownerDAO.showOwnersScrollable();

            // ---------- 6. EXCEPTION HANDLING ----------
            System.out.println("\n=== 6. EXCEPTION HANDLING ===");
            LOGGER.info("Section 6: EXCEPTION HANDLING");

            // (a) no row found: not an exception, the DAO returns null (and logs a WARN)
            Owner missing = ownerDAO.findById(999999);
            System.out.println("Owner 999999 -> " + (missing == null ? "not found (null)" : missing));

            // (b) deleting a parent row that still has children -> foreign key violation
            try {
                ownerDAO.deleteOwner(ownerId);
            } catch (SQLIntegrityConstraintViolationException e) {
                ErrorHandler.handle("Delete owner that still has related rows", e);
            }

            // (c) inserting a child row for a parent that does not exist -> foreign key violation
            try {
                petDAO.createPet(new Pet(999999, "Ghost", "Cat", "None", "Female", null, 3.0, "MC-GHOST"));
            } catch (SQLIntegrityConstraintViolationException e) {
                ErrorHandler.handle("Insert pet for an owner that does not exist", e);
            }

            // (d) any other SQL problem (here: a column that is too long)
            try {
                ownerDAO.updatePhone(ownerId, "1".repeat(100));
            } catch (SQLException e) {
                ErrorHandler.handle("Update owner phone with a value that is too long", e);
            }

        } catch (SQLException e) {
            ErrorHandler.handle("Database problem", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected problem", e);
        } finally {
            // ---------- 7. DELETE (children first, parents last) ----------
            System.out.println("\n=== 7. DELETE / CLEAN UP ===");
            LOGGER.info("Section 7: DELETE / CLEAN UP");
            try {
                if (payId > 0)   System.out.println("Payment deleted     : " + payDAO.deletePayment(payId));
                if (treatId > 0) System.out.println("Treatment deleted   : " + treatDAO.deleteTreatment(treatId));
                if (testId > 0)  System.out.println("Test deleted        : " + testDAO.deleteTest(testId));
                if (diagId > 0)  System.out.println("Diagnosis deleted   : " + diagDAO.deleteDiagnosis(diagId));
                if (apptId > 0)  System.out.println("Appointment deleted : " + apptDAO.deleteAppointment(apptId));
                if (petId > 0)   System.out.println("Pet deleted         : " + petDAO.deletePet(petId));
                if (ownerId > 0) System.out.println("Owner deleted       : " + ownerDAO.deleteOwner(ownerId));
                if (vetId > 0)   System.out.println("Vet deleted         : " + vetDAO.deleteVet(vetId));
                if (staffId > 0) System.out.println("Staff deleted       : " + staffDAO.deleteStaff(staffId));
                if (roomId > 0)  System.out.println("Room deleted        : " + roomDAO.deleteRoom(roomId));
            } catch (SQLException e) {
                ErrorHandler.handle("Clean up", e);
            }
            LOGGER.info("Vet app finished");
        }
    }
}