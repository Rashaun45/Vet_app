package com.vet_clinic.vet_app.dao.orm;

import com.vet_clinic.vet_app.dao.OwnerDao;
import com.vet_clinic.vet_app.model.Owner;
import java.util.List;
import org.hibernate.Session;

public class OwnerDaoHibernate extends BaseHibernateDao implements OwnerDao {
    @Override
    public int createOwner(Owner o) {
        return inTransaction(session -> {
            session.persist(o);
            return o.getOwnerId();
        });
    }

    @Override
    public Owner findById(int ownerId) {
        try (Session session = openSession()) {
            return session.get(Owner.class, ownerId);
        }
    }

    @Override
    public List<Owner> findByLastName(String lastName) {
        try (Session session = openSession()) {
            return session.createQuery("from Owner o where o.lastName = :lastName", Owner.class)
                    .setParameter("lastName", lastName)
                    .list();
        }
    }

    @Override
    public List<Owner> getAllOwners() {
        try (Session session = openSession()) {
            return session.createQuery("from Owner o order by o.ownerId", Owner.class).list();
        }
    }

    @Override
    public boolean updatePhone(int ownerId, String phone) {
        return inTransaction(session -> {
            Owner o = session.get(Owner.class, ownerId);
            if (o == null) {
                return false;
            }
            o.setPhone(phone);
            return true;
        });
    }

    @Override
    public boolean deleteOwner(int ownerId) {
        return inTransaction(session -> {
            Owner o = session.get(Owner.class, ownerId);
            if (o == null) {
                return false;
            }
            session.remove(o);
            return true;
        });
    }

    @Override
    public void showOwnersScrollable() {
        List<Owner> owners = getAllOwners();
        System.out.println("Total rows: " + owners.size());
        if (!owners.isEmpty()) {
            Owner last = owners.get(owners.size() - 1);
            Owner first = owners.get(0);
            System.out.println("Last owner : " + last.getFirstName() + " " + last.getLastName());
            System.out.println("First owner: " + first.getFirstName() + " " + first.getLastName());
        }
        System.out.println("All rows, top to bottom:");
        for (Owner o : owners) {
            System.out.println("  " + o.getOwnerId() + " | " + o.getFirstName()
                    + " " + o.getLastName() + " | " + o.getPhone());
        }
    }
}