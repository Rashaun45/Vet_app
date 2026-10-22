package com.vet_clinic.vet_app.model;

public class AppointmentVisit {

    private int appointmentId;
    private int petId;
    private int vetId;
    private int roomId;
    private int createdBy;
    private java.sql.Timestamp scheduledAt;
    private java.sql.Timestamp checkedInAt;
    private java.sql.Timestamp completedAt;
    private String status;
    private String reasonForVisit;

    public AppointmentVisit() { }

    // Used when inserting a new row (the database generates the id)
    public AppointmentVisit(int petId, int vetId, int roomId, int createdBy, java.sql.Timestamp scheduledAt, String status, String reasonForVisit)
    {
        this.petId = petId;
        this.vetId = vetId;
        this.roomId = roomId;
        this.createdBy = createdBy;
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
    }
//Setters and Getters
    
    public int getAppointmentId()
    { return appointmentId; }
    
    public void setAppointmentId(int appointmentId) 
    { this.appointmentId = appointmentId; }
    
    public int getPetId() 
    { return petId; }
    
    public void setPetId(int petId) 
    { this.petId = petId; }
    
    public int getVetId()
    { return vetId; }
    
    public void setVetId(int vetId) 
    { this.vetId = vetId; }
    
    public int getRoomId()
    { return roomId; }
    
    public void setRoomId(int roomId) 
    { this.roomId = roomId; }
    
    public int getCreatedBy() 
    { return createdBy; }
    
    public void setCreatedBy(int createdBy)
    { this.createdBy = createdBy; }
    
    public java.sql.Timestamp getScheduledAt()
    { return scheduledAt; }
    
    public void setScheduledAt(java.sql.Timestamp scheduledAt) 
    { this.scheduledAt = scheduledAt; }
    
    public java.sql.Timestamp getCheckedInAt()
    { return checkedInAt; }
    
    public void setCheckedInAt(java.sql.Timestamp checkedInAt)
    { this.checkedInAt = checkedInAt; }
    
    public java.sql.Timestamp getCompletedAt() 
    { return completedAt; }
    
    public void setCompletedAt(java.sql.Timestamp completedAt) 
    { this.completedAt = completedAt; }
    
    public String getStatus() 
    { return status; }
    
    public void setStatus(String status) 
    { this.status = status; }
    
    public String getReasonForVisit() 
    { return reasonForVisit; }
    
    public void setReasonForVisit(String reasonForVisit)
    { this.reasonForVisit = reasonForVisit; }

    @Override
    public String toString() {
        return "AppointmentVisit{appointmentId=" + appointmentId
                + ", petId=" + petId
                + ", vetId=" + vetId
                + ", roomId=" + roomId
                + ", createdBy=" + createdBy
                + ", scheduledAt=" + scheduledAt
                + ", checkedInAt=" + checkedInAt
                + ", completedAt=" + completedAt
                + ", status=" + status
                + ", reasonForVisit=" + reasonForVisit + "}";
    }
}