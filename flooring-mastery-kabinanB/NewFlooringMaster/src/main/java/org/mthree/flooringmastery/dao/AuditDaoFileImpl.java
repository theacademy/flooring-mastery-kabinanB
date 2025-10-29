package org.mthree.flooringmastery.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class AuditDaoFileImpl implements AuditDao{
    private static final String AUDIT_FILE = "audit.txt";
    @Override
    public void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException
    {
        PrintWriter out;

        try {
            //true is for append
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e){
            throw new FlooringMasteryPersistenceException("Could not persist audit information.", e);
        }

        LocalDateTime timestamp = LocalDateTime.now();
        out.println(timestamp.toString() + " : " + entry);
        out.flush();
    }
}
