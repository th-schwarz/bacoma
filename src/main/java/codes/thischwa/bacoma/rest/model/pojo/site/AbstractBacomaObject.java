package codes.thischwa.bacoma.rest.model.pojo.site;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The base object of all persist-able objects.
 */
public abstract class AbstractBacomaObject<P extends AbstractBacomaObject<?>> {
	public final static UUID UNSET_VALUE = null;

	private UUID id = UNSET_VALUE;

	@JsonIgnore
	private P parent;

	private String name;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The parent object.
	 */
	public P getParent() {
		return parent;
	}

	/**
	 * Set the parent of this {@link AbstractBacomaObject}.
	 */
	public void setParent(P directParent) {
		this.parent = directParent;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result += (id == null) ? 0 : id.hashCode();
		result += (parent == null) ? 0 : parent.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj == this)
			return true;
		if(obj.getClass() != getClass())
			return false;
		AbstractBacomaObject<?> po = (AbstractBacomaObject<?>) obj;
		return getId().equals(po.getId());
	}

	@Override
	public String toString() {
		return name;
	}
}
