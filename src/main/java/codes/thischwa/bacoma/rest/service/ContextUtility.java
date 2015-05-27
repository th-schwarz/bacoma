package codes.thischwa.bacoma.rest.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.model.pojo.requestcycle.GenericRequestSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqLevel;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqPage;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqTemplate;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ContextUtility {

	private int count = 0;

	private Persister persister;

	private File dataDir;

	@Autowired
	private SiteManager siteManager;

	private String user;

	@Autowired
	public ContextUtility(@Value("${dir.data}") String dataDirStr) {
		persister = new Persister();
		dataDir = new File(dataDirStr);
	}

	public int getCount() {
		return count;
	}

	public void inc() {
		count++;
	}

	@Deprecated
	public File getDataDir() {
		return dataDir;
	}

	public Site getSite() {
		return (siteManager == null) ? null : siteManager.getSite();
	}

	public String getUser() {
		return user;
	}
	
	@Deprecated
	public File getAndCheckSitesDataDir() {
		File dir = new File(new File(dataDir, user), getSite().getUrl());
		if(!dir.exists())
			dir.mkdirs();
		return dir;
	}

	public void persist() throws IOException {
		persister.persist(getDataDir(), user, getSite());
	}

	public void load(String userName, String siteUrl) throws IOException {
		this.user = userName;
		Site site = persister.load(getDataDir(), userName, siteUrl);
		siteManager.init(site);
	}

	public AbstractBacomaObject<?> getObject(UUID uuid) {
		return siteManager.getObject(uuid);
	}

	public void setLevel(ReqLevel level) {
		siteManager.setLevel(level);
	}

	public void setSiteResource(GenericRequestSiteResource macro) {
		siteManager.setSiteResource(macro);
	}
	
	public void setTemplate(ReqTemplate template) {
		siteManager.setTemplate(template);
	}
	
	public void addPage(ReqPage page) {
		siteManager.setPage(page);
	}

	public void setConfiguration(Map<String, String> config) {
		siteManager.setConfiguration(config);		
	}

	public UUID setLayoutTemplate(String text) {
		return siteManager.setLayoutTemplate(text);
	}
}
