
package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.model.Pet;
import java.sql.SQLException;
import java.util.List;


public interface PetDao {

    int createPet(Pet p) throws SQLException;

    boolean deletePet(int petId) throws SQLException;

    Pet findById(int petId) throws SQLException;

    List<Pet> findByOwnerId(int ownerId) throws SQLException;

    List<Pet> findBySpecies(String species) throws SQLException;

    List<Pet> getAllPets() throws SQLException;

    boolean updateWeight(int petId, double weight) throws SQLException;
    
}
