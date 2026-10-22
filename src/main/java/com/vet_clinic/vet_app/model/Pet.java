package com.vet_clinic.vet_app.model;

import java.sql.Date;

/** Maps one row of the pet table. */
public class Pet {
    private int petId;
    private int ownerId;
    private String name;
    private String species;
    private String breed;
    private String sex;
    private Date dateOfBirth;
    private double weight;
    private String microchipId;
    private boolean active;

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