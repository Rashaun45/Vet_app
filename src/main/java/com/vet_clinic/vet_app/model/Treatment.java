package com.vet_clinic.vet_app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "treatment")
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private int treatmentId;

    
    @Column(name = "diagnosis_id")
    private int diagnosisId;

    @Column(name = "appointment_id")
    private int appointmentId;

    @Column(name = "treatment_type")
    private String treatmentType;

    @Column(name = "description")
    private String description;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "duration")
    private String duration;

    @Column(name = "administered_by")
    private int administeredBy;

    @Column(name = "start_date")
    private java.sql.Date startDate;

    @Column(name = "end_date")
    private java.sql.Date endDate;

    // Relationships Hibernate
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "diagnosis_id", nullable = false)
    private Diagnosis diagnosis;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentVisit appointment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "administered_by", nullable = false)
    private Staff administeredByStaff;

    public Treatment() { }

    // Used when inserting a new row (the database generates the id)
    public Treatment(int diagnosisId, int appointmentId, String treatmentType, String description, String dosage, String duration, int administeredBy, java.sql.Date startDate, java.sql.Date endDate)
    {
        this.diagnosisId = diagnosisId;
        this.appointmentId = appointmentId;
        this.treatmentType = treatmentType;
        this.description = description;
        this.dosage = dosage;
        this.duration = duration;
        this.administeredBy = administeredBy;
        this.startDate = startDate;
        this.endDate = endDate;
    }
//Setters and Getters
    public int getTreatmentId() 
    { return treatmentId; }
    
    public void setTreatmentId(int treatmentId)
    { this.treatmentId = treatmentId; }
    
    public int getDiagnosisId()
    { return diagnosisId; }
    
    public void setDiagnosisId(int diagnosisId) 
    { this.diagnosisId = diagnosisId; }
    
    public int getAppointmentId()
    { return appointmentId; }
    
    public void setAppointmentId(int appointmentId)
    { this.appointmentId = appointmentId; }
    
    public String getTreatmentType() 
    { return treatmentType; }
    
    public void setTreatmentType(String treatmentType)
    { this.treatmentType = treatmentType; }
    
    public String getDescription()
    { return description; }
    
    public void setDescription(String description) 
    { this.description = description; }
    
    public String getDosage() 
    { return dosage; }
    
    public void setDosage(String dosage)
    { this.dosage = dosage; }
    
    public String getDuration() 
    { return duration; }
    
    public void setDuration(String duration) 
    { this.duration = duration; }
    
    public int getAdministeredBy() 
    { return administeredBy; }
    
    public void setAdministeredBy(int administeredBy) 
    { this.administeredBy = administeredBy; }
    
    public java.sql.Date getStartDate() 
    { return startDate; }
    
    public void setStartDate(java.sql.Date startDate) 
    { this.startDate = startDate; }
    
    public java.sql.Date getEndDate() 
    { return endDate; }
    
    public void setEndDate(java.sql.Date endDate)
    { this.endDate = endDate; }

//Relationship getters and setters (the setters keep the matching id field in sync)

    public Diagnosis getDiagnosis()
    { return diagnosis; }

    public void setDiagnosis(Diagnosis diagnosis)
    { this.diagnosis = diagnosis; if (diagnosis != null) this.diagnosisId = diagnosis.getDiagnosisId(); }

    public AppointmentVisit getAppointment()
    { return appointment; }

    public void setAppointment(AppointmentVisit appointment)
    { this.appointment = appointment; if (appointment != null) this.appointmentId = appointment.getAppointmentId(); }

    public Staff getAdministeredByStaff()
    { return administeredByStaff; }

    public void setAdministeredByStaff(Staff administeredByStaff)
    { this.administeredByStaff = administeredByStaff; if (administeredByStaff != null) this.administeredBy = administeredByStaff.getStaffId(); }

    @Override
    public String toString() {
        return "Treatment{treatmentId=" + treatmentId
                + ", diagnosisId=" + diagnosisId
                + ", appointmentId=" + appointmentId
                + ", treatmentType=" + treatmentType
                + ", description=" + description
                + ", dosage=" + dosage
                + ", duration=" + duration
                + ", administeredBy=" + administeredBy
                + ", startDate=" + startDate
                + ", endDate=" + endDate + "}";
    }
}