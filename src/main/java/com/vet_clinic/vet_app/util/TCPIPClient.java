package com.vet_clinic.vet_app.util;

import com.vet_clinic.vet_app.model.AppointmentVisit;
import com.vet_clinic.vet_app.model.Owner;
import com.vet_clinic.vet_app.model.Payment;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TCPIPClient {

    // Objective 3: configure the network port
    private static final int SERVER_PORT = 5000;

    // Objective 4: configure the network host
    private static final String SERVER_HOST = "127.0.0.1";

    public static void send(Serializable payload) {
        InetAddress serverAddress;
        try {
            serverAddress = InetAddress.getByName(SERVER_HOST);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + SERVER_HOST);
            e.printStackTrace();
            return;
        }

        // Objective 1: TCP/IP client with Socket
        // Objective 5: object streams
        // Objective 6: try-with-resources and exception handling
        try (Socket socket = new Socket(serverAddress, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            System.out.println("Connected to server at " + serverAddress.getHostAddress() + ":" + SERVER_PORT);
            System.out.println("Sending: " + payload);

            out.writeObject(payload);
            out.flush();

            System.out.println("Sent successfully.");

        } catch (IOException e) {
            System.err.println("Network or I/O error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Vet Clinic Client ===");

        Timestamp tomorrowAtTen = Timestamp.valueOf(
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0));

        send(new Owner("Jane", "Doe", "555-0101", "jane.doe@example.com", "12 Main St"));
        send(new AppointmentVisit(1, 1, 1, 1, tomorrowAtTen, "Scheduled", "Annual checkup"));
        send(new Payment(1, 1, 75.50, "Card", "Paid"));
    }
}