package codes.thischwa.bacoma.model.pojo.site;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CascadingStyleSheet extends AbstractSiteResource {

	@JsonIgnore
	@Override
	public SiteResourceType getResourceType() {
		return SiteResourceType.CSS;
	}

}
