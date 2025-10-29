package org.mthree.flooringmastery.dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuditDaoStubImplTest {

    @Test
    void writeAuditEntry_appends() {
        AuditDaoStubImpl dao = new AuditDaoStubImpl();
        dao.writeAuditEntry("ADD 1");
        dao.writeAuditEntry("EDIT 1");
        assertEquals(2, dao.getEntries().size());
        assertTrue(dao.getEntries().get(0).contains("ADD"));
    }
}

