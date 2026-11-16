package com.vet_clinic.vet_app.util;

import com.vet_clinic.vet_app.model.AppointmentVisit;
import com.vet_clinic.vet_app.model.Owner;
import com.vet_clinic.vet_app.model.Payment;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TCPIPClient {

    // Must stay the FIRST thing in the class, before the LOGGER line.
    // The client and the server are two separate programs, so each one writes to its own
    // log files (logs/client.log) instead of both fighting over logs/vet_app.log.
    static {
        System.setProperty("log.name", "client");
    }

    private static final Logger LOGGER = LogManager.getLogger(TCPIPClient.class);

    // Objective 3: configure the network port
    private static final int SERVER_PORT = 5000;

    // Objective 4: configure the network host
    private static final String SERVER_HOST = "127.0.0.1";

    public static void send(Serializable payload) {
        if (payload == null) {
            LOGGER.warn("Nothing to send: the object was null");
            return;
        }
        String type = payload.getClass().getSimpleName();

        InetAddress serverAddress;
        try {
            serverAddress = InetAddress.getByName(SERVER_HOST);
        } catch (UnknownHostException e) {
            LOGGER.error("Unknown host {} - the {} was not sent", SERVER_HOST, type, e);
            return;
        }

        // Objective 1: TCP/IP client with Socket
        // Objective 5: object streams
        // Objective 6: try-with-resources and exception handling
        try (Socket socket = new Socket(serverAddress, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            LOGGER.info("Connected to server at {}:{}", serverAddress.getHostAddress(), SERVER_PORT);
            LOGGER.info("Sending {}: {}", type, payload);

            out.writeObject(payload);
            out.flush();

            LOGGER.info("{} sent successfully", type);

        } catch (ConnectException e) {
            LOGGER.error("Could not connect to the server at {}:{} - is it running? The {} was not sent",
                    SERVER_HOST, SERVER_PORT, type, e);
        } catch (NotSerializableException e) {
            LOGGER.error("The {} cannot be sent because {} is not Serializable", type, e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error("Network or I/O error while sending the {}", type, e);
        }
    }

    public static void main(String[] args) {
        LOGGER.info("=== Vet Clinic Client started ===");

        Timestamp tomorrowAtTen = Timestamp.valueOf(
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0));

        send(new Owner("Jane", "Doe", "555-0101", "jane.doe@example.com", "12 Main St"));
        send(new AppointmentVisit(1, 1, 1, 1, tomorrowAtTen, "Scheduled", "Annual checkup"));
        send(new Payment(1, 1, 75.50, "Card", "Paid"));

        LOGGER.info("=== Vet Clinic Client finished ===");
    }
}