package com.vet_clinic.vet_app.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import com.vet_clinic.vet_app.model.Vet;
import java.sql.SQLException;
import java.util.List;


public abstract class VetDaoContractTest {

    protected abstract VetDao createDao();

    private VetDao dao;
    private int vetId;

    @BeforeEach
    void setUp() throws SQLException {
        dao = createDao();
        vetId = dao.createVet(new Vet("Test", "Vet", "Surgery", "LIC-TEST-1", "555-0100", "test.vet@example.com"));
    }

    @AfterEach
    void tearDown() throws SQLException {
        dao.deleteVet(vetId);
    }

    @Test
    void createVet_returnsGeneratedId() {
        assertTrue(vetId > 0);
    }

    @Test
    void findById_returnsTheSavedVet() throws SQLException {
        Vet found = dao.findById(vetId);
        assertNotNull(found);
        assertEquals("Test", found.getFirstName());
        assertEquals("Vet", found.getLastName());
        assertEquals("Surgery", found.getSpecialty());
    }

    @Test
    void findById_returnsNullWhenMissing() throws SQLException {
        assertNull(dao.findById(-1));
    }

    @Test
    void findBySpecialty_includesTheSavedVet() throws SQLException {
        List<Vet> vets = dao.findBySpecialty("Surgery");
        assertTrue(vets.stream().anyMatch(v -> v.getVetId() == vetId));
    }

    @Test
    void getAllVets_includesTheSavedVet() throws SQLException {
        assertTrue(dao.getAllVets().stream().anyMatch(v -> v.getVetId() == vetId));
    }

    @Test
    void updatePhone_changesThePhone() throws SQLException {
        assertTrue(dao.updatePhone(vetId, "555-0199"));
        assertEquals("555-0199", dao.findById(vetId).getPhone());
    }

    @Test
    void updatePhone_returnsFalseWhenMissing() throws SQLException {
        assertFalse(dao.updatePhone(-1, "555-0000"));
    }

    @Test
    void deleteVet_removesTheRow() throws SQLException {
        assertTrue(dao.deleteVet(vetId));
        assertNull(dao.findById(vetId));
    }

    @Test
    void deleteVet_returnsFalseWhenMissing() throws SQLException {
        assertFalse(dao.deleteVet(-1));
    }
}