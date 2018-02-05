package codes.thischwa.bacoma.rest.model.pojo.requestcycle;

import codes.thischwa.bacoma.rest.model.pojo.site.SiteResourceType;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericRequestSiteResource extends GenericRequestObject {
	
	private String text;	
	
	private SiteResourceType resourceType;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public SiteResourceType getResourceType() {
		return resourceType;
	}
	
	public void setResourceType(SiteResourceType resourceType) {
		this.resourceType = resourceType;
	}

	@JsonProperty("resourceType")
	public void setResourceType(String resourceType) {
		this.resourceType = Enum.valueOf(SiteResourceType.class, resourceType.toUpperCase());
	}

	public boolean isValid() {
		return getName() != null && text != null && resourceType != null;
	}

}
