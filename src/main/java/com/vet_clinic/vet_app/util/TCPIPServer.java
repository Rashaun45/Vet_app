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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TCPIPServer {

    // Must stay the FIRST thing in the class, before the LOGGER line and the DAO fields below.
    // The client and the server are two separate programs, so each one writes to its own
    // log files (logs/server.log) instead of both fighting over logs/vet_app.log.
    static {
        System.setProperty("log.name", "server");
    }

    private static final Logger LOGGER = LogManager.getLogger(TCPIPServer.class);

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
        LOGGER.info("=== Vet Clinic Server starting ===");

        InetAddress bindAddress;
        try {
            bindAddress = InetAddress.getByName(BIND_HOST);
        } catch (UnknownHostException e) {
            LOGGER.fatal("Invalid bind host {} - the server cannot start", BIND_HOST, e);
            return;
        }

        try {
            HibernateUtil.getSessionFactory();
        } catch (ExceptionInInitializerError e) {
            LOGGER.fatal("Hibernate could not start - the server cannot continue", e);
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));

        // Objective 2: TCP/IP server with ServerSocket
        // Objective 6: exception handling with try-with-resources
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT, 50, bindAddress)) {

            LOGGER.info("Server listening on {}:{}", bindAddress.getHostAddress(), SERVER_PORT);

            while (true) {
                LOGGER.debug("Waiting for a client connection...");

                try (Socket clientSocket = serverSocket.accept();
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

                    LOGGER.info("Client connected from {}", clientSocket.getRemoteSocketAddress());

                    // Objective 5: object streams
                    Object received = in.readObject();
                    handle(received);

                    LOGGER.info("Finished with client {}", clientSocket.getRemoteSocketAddress());

                } catch (ClassNotFoundException e) {
                    LOGGER.error("The client sent an object of a class the server does not know", e);
                } catch (SQLException e) {
                    ErrorHandler.handle("Processing the received object", e);
                } catch (IOException e) {
                    LOGGER.error("I/O error while handling a client", e);
                } catch (RuntimeException e) {
                    LOGGER.error("Unexpected error while processing the received object", e);
                }
            }

        } catch (IOException e) {
            LOGGER.fatal("Could not start the server on port {}", SERVER_PORT, e);
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
            LOGGER.warn("Received an object of an unexpected type: {}", type);
        }
    }

    private static void processAppointment(AppointmentVisit a) throws SQLException {
        LOGGER.info("Received: {}", a);

        if (a.getPetId() <= 0 || a.getVetId() <= 0 || a.getRoomId() <= 0 || a.getCreatedBy() <= 0) {
            LOGGER.warn("REJECTED: pet, vet, room and staff ids are required.");
            return;
        }
        if (a.getScheduledAt() == null || a.getScheduledAt().before(new Timestamp(System.currentTimeMillis()))) {
            LOGGER.warn("REJECTED: the appointment must be scheduled in the future.");
            return;
        }
        if (isBlank(a.getStatus())) {
            a.setStatus("Scheduled");
        }

        int hour = a.getScheduledAt().toLocalDateTime().getHour();
        if (hour < 8 || hour >= 18) {
            LOGGER.warn("ALERT: outside normal clinic hours (08:00-18:00).");
        }

        int id = APPOINTMENT_DAO.createAppointment(a);
        LOGGER.info("Appointment booked with id {} (saved with Hibernate)", id);
    }

    private static void processPayment(Payment p) throws SQLException {
        LOGGER.info("Received: {}", p);

        if (p.getAmount() <= 0) {
            LOGGER.warn("REJECTED: the amount must be greater than zero.");
            return;
        }
        if (isBlank(p.getPaymentMethod())) {
            LOGGER.warn("REJECTED: a payment method is required.");
            return;
        }
        if (p.getAmount() > 5000) {
            LOGGER.warn("ALERT: unusually large payment, please review.");
        }

        int id = PAYMENT_DAO.createPayment(p);
        if ("Paid".equalsIgnoreCase(p.getStatus())) {
            PAYMENT_DAO.markPaid(id);
            LOGGER.info("Payment {} recorded and marked as paid (JDBC).", id);
            LOGGER.info("Owner {} has now paid a total of {}",
                    p.getOwnerId(), PAYMENT_DAO.getTotalPaidByOwner(p.getOwnerId()));
        } else {
            LOGGER.info("Payment {} recorded with status {} (JDBC).", id, p.getStatus());
        }
    }

    private static void processLabTest(LabTest t) throws SQLException {
        LOGGER.info("Received: {}", t);

        if (t.getTestId() == 0) {
            if (isBlank(t.getTestType())) {
                LOGGER.warn("REJECTED: a test type is required.");
                return;
            }
            int id = LAB_TEST_DAO.createTest(t);
            LOGGER.info("New test ordered with id {} (saved with Hibernate)", id);
        } else if (isBlank(t.getResult())) {
            LOGGER.warn("REJECTED: no result supplied for test {}", t.getTestId());
        } else if (LAB_TEST_DAO.recordResult(t.getTestId(), t.getResult())) {
            LOGGER.info("Result recorded for test {} and marked complete (Hibernate).", t.getTestId());
        } else {
            LOGGER.warn("REJECTED: test {} does not exist.", t.getTestId());
        }
    }

    private static void processOwner(Owner o) throws SQLException {
        LOGGER.info("Received: {}", o);

        if (isBlank(o.getFirstName()) || isBlank(o.getLastName())) {
            LOGGER.warn("REJECTED: first and last name are required.");
            return;
        }
        if (!isBlank(o.getEmail()) && !o.getEmail().contains("@")) {
            LOGGER.warn("REJECTED: the email address is not valid.");
            return;
        }

        boolean duplicate = OWNER_DAO.findByLastName(o.getLastName()).stream()
                .anyMatch(x -> x.getFirstName().equalsIgnoreCase(o.getFirstName())
                        && Objects.equals(x.getPhone(), o.getPhone()));
        if (duplicate) {
            LOGGER.warn("REJECTED: an owner with the same name and phone already exists.");
            return;
        }

        int id = OWNER_DAO.createOwner(o);
        LOGGER.info("Owner registered with id {} (JDBC)", id);
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}