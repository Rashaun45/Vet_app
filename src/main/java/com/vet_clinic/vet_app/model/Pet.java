package com.vet_clinic.vet_app.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/** Maps one row of the pet table. */
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private int petId;

   
    @Column(name = "owner_id")
    private int ownerId;

    @Column(name = "name")
    private String name;

    @Column(name = "species")
    private String species;

    @Column(name = "breed")
    private String breed;

    @Column(name = "sex")
    private String sex;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "weight")
    private double weight;

    @Column(name = "microchip_id")
    private String microchipId;

    @Column(name = "is_active")
    private boolean active;

    // Relationships Hibernate
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @OneToMany(mappedBy = "pet")
    private List<AppointmentVisit> appointments = new ArrayList<>();

    public Pet() {
    }

    // Used when inserting a new pet (the database sets pet_id, and a new pet is active)
    public Pet(int ownerId, String name, String species, String breed, String sex, Date dateOfBirth, double weight, String microchipId)
    {
        this.ownerId = ownerId;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.weight = weight;
        this.microchipId = microchipId;
        this.active = true;
    }

    public Pet(int petId, int ownerId, String name, String species, String breed, String sex, Date dateOfBirth, double weight, String microchipId, boolean active)
    {
        this.petId = petId;
        this.ownerId = ownerId;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.weight = weight;
        this.microchipId = microchipId;
        this.active = active;
    }

    //Getters and Setters
    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getMicrochipId() {
        return microchipId;
    }

    public void setMicrochipId(String microchipId) {
        this.microchipId = microchipId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    //Relationship getters and setters (setOwner keeps ownerId in sync)
    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
        if (owner != null) {
            this.ownerId = owner.getOwnerId();
        }
    }

    public List<AppointmentVisit> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentVisit> appointments) {
        this.appointments = appointments;
    }

    //String Concatonation
    @Override
    public String toString() {
        return "Pet{" +
                "petId=" + petId + ", " +
                "ownerId=" + ownerId + ", " +
                "name=" + name + ", " +
                "species=" + species + ", " +
                "breed=" + breed + ", " +
                "sex=" + sex + ", " +
                "dateOfBirth=" + dateOfBirth + ", " +
                "weight=" + weight + ", " +
                "microchipId=" + microchipId + ", " +
                "active=" + active + "}";
    }
}