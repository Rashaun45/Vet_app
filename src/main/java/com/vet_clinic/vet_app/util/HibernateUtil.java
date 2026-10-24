package com.vet_clinic.vet_app.util;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/**
* Builds ONE SessionFactory for the whole application.
*
* A SessionFactory is expensive to create and thread-safe, so we create it once.
* A Session is cheap and NOT thread-safe, so we open a new one per operation.
*/
public final class HibernateUtil {
 private static final SessionFactory SESSION_FACTORY = buildSessionFactory();
 private HibernateUtil() {
 // utility class: no instances
 }
 private static SessionFactory buildSessionFactory() {
 try {
 // Reads src/main/resources/hibernate.cfg.xml
 return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
 } catch (Throwable ex) {
    ex.printStackTrace();   // shows the whole "Caused by: ..." chain
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
 }
 }
}
