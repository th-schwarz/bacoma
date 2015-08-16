package codes.thischwa.bacoma.rest.service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.exception.PersitException;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.GenericRequestSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqLevel;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqPage;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqTemplate;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

@Component
public class ContextUtility {

	private Persister persister;

	@Autowired
	private SiteManager siteManager;
	
	@Autowired
	private FileSystemUtil fileSystemUtil;

	public ContextUtility() {
		persister = new Persister();
	}

	public Site getSite() {
		return siteManager.getSite();
	}

	public void persist() throws PersitException {
		persister.persist(fileSystemUtil.getAndCheckSitesDir(), getSite());
	}

	public void load(String siteUrl) throws IOException {
		Site site = persister.load(fileSystemUtil.getDataDir(), siteUrl);
		siteManager.init(site);
	}

	public AbstractBacomaObject<?> getObject(UUID uuid) {
		return siteManager.getObject(uuid);
	}

	public void addLevel(ReqLevel level) {
		siteManager.addLevel(level);
	}

	public void addResource(GenericRequestSiteResource macro) {
		siteManager.addResource(macro);
	}
	
	public void addTemplate(ReqTemplate template) {
		siteManager.addTemplate(template);
	}
	
	public void addPage(ReqPage page) {
		siteManager.addPage(page);
	}

	public void setConfiguration(Map<String, String> config) {
		siteManager.setConfiguration(config);		
	}

	public UUID setLayoutTemplate(String text) {
		return siteManager.setLayoutTemplate(text);
	}

	public void remove(UUID uuid) {
		siteManager.remove(uuid);
	}
}
