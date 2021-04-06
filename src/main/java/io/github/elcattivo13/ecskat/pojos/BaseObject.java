package io.github.elcattivo13.ecskat.pojos;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class BaseObject implements Serializable {
    
	private static final long serialVersionUID = 1L;
    private final String id;
    
    public BaseObject() {
        this.id = UUID.randomUUID().toString();
    }
    
    public String getId() {
        return this.id;
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