package io.github.elcattivo13.ecskat.beans;

import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.UNKNOWN_TABLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.pojos.Table;

@ApplicationScoped
public class TableCache {
    private Map<String, Table> tables = new HashMap<>();
    
    public void addTable(Table table) {
        this.tables.put(table.getId(), table);
    }
    
    public void removeTable(String tableId) {
        this.tables.remove(tableId);
    }

    public Table findTable(String tableId) throws EcSkatException {
        if (tableId == null || this.tables.get(tableId) == null) {
            throw new EcSkatException(UNKNOWN_TABLE);
        }
        return this.tables.get(tableId);
    }

    public List<Table> findAllTables() {
        return new ArrayList<>(tables.values());
    }
}
