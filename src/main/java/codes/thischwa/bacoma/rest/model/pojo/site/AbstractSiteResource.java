package codes.thischwa.bacoma.rest.model.pojo.site;


/**
 * Base class for resources of a {@link Site}.
 */
public abstract class AbstractSiteResource extends AbstractBacomaObject<Site> {
	private String name;
	private String text;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return String.format("%s#%s", name, getResourceType());
	}
	
	public abstract SiteResourceType getResourceType();
}

