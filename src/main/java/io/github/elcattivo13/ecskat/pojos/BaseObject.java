package io.github.elcattivo13.ecskat.pojos;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class BaseObject implements Serializable {
    
	private static final long serialVersionUID = 1L;
    private final String id;
    private SkatWebsocket websocket;
    
    public BaseObject() {
        this.id = UUID.randomUUID().toString();
    }
    
    public String getId() {
        return this.id;
    }
    
    @JsonIgnore
    public SkatWebsocket getWebsocket() throws EcSkatException {
        if (this.websocket == null) {
            throw new EcSkatException(NO_WEBSOCKET)
        }
        return this.websocket;
    }
    
    @JsonIgnore
    public void setWebsocket(SkatWebsocket websocket){
        this.websocket = websocket;
    }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseObject other = (BaseObject) obj;
		return Objects.equals(id, other.id);
	}
    
    
}