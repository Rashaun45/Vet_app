package com.vet_clinic.vet_app.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private int staffId;

    @Column(name = "role")
    private String role;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    // Relationships Hibernate
    @OneToMany(mappedBy = "createdByStaff")
    private List<AppointmentVisit> createdAppointments = new ArrayList<>();

    @OneToMany(mappedBy = "performedByStaff")
    private List<LabTest> performedTests = new ArrayList<>();

    @OneToMany(mappedBy = "administeredByStaff")
    private List<Treatment> administeredTreatments = new ArrayList<>();

    public Staff() { }

    // Used when inserting a new row (the database generates the id)
    public Staff(String role, String firstName, String lastName, String phone, String email)
    {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

        //Setters and Getters
    public int getStaffId() 
    { return staffId; }
    
    public void setStaffId(int staffId)
    { this.staffId = staffId; }
    
    public String getRole() 
    { return role; }
    
    public void setRole(String role)
    { this.role = role; }
    
    public String getFirstName()
    { return firstName; }
    
    public void setFirstName(String firstName)
    { this.firstName = firstName; }
    
    public String getLastName() 
    { return lastName; }
    
    public void setLastName(String lastName)
    { this.lastName = lastName; }
    
    public String getPhone()
    { return phone; }
    
    public void setPhone(String phone)
    { this.phone = phone; }
    
    public String getEmail()
    { return email; }
    
    public void setEmail(String email)
    { this.email = email; }

    public List<AppointmentVisit> getCreatedAppointments()
    { return createdAppointments; }

    public void setCreatedAppointments(List<AppointmentVisit> createdAppointments)
    { this.createdAppointments = createdAppointments; }

    public List<LabTest> getPerformedTests()
    { return performedTests; }

    public void setPerformedTests(List<LabTest> performedTests)
    { this.performedTests = performedTests; }

    public List<Treatment> getAdministeredTreatments()
    { return administeredTreatments; }

    public void setAdministeredTreatments(List<Treatment> administeredTreatments)
    { this.administeredTreatments = administeredTreatments; }

    @Override
    public String toString() {
        return "Staff{staffId=" + staffId
                + ", role=" + role
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", phone=" + phone
                + ", email=" + email + "}";
    }
}