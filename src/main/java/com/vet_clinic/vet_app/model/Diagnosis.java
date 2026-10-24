package com.vet_clinic.vet_app.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "diagnosis")
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosis_id")
    private int diagnosisId;

    @Column(name = "appointment_id")
    private int appointmentId;

    @Column(name = "vet_id")
    private int vetId;

    @Column(name = "description")
    private String description;

    @Column(name = "diagnosis_code")
    private String diagnosisCode;

    @Column(name = "notes")
    private String notes;

    @Column(name = "diagnosed_at")
    private java.sql.Timestamp diagnosedAt;

    //Relationship Hibernate
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentVisit appointment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @OneToMany(mappedBy = "diagnosis")
    private List<Treatment> treatments = new ArrayList<>();

    @OneToMany(mappedBy = "diagnosis")
    private List<LabTest> tests = new ArrayList<>();

    public Diagnosis() { }

    // Used when inserting a new row (the database generates the id)
    public Diagnosis(int appointmentId, int vetId, String description, String diagnosisCode, String notes, java.sql.Timestamp diagnosedAt)
    {
        this.appointmentId = appointmentId;
        this.vetId = vetId;
        this.description = description;
        this.diagnosisCode = diagnosisCode;
        this.notes = notes;
        this.diagnosedAt = diagnosedAt;
    }
//Setters and Getters
    
    public int getDiagnosisId()
    { return diagnosisId; }
    
    public void setDiagnosisId(int diagnosisId)
    { this.diagnosisId = diagnosisId; }
    
    public int getAppointmentId()
    { return appointmentId; }
    
    public void setAppointmentId(int appointmentId)
    { this.appointmentId = appointmentId; }
    
    public int getVetId() 
    { return vetId; }
    
    public void setVetId(int vetId) 
    { this.vetId = vetId; }
    
    public String getDescription()
    { return description; }
    
    public void setDescription(String description)
    { this.description = description; }
    
    public String getDiagnosisCode()
    { return diagnosisCode; }
    
    public void setDiagnosisCode(String diagnosisCode)
    { this.diagnosisCode = diagnosisCode; }
    
    public String getNotes()
    { return notes; }
    
    public void setNotes(String notes) 
    { this.notes = notes; }
    
    public java.sql.Timestamp getDiagnosedAt()
    { return diagnosedAt; }
    
    public void setDiagnosedAt(java.sql.Timestamp diagnosedAt) 
    { this.diagnosedAt = diagnosedAt; }

//Relationship getters and setters (the setters keep the matching id field in sync)

    public AppointmentVisit getAppointment()
    { return appointment; }

    public void setAppointment(AppointmentVisit appointment)
    { this.appointment = appointment; if (appointment != null) this.appointmentId = appointment.getAppointmentId(); }

    public Vet getVet()
    { return vet; }

    public void setVet(Vet vet)
    { this.vet = vet; if (vet != null) this.vetId = vet.getVetId(); }

    public List<Treatment> getTreatments()
    { return treatments; }

    public void setTreatments(List<Treatment> treatments)
    { this.treatments = treatments; }

    public List<LabTest> getTests()
    { return tests; }

    public void setTests(List<LabTest> tests)
    { this.tests = tests; }

    @Override
    public String toString() {
        return "Diagnosis{diagnosisId=" + diagnosisId
                + ", appointmentId=" + appointmentId
                + ", vetId=" + vetId
                + ", description=" + description
                + ", diagnosisCode=" + diagnosisCode
                + ", notes=" + notes
                + ", diagnosedAt=" + diagnosedAt + "}";
    }
}