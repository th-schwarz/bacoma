package codes.thischwa.bacoma.rest.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.exception.SiteNotLoadedException;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.GenericRequestSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqLevel;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqPage;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqTemplate;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

@Component
public class ContextUtility {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Persister persister;

	@Autowired
	private BeanFactory beanFactory;
	
	@Autowired
	private FileSystemUtil fileSystemUtil;
	
	private Map<String, SiteManager> smCache;

	public ContextUtility() {
		persister = new Persister();
		smCache = new HashMap<>();
	}

	public Site getSite(String siteUrl) {
		return getSiteManager(siteUrl).getSite();
	}
	
	private SiteManager getSiteManager(String siteUrl) {
		if(!smCache.containsKey(siteUrl)) {
			try {
				Site site = load(siteUrl);
				SiteManager sm = beanFactory.getBean(SiteManager.class);
				sm.init(site);
				smCache.put(siteUrl, sm);
				logger.debug("successful caching of {}", siteUrl);
			} catch (BeansException | IOException e) {
				logger.error("Error while init site: " + siteUrl, e);
				throw new SiteNotLoadedException();
			}
		}
		return smCache.get(siteUrl);
	}

	private Site load(String siteUrl) throws IOException {
		return persister.load(fileSystemUtil.getDataDir(), siteUrl);
	}

	public AbstractBacomaObject<?> getObject(String siteUrl, UUID uuid) {
		return getSiteManager(siteUrl).getObject(uuid);
	}

	public void addLevel(String siteUrl, ReqLevel level) {
		getSiteManager(siteUrl).addLevel(level);
	}

	public void addResource(String siteUrl, GenericRequestSiteResource macro) {
		getSiteManager(siteUrl).addResource(macro);
	}
	
	public void addTemplate(String siteUrl, ReqTemplate template) {
		getSiteManager(siteUrl).addTemplate(template);
	}
	
	public void addPage(String siteUrl, ReqPage page) {
		getSiteManager(siteUrl).addPage(page);
	}

	public void setConfiguration(String siteUrl, Map<String, String> config) {
		getSiteManager(siteUrl).setConfiguration(config);		
	}

	public UUID setLayoutTemplate(String siteUrl, String text) {
		return getSiteManager(siteUrl).setLayoutTemplate(text);
	}

	public void remove(String siteUrl, UUID uuid) {
		getSiteManager(siteUrl).remove(uuid);
	}
}
