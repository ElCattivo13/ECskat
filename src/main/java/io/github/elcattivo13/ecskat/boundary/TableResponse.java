package io.github.elcattivo13.ecskat.boundary;

import java.util.List;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.pojos.Table;

public class TableResponse {
    private final String status;
    private List<Table> tables;
    private String tableId;
    
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
        
    public TableResponse setTables(List<Table> tables){
        this.tables = tables;
        return this;
    }

	public String getStatus() {
		return status;
	}

	public String getTableId() {
		return tableId;
	}

	public TableResponse setTableId(String tableId) {
		this.tableId = tableId;
		return this;
	}
	
	
}