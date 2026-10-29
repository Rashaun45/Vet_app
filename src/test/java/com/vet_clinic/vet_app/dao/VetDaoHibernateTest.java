package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.dao.orm.VetDaoHibernate;

class VetDaoHibernateTest extends VetDaoContractTest {

    @Override
    protected VetDao createDao() {
        return new VetDaoHibernate();
    }
}