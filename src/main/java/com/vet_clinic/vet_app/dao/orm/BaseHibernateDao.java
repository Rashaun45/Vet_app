package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.util.HibernateUtil;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class BaseHibernateDao {
    protected Session openSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    protected <T> T inTransaction(Function<Session, T> work) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                T result = work.apply(session);
                tx.commit();
                return result;
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        }
    }
}