import io.github.elcattivo13.pojos.Table;

@Singleton
public class TableCache {
    private Map<String, Table> tables = new HashMap<>();
    
    public void addTable(Table table) {
        this.tables.put(table.getId(), table);
    }
    
    public void removeTable(String tableId) {
        this.tables.remove(tableId);
    }
    
    public Optional<Table> getTable(String id) {
        Table table = this.tables.get(id);
        return table == null ? Optional.empty() : Optional.of(table);
    }
    
    public List<Table> findAllTables() {
        return new ArrayList<>(tables.values());
    }
}
