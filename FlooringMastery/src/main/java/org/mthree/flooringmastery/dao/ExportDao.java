//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.dao;

import java.time.LocalDate;
import java.util.Map;
import org.mthree.flooringmastery.model.Order;

public interface ExportDao {
    void exportData(Map<LocalDate, Map<Integer, Order>> var1);
}
