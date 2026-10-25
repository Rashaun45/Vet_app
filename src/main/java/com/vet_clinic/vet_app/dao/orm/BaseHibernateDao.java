package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.util.HibernateUtil;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class BaseHibernateDao {

    private static final Logger LOGGER = LogManager.getLogger(BaseHibernateDao.class);

    protected Session openSession() {
        LOGGER.debug("Opening a Hibernate session");
        return HibernateUtil.getSessionFactory().openSession();
    }

    protected <T> T inTransaction(Function<Session, T> work) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            LOGGER.debug("Transaction started");
            try {
                T result = work.apply(session);
                tx.commit();
                LOGGER.info("Transaction committed");
                return result;
            } catch (RuntimeException e) {
                tx.rollback();
                LOGGER.error("Transaction rolled back", e);
                throw e;
            }
        }
    }
}