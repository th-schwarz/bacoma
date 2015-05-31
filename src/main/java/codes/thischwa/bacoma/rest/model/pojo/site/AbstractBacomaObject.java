package codes.thischwa.bacoma.rest.model.pojo.site;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The base object of all persist-able objects.
 */
public abstract class AbstractBacomaObject<P> {
	public final static UUID UNSET_VALUE = null;

	private UUID id = UNSET_VALUE;

	@JsonIgnore
	private P parent;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
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
	protected void setParent(P directParent) {
		this.parent = directParent;
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result += (id != null) ? 0 : id.hashCode();
		result += ((parent == null) ? 0 : parent.hashCode());
		result += id.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        AbstractBacomaObject<?> po = (AbstractBacomaObject<?>)obj;
        return getId() == po.getId();
	}
	
	@Override
	public abstract String toString();
}
