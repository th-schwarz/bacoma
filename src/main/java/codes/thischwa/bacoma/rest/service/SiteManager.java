package codes.thischwa.bacoma.rest.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import codes.thischwa.bacoma.rest.exception.PersitException;
import codes.thischwa.bacoma.rest.exception.ResourceNotFoundException;
import codes.thischwa.bacoma.rest.exception.SiteNotLoadedException;
import codes.thischwa.bacoma.rest.model.InstanceUtil;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.GenericRequestSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqLevel;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqPage;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqTemplate;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.site.CascadingStyleSheet;
import codes.thischwa.bacoma.rest.model.pojo.site.Content;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.Macro;
import codes.thischwa.bacoma.rest.model.pojo.site.OtherResource;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.model.pojo.site.Template;
import codes.thischwa.bacoma.rest.model.pojo.site.TemplateType;
import codes.thischwa.bacoma.rest.render.ViewMode;
import codes.thischwa.bacoma.rest.util.ConfigurationUtil;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

/**
 * Holds a {@link Site}-object and manage some complex object of/for it.
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SiteManager {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Site site;
	private VelocityEngine velocityEngine;
	private Persister persister = new Persister();

	@Autowired
	private ConfigurationHolder defaultConfigurationHolder;

	@Autowired
	private FileSystemUtil fileSystemUtil;
	
	private Map<UUID, AbstractBacomaObject<?>> objectsPerIdentifier = new HashMap<>();

	private Map<String, String> siteConfig = new HashMap<>();

	private ViewMode viewMode = ViewMode.PREVIEW;

	public void init(Site site) throws IOException {
		this.site = site;
		objectsPerIdentifier.clear();
		identify(site);

		// ** build the configuration of the site
		siteConfig.putAll(ConfigurationUtil.getProperties(defaultConfigurationHolder.getDefaultConfiguration(), "site"));
		siteConfig.putAll(ConfigurationUtil.getProperties(defaultConfigurationHolder.getDefaultConfiguration(), "velocity"));
		if(site.getConfiguration() != null) {
			siteConfig.putAll(ConfigurationUtil.getProperties(site.getConfiguration(), "site"));
			siteConfig.putAll(ConfigurationUtil.getProperties(site.getConfiguration(), "velocity"));
		}

		velocityEngine = buildVelocityEngine();
	}

	public Site getSite() {
		if(site == null)
			throw new SiteNotLoadedException();
		return site;
	}

	public ViewMode getViewMode() {
		return viewMode;
	}

	public void setViewMode(ViewMode viewMode) {
		this.viewMode = viewMode;
	}

	/**
	 * @return the complete site configuration (merged with defaults)
	 */
	public Map<String, String> getSiteConfig() {
		if(site == null)
			throw new SiteNotLoadedException();
		return siteConfig;
	}

	public VelocityEngine getVelocityEngine() {
		if(site == null)
			throw new SiteNotLoadedException();
		return velocityEngine;
	}

	private void identify(AbstractBacomaObject<?> obj) {
		objectsPerIdentifier.put(obj.getId(), obj);
		if(InstanceUtil.isLevel(obj)) {
			Level lev = (Level) obj;
			if(lev.hasSublevels()) {
				for(Level subLev : lev.getSublevels()) {
					identify(subLev);
				}
			}
			for(Page page : lev.getPages()) {
				identify(page);
			}
			if(InstanceUtil.isSite(obj)) {
				Site s = (Site) obj;
				if(s.getLayoutTemplate() != null)
					identify(s.getLayoutTemplate());
				for(Macro m : s.getMacros())
					identify(m);
				for(Template t : s.getTemplates())
					identify(t);
				for(CascadingStyleSheet c : s.getCascadingStyleSheets())
					identify(c);
				for(OtherResource o : s.getOtherResources())
					identify(o);
			}
		} else if(InstanceUtil.isPage(obj)) {
			Page p = (Page) obj;
			if(p.getContent() != null) {
				for(Content c : p.getContent())
					objectsPerIdentifier.put(c.getId(), c);
			}
		}
	}

	private VelocityEngine buildVelocityEngine() throws IOException {
		if(site == null)
			throw new SiteNotLoadedException();
		// merge the configs
		Map<String, String> velConfig = ConfigurationUtil.getProperties(siteConfig, "velocity", true);

		Properties props = new Properties();
		props.putAll(velConfig);

		VelocityEngine ve = new VelocityEngine();
		ve.init(props);

		StringResourceRepository repo = StringResourceLoader.getRepository();

		// add site specific macros
		if(site.getMacros() != null)
			for(Macro macro : site.getMacros())
				repo.putStringResource(macro.getName(), macro.getText());

		// add content.vm
		// String content = IOUtils.toString(SiteManager.class.getResourceAsStream("/vm/content.vm"));
		// repo.putStringResource("content.vm", content);

		logger.info("*** VelocityEngine successful initialized for: {}", site.getUrl());
		return ve;
	}

	private void persist() throws PersitException {
		persister.persist(fileSystemUtil.getDataDir(), site);
	}
	
	public AbstractBacomaObject<?> getObject(UUID uuid) {
		if(site == null)
			throw new SiteNotLoadedException();
		return objectsPerIdentifier.get(uuid);
	}

	public void addTemplate(ReqTemplate reqTemplate) {
		if(site == null)
			throw new SiteNotLoadedException();
		Template template;
		if(reqTemplate.isUpdateRequest()) {
			template = (Template) objectsPerIdentifier.get(reqTemplate.getId());
		} else {
			template = new Template();
			template.setId(UUID.randomUUID());
			objectsPerIdentifier.put(template.getId(), template);
			site.getTemplates().add(template);
			reqTemplate.setId(template.getId());
		}
		template.setName(reqTemplate.getName());
		template.setText(reqTemplate.getText());
		template.setType(reqTemplate.getTemplateType());
		persist();
	}

	public void addLevel(ReqLevel reqLevel) {
		if(site == null)
			throw new SiteNotLoadedException();
		Level level;
		if(reqLevel.isUpdateRequest()) {
			level = (Level) objectsPerIdentifier.get(reqLevel.getId());
		} else {
			level = new Level();
			level.setId(UUID.randomUUID());
			objectsPerIdentifier.put(level.getId(), level);
			Level parentLevel = (Level) objectsPerIdentifier.get(reqLevel.getParent());
			parentLevel.add(level);
			reqLevel.setId(level.getId());
		}
		level.setName(reqLevel.getName());
		level.setTitle(reqLevel.getTitle());
		persist();
	}

	public void addPage(ReqPage reqPage) {
		if(site == null)
			throw new SiteNotLoadedException();
		Page page;
		if(reqPage.isUpdateRequest()) {
			page = (Page) objectsPerIdentifier.get(reqPage.getId());
		} else {
			page = new Page();
			page.setId(UUID.randomUUID());
			objectsPerIdentifier.put(page.getId(), page);
			Level parentLevel = (Level) objectsPerIdentifier.get(reqPage.getParent());
			parentLevel.add(page);
		}
		page.setName(reqPage.getName());
		page.setTitle(reqPage.getTitle());
		page.setTemplateID(reqPage.getTemplate());
		persist();
	}

	public void setConfiguration(Map<String, String> config) {
		if(site == null)
			throw new SiteNotLoadedException();
		site.setConfiguration(config);
		siteConfig.putAll(config);
		persist();
		VelocityEngine ve = null;
		try {
			ve = buildVelocityEngine();
			velocityEngine = ve;
		} catch (IOException e) {
			logger.error("Error while building the VelocityEngine with the new configuration", e);
			// TODO throw a defined exception for the response status
		}
	}

	public UUID setLayoutTemplate(String text) {
		if(site == null)
			throw new SiteNotLoadedException();
		Template layoutTemplate = new Template();
		layoutTemplate.setId(UUID.randomUUID());
		layoutTemplate.setName("layout");
		layoutTemplate.setType(TemplateType.LAYOUT);
		layoutTemplate.setText(text);
		objectsPerIdentifier.put(layoutTemplate.getId(), layoutTemplate);
		site.setLayoutTemplate(layoutTemplate);
		persist();
		return layoutTemplate.getId();
	}

	public void addResource(GenericRequestSiteResource reqSiteResource) {
		if(site == null)
			throw new SiteNotLoadedException();
		AbstractSiteResource siteResource;
		if(reqSiteResource.isUpdateRequest()) {
			siteResource = (AbstractSiteResource) objectsPerIdentifier.get(reqSiteResource.getId());
		} else {
			switch(reqSiteResource.getResourceType()) {
				case CSS:
					siteResource = new CascadingStyleSheet();
					site.addCascadingStyleSheet((CascadingStyleSheet) siteResource);
					break;
				case MACRO:
					siteResource = new Macro();
					site.addMacro((Macro) siteResource);
					break;
				case OTHER:
					siteResource = new OtherResource();
					site.addOtherResource((OtherResource) siteResource);
					break;
				default:
					throw new IllegalArgumentException(
							String.format("Forbidden or unknow resource-type: %s", reqSiteResource.getResourceType().toString()));
			}
			siteResource.setId(UUID.randomUUID());
			objectsPerIdentifier.put(siteResource.getId(), siteResource);
			reqSiteResource.setId(siteResource.getId());
		}
		siteResource.setName(reqSiteResource.getName());
		siteResource.setText(reqSiteResource.getText());
		persist();
	}

	public void remove(UUID id) {
		if(site == null)
			throw new SiteNotLoadedException();
		AbstractBacomaObject<?> bo = objectsPerIdentifier.get(id);
		if(bo == null)
			throw new ResourceNotFoundException(site, id);
		if(InstanceUtil.isSite(bo))
			throw new IllegalArgumentException("A site-object can't be removed in this context!");
		
		if(InstanceUtil.isSiteResource(bo)) {
			AbstractSiteResource siteResource = (AbstractSiteResource) bo;
			switch(siteResource.getResourceType()) {
				case CSS:
					site.getCascadingStyleSheets().remove(siteResource);
					break;
				case TEMPLATE:
					site.getTemplates().remove(siteResource);
					break;
				case MACRO:
					site.getMacros().remove(siteResource);
					break;
				case OTHER:
					site.getOtherResources().remove(siteResource);
				default:
					throw new IllegalArgumentException(String.format("Unknown resource-type: %s", siteResource.getResourceType().toString()));
			}
			
		} else if(InstanceUtil.isJustLevel(bo)) {
			Level lev = (Level) bo;
			Level parent = lev.getParent();
			parent.getSublevels().remove(lev);			
		
		} else if(InstanceUtil.isPage(bo)) {
			Page page = (Page) bo;
			Level parent = page.getParent();
			parent.getPages().remove(page);
			// TODO special handling for galleries and their images
		}		
		persist();
	}
}
