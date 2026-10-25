package com.vet_clinic.vet_app.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Builds ONE SessionFactory for the whole application.
 *
 * A SessionFactory is expensive to create and thread-safe, so we create it once.
 * A Session is cheap and NOT thread-safe, so we open a new one per operation.
 */
public final class HibernateUtil {

    private static final Logger LOGGER = LogManager.getLogger(HibernateUtil.class);

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
        // utility class: no instances
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Reads src/main/resources/hibernate.cfg.xml
            SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            LOGGER.info("Hibernate SessionFactory built successfully");
            return factory;
        } catch (Throwable ex) {
            // FATAL: without a SessionFactory the ORM part cannot work at all
            LOGGER.fatal("Could not build the Hibernate SessionFactory", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    /** Call once when the application exits to release the connection pool. */
    public static void shutdown() {
        if (SESSION_FACTORY != null && SESSION_FACTORY.isOpen()) {
            SESSION_FACTORY.close();
            LOGGER.info("Hibernate SessionFactory closed");
        }
    }
}