package com.vet_clinic.vet_app.model;

public class Room {

    private int roomId;
    private String roomNumber;
    private String roomType;
    private String status;

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

    @Override
    public String toString() {
        return "Room{roomId=" + roomId
                + ", roomNumber=" + roomNumber
                + ", roomType=" + roomType
                + ", status=" + status + "}";
    }
}