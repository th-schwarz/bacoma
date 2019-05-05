package codes.thischwa.bacoma.rest.render.context.object;

import codes.thischwa.bacoma.model.BoInfo;
import codes.thischwa.bacoma.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.Constants;

public class LinkBuilder implements Constants {

	static String buildPreviewLink(AbstractBacomaObject<?> bo) {
		Site site = BoInfo.getSite(bo);
		String link = LINK_PREVIEW.replace("{uuid}", bo.getId().toString());
		link = link.replace("{siteUrl}", site.getUrl());
		return link;
	}
}
