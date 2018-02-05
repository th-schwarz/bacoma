package codes.thischwa.bacoma.rest.model.pojo.site;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Object for the velocity macros of a {@link Site}.
 */
public class Macro extends AbstractSiteResource {

	@JsonIgnore
	@Override
	public SiteResourceType getResourceType() {
		return SiteResourceType.MACRO;
	}
}
