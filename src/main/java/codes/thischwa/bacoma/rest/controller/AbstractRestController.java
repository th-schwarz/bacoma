package codes.thischwa.bacoma.rest.controller;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.service.ConfigurationHolder;
import codes.thischwa.bacoma.rest.service.ContextUtility;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

abstract class AbstractRestController {
	
	protected static final String BASEURL = "/site/{siteUrl}";

	@Autowired
	protected ContextUtility cu;

	@Autowired
	protected FileSystemUtil fileSystemUtil;

	@Autowired
	private ConfigurationHolder configurationHolder;
	
	protected Site getSite(String siteUrl) {
		return cu.getSite(siteUrl);
	}
	
	protected Charset getDefaultCharset(String siteUrl) {
		return getDefaultCharset(getSite(siteUrl));
	}
	
	protected Charset getDefaultCharset(Site site) {
		String enc = getProperty(site, ConfigurationHolder.KEY_DEFAULT_ENCODING);
		return Charset.forName(enc);
	}

	protected String getProperty(String siteUrl, String key) {
		return getProperty(getSite(siteUrl), key);
	}
	
	protected String getProperty(Site site, String key) {
		return configurationHolder.getMergedProperty(site, key);
	}
	
	protected String getDefaultEncoding(Site site) {
		return getDefaultCharset(site).name();
	}
}
