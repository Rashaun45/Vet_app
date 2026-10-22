package com.vet_clinic.vet_app.model;

public class Diagnosis {

    private int diagnosisId;
    private int appointmentId;
    private int vetId;
    private String description;
    private String diagnosisCode;
    private String notes;
    private java.sql.Timestamp diagnosedAt;

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