package com.vet_clinic.vet_app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "appointment_id")
    private int appointmentId;

    @Column(name = "owner_id")
    private int ownerId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "status")
    private String status;

    @Column(name = "paid_at")
    private java.sql.Timestamp paidAt;

    // Relationships Hibernate
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentVisit appointment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    public Payment() { }

    // Used when inserting a new row (the database generates the id)
    public Payment(int appointmentId, int ownerId, double amount, String paymentMethod, String status)
    {
        this.appointmentId = appointmentId;
        this.ownerId = ownerId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public int getPaymentId() 
    { return paymentId; }
    
    public void setPaymentId(int paymentId) 
    { this.paymentId = paymentId; }
    
    public int getAppointmentId() 
    { return appointmentId; }
    
    public void setAppointmentId(int appointmentId) 
    { this.appointmentId = appointmentId; }
    
    public int getOwnerId()
    { return ownerId; }
    
    public void setOwnerId(int ownerId)
    { this.ownerId = ownerId; }
    
    public double getAmount()
    { return amount; }
    
    public void setAmount(double amount)
    { this.amount = amount; }
    
    public String getPaymentMethod()
    { return paymentMethod; }
    
    public void setPaymentMethod(String paymentMethod) 
    { this.paymentMethod = paymentMethod; }
    
    public String getStatus()
    { return status; }
    public void setStatus(String status)
    { this.status = status; }
    
    public java.sql.Timestamp getPaidAt()
    { return paidAt; }
    
    public void setPaidAt(java.sql.Timestamp paidAt) 
    { this.paidAt = paidAt; }

//Relationship getters and setters (the setters keep the matching id field in sync)

    public AppointmentVisit getAppointment()
    { return appointment; }

    public void setAppointment(AppointmentVisit appointment)
    { this.appointment = appointment; if (appointment != null) this.appointmentId = appointment.getAppointmentId(); }

    public Owner getOwner()
    { return owner; }

    public void setOwner(Owner owner)
    { this.owner = owner; if (owner != null) this.ownerId = owner.getOwnerId(); }

    @Override
    public String toString() {
        return "Payment{paymentId=" + paymentId
                + ", appointmentId=" + appointmentId
                + ", ownerId=" + ownerId
                + ", amount=" + amount
                + ", paymentMethod=" + paymentMethod
                + ", status=" + status
                + ", paidAt=" + paidAt + "}";
    }
}