package codes.thischwa.bacoma.model.pojo.site;

/**
 * Base object - content of a {@link Page}.
 * Content#name should be unique in a collection of content.
 */
public class Content extends AbstractBacomaObject<Page> {
	private String name;
	private String value;
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
		
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result += ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof Content))
			return false;
		Content other = (Content) obj;
		if(name == null) {
			if(other.name != null)
				return false;
		} else if(!name.equals(other.name))
			return false;
		return true;
	}
}
