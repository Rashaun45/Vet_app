package com.vet_clinic.vet_app.dao;

import com.vet_clinic.vet_app.dao.jdbc.VetDAOJdbc;

class VetDaoJdbcTest extends VetDaoContractTest {

    @Override
    protected VetDao createDao() {
        return new VetDAOJdbc();
    }
}