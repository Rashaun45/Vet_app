package com.vet_clinic.vet_app.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int roomId;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "status")
    private String status;

    // Relationships Hibernate
    @OneToMany(mappedBy = "room")
    private List<AppointmentVisit> appointments = new ArrayList<>();

    public Room() { }

    // Used when inserting a new row (the database generates the id)
    public Room(String roomNumber, String roomType, String status)
    {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.status = status;
    }

    //Setters and Getters
    public int getRoomId()
    { return roomId; }
    
    public void setRoomId(int roomId) 
    { this.roomId = roomId; }
    
    public String getRoomNumber()
    { return roomNumber; }
    
    public void setRoomNumber(String roomNumber) 
    { this.roomNumber = roomNumber; }
    
    public String getRoomType() 
    { return roomType; }
    
    public void setRoomType(String roomType)
    { this.roomType = roomType; }
    
    public String getStatus() 
    { return status; }
    
    public void setStatus(String status)
    { this.status = status; }

    public List<AppointmentVisit> getAppointments()
    { return appointments; }

    public void setAppointments(List<AppointmentVisit> appointments)
    { this.appointments = appointments; }

    @Override
    public String toString() {
        return "Room{roomId=" + roomId
                + ", roomNumber=" + roomNumber
                + ", roomType=" + roomType
                + ", status=" + status + "}";
    }
}