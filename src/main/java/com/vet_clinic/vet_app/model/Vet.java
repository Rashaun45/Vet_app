package com.vet_clinic.vet_app.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vet")
public class Vet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vet_id")
    private int vetId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    // Relationships Hibernate
    @OneToMany(mappedBy = "vet")
    private List<AppointmentVisit> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "vet")
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @OneToMany(mappedBy = "orderedByVet")
    private List<LabTest> orderedTests = new ArrayList<>();

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

    public List<AppointmentVisit> getAppointments()
    { return appointments; }

    public void setAppointments(List<AppointmentVisit> appointments)
    { this.appointments = appointments; }

    public List<Diagnosis> getDiagnoses()
    { return diagnoses; }

    public void setDiagnoses(List<Diagnosis> diagnoses)
    { this.diagnoses = diagnoses; }

    public List<LabTest> getOrderedTests()
    { return orderedTests; }

    public void setOrderedTests(List<LabTest> orderedTests)
    { this.orderedTests = orderedTests; }

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