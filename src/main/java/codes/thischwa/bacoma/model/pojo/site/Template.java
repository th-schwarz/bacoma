package codes.thischwa.bacoma.model.pojo.site;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Base object for the templates of a {@link Site}.
 */
public class Template extends AbstractSiteResource {
	
	private TemplateType type;
	
	public TemplateType getType() {
		return type;
	}
	public void setType(TemplateType type) {
		this.type = type;
	}
	@JsonProperty("type")
	public void setType(String templateType) {
		type = Enum.valueOf(TemplateType.class, templateType.toUpperCase());
	}
	
	@JsonIgnore
	@Override
	public SiteResourceType getResourceType() {
		return SiteResourceType.TEMPLATE;
	}
	
	@JsonIgnore
	public boolean isLayoutTemplate() {
		return (type == TemplateType.LAYOUT);
	}
}
