package com.vet_clinic.vet_app.model;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import org.junit.jupiter.api.Test;

class AppointmentVisitTest {

    @Test
    void setPet_storesThePetAndCopiesItsId() {
        Pet pet = new Pet();
        pet.setPetId(7);
        AppointmentVisit a = new AppointmentVisit();
        a.setPet(pet);
        assertSame(pet, a.getPet());
        assertEquals(7, a.getPetId());
    }

    @Test
    void setVet_copiesTheVetId() {
        Vet vet = new Vet();
        vet.setVetId(3);
        AppointmentVisit a = new AppointmentVisit();
        a.setVet(vet);
        assertEquals(3, a.getVetId());
    }

    @Test
    void setCreatedByStaff_copiesTheStaffIdIntoCreatedBy() {
        Staff staff = new Staff();
        staff.setStaffId(12);
        AppointmentVisit a = new AppointmentVisit();
        a.setCreatedByStaff(staff);
        assertEquals(12, a.getCreatedBy());
    }

    @Test
    void insertConstructor_setsTheGivenFieldsAndLeavesTheIdEmpty() {
        Timestamp when = Timestamp.valueOf("2026-11-02 09:30:00");
        AppointmentVisit a = new AppointmentVisit(1, 2, 3, 4, when, "Scheduled", "Checkup");
        assertEquals(1, a.getPetId());
        assertEquals(2, a.getVetId());
        assertEquals(3, a.getRoomId());
        assertEquals(4, a.getCreatedBy());
        assertEquals(when, a.getScheduledAt());
        assertEquals("Scheduled", a.getStatus());
        assertEquals("Checkup", a.getReasonForVisit());
        assertEquals(0, a.getAppointmentId());
        assertNull(a.getPet());
    }
}