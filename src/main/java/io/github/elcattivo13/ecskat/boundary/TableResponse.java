package io.github.elcattivo13.ecskat.boundary;

import java.util.List;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason;
import io.github.elcattivo13.ecskat.pojos.Table;

public class TableResponse extends BaseResponse {
    
    private List<Table> tables;
    
    private TableResponse(boolean success, Reason reason) {
        super(success, reason);
    }
    
    public static TableResponse ok() {
        return new TableResponse(true, null);
    }
    
    public static TableResponse fail(EcSkatException e) {
        return new TableResponse(false, e.getReason());
    }
    
    public List<Table> getTables(){
        return this.tables;
    }
    
    public TableResponse setTables(List<Table> tables){
        this.tables = tables;
        return this;
    }
}