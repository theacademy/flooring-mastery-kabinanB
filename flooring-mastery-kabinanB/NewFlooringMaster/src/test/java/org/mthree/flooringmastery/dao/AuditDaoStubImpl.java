package org.mthree.flooringmastery.dao;

import java.util.ArrayList;
import java.util.List;

public class AuditDaoStubImpl implements AuditDao {
    public final List<String> entries = new ArrayList<>();

    @Override
    public void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException {
        entries.add(entry);
    }

    public List<String> getEntries()
    {
        return entries;
    }
}
