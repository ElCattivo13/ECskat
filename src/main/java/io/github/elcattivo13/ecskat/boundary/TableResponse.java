import io.github.elcattivo13.pojos.Table;

public class TableResponse {
    private final String status;
    private List<Table> tables;
    
    private TableResponse(String status) {
        this.status = status;
    }
    
    public static TableResponse ok() {
        return new TableResponse("OK");
    }
    
    public static TableResponse fail(EcSkatException e) {
        return new TableResponse(e.getReason().toString());
    }
    
    public List<Table> getTables(){
        return this.tables;
    }
        
    public TableRespone setTables(List<Table> tables){
        this.tables = tables;
        return this;
    }
}