package codes.thischwa.bacoma.rest.model.pojo.site;

/**
 * Base object - content of a {@link Page}.
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
}
