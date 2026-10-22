package com.vet_clinic.vet_app.model;

import java.sql.Timestamp;

/** Maps one row of the owner table. */
public class Owner {
    private int ownerId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private Timestamp createdAt;

    public Owner() {
    }

    public Owner(int ownerId, String firstName, String lastName, String phone, String email, String address, Timestamp createdAt) 
    {
        this.ownerId = ownerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.createdAt = createdAt;
    }
    
//Setters and Getters
    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
//String concatonation
    @Override
    public String toString() {
        return "Owner{" +
                "ownerId=" + ownerId + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "phone=" + phone + ", " +
                "email=" + email + ", " +
                "address=" + address + ", " +
                "createdAt=" + createdAt + "}";
    }
}