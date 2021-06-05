package io.github.elcattivo13.ecskat.boundary;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason;

public class BaseResponse {
    
    private final boolean success;
    private final Reason reason;
    
    protected BaseResponse(boolean success, Reason reason) {
        this.success = success;
        this.reason = reason;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public Reason getReason() {
        return reason;
    }
}