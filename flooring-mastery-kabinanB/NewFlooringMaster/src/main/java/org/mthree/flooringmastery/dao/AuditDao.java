package org.mthree.flooringmastery.dao;

public interface AuditDao {
    public void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException;
}
