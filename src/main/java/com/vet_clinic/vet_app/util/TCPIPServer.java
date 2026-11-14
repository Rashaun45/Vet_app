package com.vet_clinic.vet_app.util;

import com.vet_clinic.vet_app.dao.AppointmentDao;
import com.vet_clinic.vet_app.dao.LabTestDao;
import com.vet_clinic.vet_app.dao.OwnerDao;
import com.vet_clinic.vet_app.dao.PaymentDao;
import com.vet_clinic.vet_app.dao.jdbc.OwnerDAOJdbc;
import com.vet_clinic.vet_app.dao.jdbc.PaymentDAOJdbc;
import com.vet_clinic.vet_app.dao.orm.AppointmentDaoHibernate;
import com.vet_clinic.vet_app.dao.orm.LabTestDaoHibernate;
import com.vet_clinic.vet_app.model.AppointmentVisit;
import com.vet_clinic.vet_app.model.LabTest;
import com.vet_clinic.vet_app.model.Owner;
import com.vet_clinic.vet_app.model.Payment;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

public class TCPIPServer {

    // Objective 3: configure the network port
    private static final int SERVER_PORT = 5000;

    // Objective 4: configure the network host
    private static final String BIND_HOST = "127.0.0.1";

    // ORM (Hibernate)
    private static final AppointmentDao APPOINTMENT_DAO = new AppointmentDaoHibernate();
    private static final LabTestDao LAB_TEST_DAO = new LabTestDaoHibernate();

    // JDBC
    private static final PaymentDao PAYMENT_DAO = new PaymentDAOJdbc();
    private static final OwnerDao OWNER_DAO = new OwnerDAOJdbc();

    public static void main(String[] args) {
        System.out.println("=== Vet Clinic Server ===");

        InetAddress bindAddress;
        try {
            bindAddress = InetAddress.getByName(BIND_HOST);
        } catch (UnknownHostException e) {
            System.err.println("Invalid bind host: " + BIND_HOST);
            e.printStackTrace();
            return;
        }

        HibernateUtil.getSessionFactory();
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));

        // Objective 2: TCP/IP server with ServerSocket
        // Objective 6: exception handling with try-with-resources
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT, 50, bindAddress)) {

            System.out.println("Server listening on " + bindAddress.getHostAddress() + ":" + SERVER_PORT);

            while (true) {
                System.out.println("Waiting for a client connection...");

                try (Socket clientSocket = serverSocket.accept();
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

                    System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress());

                    // Objective 5: object streams
                    Object received = in.readObject();
                    handle(received);

                } catch (ClassNotFoundException e) {
                    System.err.println("ClassNotFoundException - client sent an unknown class");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.err.println("Database error while processing the object: " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("I/O error while handling client: " + e.getMessage());
                } catch (RuntimeException e) {
                    System.err.println("Error while processing the object: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Could not start server on port " + SERVER_PORT);
            e.printStackTrace();
        }
    }

    private static void handle(Object received) throws SQLException {
        if (received instanceof AppointmentVisit appointment) {
            processAppointment(appointment);
        } else if (received instanceof Payment payment) {
            processPayment(payment);
        } else if (received instanceof LabTest test) {
            processLabTest(test);
        } else if (received instanceof Owner owner) {
            processOwner(owner);
        } else {
            String type = received == null ? "null" : received.getClass().getName();
            System.err.println("Received unexpected object type: " + type);
        }
    }

    private static void processAppointment(AppointmentVisit a) throws SQLException {
        System.out.println("Received: " + a);

        if (a.getPetId() <= 0 || a.getVetId() <= 0 || a.getRoomId() <= 0 || a.getCreatedBy() <= 0) {
            System.out.println("  -> REJECTED: pet, vet, room and staff ids are required.");
            return;
        }
        if (a.getScheduledAt() == null || a.getScheduledAt().before(new Timestamp(System.currentTimeMillis()))) {
            System.out.println("  -> REJECTED: the appointment must be scheduled in the future.");
            return;
        }
        if (isBlank(a.getStatus())) {
            a.setStatus("Scheduled");
        }

        int hour = a.getScheduledAt().toLocalDateTime().getHour();
        if (hour < 8 || hour >= 18) {
            System.out.println("  -> ALERT: outside normal clinic hours (08:00-18:00).");
        }

        int id = APPOINTMENT_DAO.createAppointment(a);
        System.out.println("  -> Appointment booked with id " + id + " (saved with Hibernate)");
    }

    private static void processPayment(Payment p) throws SQLException {
        System.out.println("Received: " + p);

        if (p.getAmount() <= 0) {
            System.out.println("  -> REJECTED: the amount must be greater than zero.");
            return;
        }
        if (isBlank(p.getPaymentMethod())) {
            System.out.println("  -> REJECTED: a payment method is required.");
            return;
        }
        if (p.getAmount() > 5000) {
            System.out.println("  -> ALERT: unusually large payment, please review.");
        }

        int id = PAYMENT_DAO.createPayment(p);
        if ("Paid".equalsIgnoreCase(p.getStatus())) {
            PAYMENT_DAO.markPaid(id);
            System.out.println("  -> Payment " + id + " recorded and marked as paid (JDBC).");
            System.out.println("  -> Owner " + p.getOwnerId() + " has now paid a total of "
                    + PAYMENT_DAO.getTotalPaidByOwner(p.getOwnerId()));
        } else {
            System.out.println("  -> Payment " + id + " recorded with status " + p.getStatus() + " (JDBC).");
        }
    }

    private static void processLabTest(LabTest t) throws SQLException {
        System.out.println("Received: " + t);

        if (t.getTestId() == 0) {
            if (isBlank(t.getTestType())) {
                System.out.println("  -> REJECTED: a test type is required.");
                return;
            }
            int id = LAB_TEST_DAO.createTest(t);
            System.out.println("  -> New test ordered with id " + id + " (saved with Hibernate)");
        } else if (isBlank(t.getResult())) {
            System.out.println("  -> REJECTED: no result supplied for test " + t.getTestId());
        } else if (LAB_TEST_DAO.recordResult(t.getTestId(), t.getResult())) {
            System.out.println("  -> Result recorded for test " + t.getTestId() + " and marked complete (Hibernate).");
        } else {
            System.out.println("  -> REJECTED: test " + t.getTestId() + " does not exist.");
        }
    }

    private static void processOwner(Owner o) throws SQLException {
        System.out.println("Received: " + o);

        if (isBlank(o.getFirstName()) || isBlank(o.getLastName())) {
            System.out.println("  -> REJECTED: first and last name are required.");
            return;
        }
        if (!isBlank(o.getEmail()) && !o.getEmail().contains("@")) {
            System.out.println("  -> REJECTED: the email address is not valid.");
            return;
        }

        boolean duplicate = OWNER_DAO.findByLastName(o.getLastName()).stream()
                .anyMatch(x -> x.getFirstName().equalsIgnoreCase(o.getFirstName())
                        && Objects.equals(x.getPhone(), o.getPhone()));
        if (duplicate) {
            System.out.println("  -> REJECTED: an owner with the same name and phone already exists.");
            return;
        }

        int id = OWNER_DAO.createOwner(o);
        System.out.println("  -> Owner registered with id " + id + " (JDBC)");
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}