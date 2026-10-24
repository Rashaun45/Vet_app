package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.dao.StaffDao;
import com.vet_clinic.vet_app.model.Staff;
import java.util.List;
import org.hibernate.Session;

public class StaffDaoHibernate extends BaseHibernateDao implements StaffDao {
    @Override
    public int createStaff(Staff s) {
        return inTransaction(session -> {
            session.persist(s);
            return s.getStaffId();
        });
    }

    @Override
    public Staff findById(int staffId) {
        try (Session session = openSession()) {
            return session.get(Staff.class, staffId);
        }
    }

    @Override
    public List<Staff> findByRole(String role) {
        try (Session session = openSession()) {
            return session.createQuery("from Staff s where s.role = :role", Staff.class)
                    .setParameter("role", role)
                    .list();
        }
    }

    @Override
    public List<Staff> getAllStaff() {
        try (Session session = openSession()) {
            return session.createQuery("from Staff s order by s.staffId", Staff.class).list();
        }
    }

    @Override
    public boolean updateRole(int staffId, String role) {
        return inTransaction(session -> {
            Staff s = session.get(Staff.class, staffId);
            if (s == null) {
                return false;
            }
            s.setRole(role);
            return true;
        });
    }

    @Override
    public boolean deleteStaff(int staffId) {
        return inTransaction(session -> {
            Staff s = session.get(Staff.class, staffId);
            if (s == null) {
                return false;
            }
            session.remove(s);
            return true;
        });
    }
}