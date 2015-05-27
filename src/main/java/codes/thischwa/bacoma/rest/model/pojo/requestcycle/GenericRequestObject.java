package codes.thischwa.bacoma.rest.model.pojo.requestcycle;

import java.util.UUID;

public abstract class GenericRequestObject {
	
	private UUID id;
	
	private UUID parent;

	private String name;
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getParent() {
		return parent;
	}

	public void setParent(UUID parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUpdateRequest() {
		return id != null;
	}
}
