package codes.thischwa.bacoma.rest.model.pojo.site;


/**
 * Object for the velocity macros of a {@link Site}.
 */
public class Macro extends AbstractSiteResource {

	@Override
	public SiteResourceType getResourceType() {
		return SiteResourceType.MACRO;
	}
}
