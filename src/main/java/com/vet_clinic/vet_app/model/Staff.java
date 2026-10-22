package com.vet_clinic.vet_app.model;

public class Staff {

    private int staffId;
    private String role;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

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