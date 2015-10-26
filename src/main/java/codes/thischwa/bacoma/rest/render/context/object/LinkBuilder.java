package codes.thischwa.bacoma.rest.render.context.object;

import codes.thischwa.bacoma.rest.Constants;
import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;

public class LinkBuilder implements Constants {

	static String buildPreviewLink(AbstractBacomaObject<?> bo) {
		Site site = BoInfo.getSite(bo);
		String link = LINK_PREVIEW.replace("{uuid}", bo.getId().toString());
		link = link.replace("{siteUrl}", site.getUrl());
		return link;
	}
}
