package codes.thischwa.bacoma.rest.controller;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.service.ConfigurationHolder;
import codes.thischwa.bacoma.rest.service.ContextUtility;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

public abstract class AbstractController {
	
	private String defaultEncoding;
	
	private Charset defaultCharset;

	@Autowired
	protected ContextUtility cu;

	@Autowired
	protected FileSystemUtil fileSystemUtil;

	@Autowired
	private ConfigurationHolder configurationHolder;
	
	protected Site getSite() {
		return cu.getSite();
	}
	
	protected String getDefaultEncoding() {
		String enc = getProperty("default.encoding");
		if(defaultEncoding == null || !defaultEncoding.equalsIgnoreCase(enc)) {
			defaultEncoding = enc;
			defaultCharset = Charset.forName(defaultEncoding);
		}
		return defaultEncoding;
	}
	
	protected String getProperty(String key) {
		return configurationHolder.get(getSite(), key);
	}
	
	protected Charset getDefaultCharset() {
		if(defaultCharset == null)
			getDefaultEncoding();
		return defaultCharset;
	}
}
