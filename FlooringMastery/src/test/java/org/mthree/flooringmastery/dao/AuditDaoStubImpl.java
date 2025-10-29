package org.mthree.flooringmastery.dao;

import java.util.ArrayList;
import java.util.List;

public class AuditDaoStubImpl implements AuditDao {
    public List<String> entries = new ArrayList<>();

    @Override
    public void writeAuditEntry(String entry) {
        entries.add(entry);
    }
}
