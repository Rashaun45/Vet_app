package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.Pet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {

    // INSERT: add a new pet and return the generated ID
    public int createPet(Pet p) throws SQLException {
        String sql = "INSERT INTO pet (owner_id, name, species, breed, sex, date_of_birth, weight, microchip_id, is_active) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getOwnerId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getSpecies());
            ps.setString(4, p.getBreed());
            ps.setString(5, p.getSex());
            ps.setDate(6, p.getDateOfBirth());
            ps.setDouble(7, p.getWeight());
            ps.setString(8, p.getMicrochipId());
            ps.setBoolean(9, p.isActive());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // SELECT with a condition: all the pets that belong to one owner (multiple rows)
    public List<Pet> findByOwnerId(int ownerId) throws SQLException {
        List<Pet> result = new ArrayList<>();
        String sql = "SELECT * FROM pet WHERE owner_id = ? ORDER BY pet_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pet p = new Pet();
                    p.setPetId(rs.getInt("pet_id"));
                    p.setOwnerId(rs.getInt("owner_id"));
                    p.setName(rs.getString("name"));
                    p.setSpecies(rs.getString("species"));
                    p.setBreed(rs.getString("breed"));
                    p.setSex(rs.getString("sex"));
                    p.setDateOfBirth(rs.getDate("date_of_birth"));
                    p.setWeight(rs.getDouble("weight"));
                    p.setMicrochipId(rs.getString("microchip_id"));
                    p.setActive(rs.getBoolean("is_active"));
                    result.add(p);
                }
            }
        }
        return result;
    }

    // UPDATE: change a pet's weight (returns false if no pet was changed)
    public boolean updateWeight(int petId, double newWeight) throws SQLException {
        String sql = "UPDATE pet SET weight = ? WHERE pet_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newWeight);
            ps.setInt(2, petId);
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE: remove a pet (returns false if no pet was removed)
    public boolean deletePet(int petId) throws SQLException {
        String sql = "DELETE FROM pet WHERE pet_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, petId);
            return ps.executeUpdate() > 0;
        }
    }
}