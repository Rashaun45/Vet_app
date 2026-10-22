package com.vet_clinic.vet_app.model;

public class Vet {

    private int vetId;
    private String firstName;
    private String lastName;
    private String specialty;
    private String licenseNumber;
    private String phone;
    private String email;

    public Vet() { }

    // Used when inserting a new row (the database generates the id)
    public Vet(String firstName, String lastName, String specialty, String licenseNumber, String phone, String email)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.email = email;
    }

    //Getters and Setters
    public int getVetId() 
    { return vetId; }
    
    public void setVetId(int vetId)
    { this.vetId = vetId; }
    
    public String getFirstName() 
    { return firstName; }
    
    public void setFirstName(String firstName)
    { this.firstName = firstName; }
    
    public String getLastName() 
    { return lastName; }
    
    public void setLastName(String lastName)
    { this.lastName = lastName; }
    
    public String getSpecialty()
    { return specialty; }
    
    public void setSpecialty(String specialty) 
    { this.specialty = specialty; }
    
    public String getLicenseNumber()
    { return licenseNumber; }
    
    public void setLicenseNumber(String licenseNumber)
    { this.licenseNumber = licenseNumber; }
    
    public String getPhone() 
    { return phone; }
    
    public void setPhone(String phone)
    { this.phone = phone; }
    
    public String getEmail() 
    { return email; }
    
    public void setEmail(String email) 
    { this.email = email; }

    @Override
    public String toString() {
        return "Vet{vetId=" + vetId
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", specialty=" + specialty
                + ", licenseNumber=" + licenseNumber
                + ", phone=" + phone
                + ", email=" + email + "}";
    }
}