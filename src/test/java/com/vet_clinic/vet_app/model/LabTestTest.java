package com.vet_clinic.vet_app.model;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

class LabTestTest {

    @Test
    void setDiagnosis_copiesTheDiagnosisId() {
        Diagnosis d = new Diagnosis();
        d.setDiagnosisId(3);
        LabTest t = new LabTest();
        t.setDiagnosis(d);
        assertEquals(3, t.getDiagnosisId());
    }

    @Test
    void setDiagnosisToNull_clearsTheDiagnosisId() {
        LabTest t = new LabTest();
        t.setDiagnosisId(5);
        t.setDiagnosis(null);
        assertNull(t.getDiagnosisId());
    }

    @Test
    void setOrderedByVet_copiesTheVetIdIntoOrderedBy() {
        Vet vet = new Vet();
        vet.setVetId(9);
        LabTest t = new LabTest();
        t.setOrderedByVet(vet);
        assertEquals(9, t.getOrderedBy());
    }
}