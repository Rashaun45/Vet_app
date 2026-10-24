package com.vet_clinic.vet_app.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "appointment_visit")
public class AppointmentVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private int appointmentId;

    @Column(name = "pet_id")
    private int petId;

    @Column(name = "vet_id")
    private int vetId;

    @Column(name = "room_id")
    private int roomId;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "scheduled_at", nullable = false)
    private java.sql.Timestamp scheduledAt;

    @Column(name = "checked_in_at")
    private java.sql.Timestamp checkedInAt;

    @Column(name = "completed_at")
    private java.sql.Timestamp completedAt;

    @Column(name = "status")
    private String status;

    @Column(name = "reason_for_visit")
    private String reasonForVisit;

    // Relationships  Hibernate) 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private Staff createdByStaff;

    @OneToMany(mappedBy = "appointment")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "appointment")
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @OneToMany(mappedBy = "appointment")
    private List<LabTest> tests = new ArrayList<>();

    @OneToMany(mappedBy = "appointment")
    private List<Treatment> treatments = new ArrayList<>();

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

//Relationship getters and setters (the setters keep the matching id field in sync)

    public Pet getPet()
    { return pet; }

    public void setPet(Pet pet)
    { this.pet = pet; if (pet != null) this.petId = pet.getPetId(); }

    public Vet getVet()
    { return vet; }

    public void setVet(Vet vet)
    { this.vet = vet; if (vet != null) this.vetId = vet.getVetId(); }

    public Room getRoom()
    { return room; }

    public void setRoom(Room room)
    { this.room = room; if (room != null) this.roomId = room.getRoomId(); }

    public Staff getCreatedByStaff()
    { return createdByStaff; }

    public void setCreatedByStaff(Staff createdByStaff)
    { this.createdByStaff = createdByStaff; if (createdByStaff != null) this.createdBy = createdByStaff.getStaffId(); }

    public List<Payment> getPayments()
    { return payments; }

    public void setPayments(List<Payment> payments)
    { this.payments = payments; }

    public List<Diagnosis> getDiagnoses()
    { return diagnoses; }

    public void setDiagnoses(List<Diagnosis> diagnoses)
    { this.diagnoses = diagnoses; }

    public List<LabTest> getTests()
    { return tests; }

    public void setTests(List<LabTest> tests)
    { this.tests = tests; }

    public List<Treatment> getTreatments()
    { return treatments; }

    public void setTreatments(List<Treatment> treatments)
    { this.treatments = treatments; }

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