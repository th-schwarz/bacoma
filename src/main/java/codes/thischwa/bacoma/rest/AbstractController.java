package codes.thischwa.bacoma.rest;

import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.service.ContextUtility;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

public abstract class AbstractController {
		
	@Autowired
	protected FileSystemUtil fileSystemUtil;

	@Autowired
	protected ContextUtility cu;
	
	@Autowired
	private SiteConfiguration siteConfiguration;

	protected Site getSite(String siteUrl) {
		return cu.getSite(siteUrl);
	}
	
	protected String getProperty(Site site, String key) {
		return siteConfiguration.getSite().get(key);
	}
}
