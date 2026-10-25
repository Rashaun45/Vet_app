package com.vet_clinic.vet_app.dao.jdbc;

import com.vet_clinic.vet_app.dao.PetDao;
import com.vet_clinic.vet_app.config.DatabaseConnection;
import com.vet_clinic.vet_app.model.Pet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PetDAOJdbc implements PetDao {

    private static final Logger LOGGER = LogManager.getLogger(PetDAOJdbc.class);

    @Override
    public int createPet(Pet p) throws SQLException {
        String sql = "INSERT INTO pet (owner_id, name, species, breed, sex, date_of_birth, weight, microchip_id, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
            try (ResultSet keys = ps.getGeneratedKeys()) {
                int id = keys.next() ? keys.getInt(1) : -1;
                LOGGER.info("{} succeeded, new id = {}", "createPet", id);
                return id;
            }
        }
    }

    @Override
    public Pet findById(int petId) throws SQLException {
        String sql = "SELECT * FROM pet WHERE pet_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, petId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOGGER.debug("{} found a row for id {}", "findById", petId);
                    return mapPet(rs);
                }
                LOGGER.warn("{} found no row for id {}", "findById", petId);
                return null;
            }
        }
    }

    @Override
    public List<Pet> findByOwnerId(int ownerId) throws SQLException {
        return query("SELECT * FROM pet WHERE owner_id = ?", ownerId);
    }

    @Override
    public List<Pet> findBySpecies(String species) throws SQLException {
        return query("SELECT * FROM pet WHERE species = ?", species);
    }

    @Override
    public List<Pet> getAllPets() throws SQLException {
        return query("SELECT * FROM pet ORDER BY pet_id", null);
    }

    @Override
    public boolean updateWeight(int petId, double weight) throws SQLException {
        String sql = "UPDATE pet SET weight = ? WHERE pet_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, weight);
            ps.setInt(2, petId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "updateWeight", petId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "updateWeight", petId);
            }
            return changed;
        }
    }

    @Override
    public boolean deletePet(int petId) throws SQLException {
        String sql = "DELETE FROM pet WHERE pet_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, petId);
            boolean changed = ps.executeUpdate() > 0;
            if (changed) {
                LOGGER.info("{} succeeded for id {}", "deletePet", petId);
            } else {
                LOGGER.warn("{} changed no rows, id {} not found", "deletePet", petId);
            }
            return changed;
        }
    }

    // shared helper for the SELECT methods above (param can be null, an Integer or a String)
    private List<Pet> query(String sql, Object param) throws SQLException {
        List<Pet> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (param != null) ps.setObject(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapPet(rs));
            }
        }
        LOGGER.debug("{} returned {} row(s)", "query", list.size());
        return list;
    }

    private Pet mapPet(ResultSet rs) throws SQLException {
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
        return p;
    }
}