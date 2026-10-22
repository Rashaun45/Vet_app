package com.vet_clinic.vet_app.model;

public class Treatment {

    private int treatmentId;
    private int diagnosisId;
    private int appointmentId;
    private String treatmentType;
    private String description;
    private String dosage;
    private String duration;
    private int administeredBy;
    private java.sql.Date startDate;
    private java.sql.Date endDate;

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