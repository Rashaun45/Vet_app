package com.vet_clinic.vet_app.model;

public class Payment {

    private int paymentId;
    private int appointmentId;
    private int ownerId;
    private double amount;
    private String paymentMethod;
    private String status;
    private java.sql.Timestamp paidAt;

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