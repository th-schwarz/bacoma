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
import org.springframework.stereotype.Service;

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
import codes.thischwa.bacoma.rest.util.ConfigurationUtil;

@Service
public class SiteManager {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Site site;
	private VelocityEngine velocityEngine;
	
	@Autowired
	private DefaultConfigurationHolder defaultConfigurationHolder;
	
	private Map<UUID, AbstractBacomaObject<?>> objectsPerIdentifier = new HashMap<>();

	public void init(Site site) throws IOException {
		this.site = site;
		objectsPerIdentifier.clear();
		identify(site);
		velocityEngine = buildVelocityEngine(site);
	}

	public Site getSite() {
		return site;
	}
	
	public VelocityEngine getVelocityEngine() {
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
			}
		} else if(InstanceUtil.isPage(obj)) {
			Page p = (Page)obj;
			if(p.getContent() != null) {
				for(Content c : p.getContent()) 
					objectsPerIdentifier.put(c.getId(), c);
			}
		}
	}
	
	private VelocityEngine buildVelocityEngine(Site site) throws IOException {
		// merge the configs
		Map<String, String> velConfig = defaultConfigurationHolder.getVelocityConfiguration();
		velConfig.putAll(ConfigurationUtil.getProperties(site.getConfiguration(), "velocity", true));
		
		Properties props = new Properties();
		props.putAll(velConfig);
		
		VelocityEngine ve = new VelocityEngine();
		ve.init(props);
		
		StringResourceRepository repo = StringResourceLoader.getRepository();

		// add site specific macros
		if (site.getMacros() != null)
			for (Macro macro : site.getMacros())
				repo.putStringResource(macro.getName(), macro.getText());
		
		// add content.vm
//		String content = IOUtils.toString(SiteManager.class.getResourceAsStream("/vm/content.vm"));
//		repo.putStringResource("content.vm", content);
					
		logger.info("*** VelocityEngine successful initialized for: {}", site.getUrl());
		return ve;
	}
	
	public AbstractBacomaObject<?> getObject(UUID uuid) {
		return objectsPerIdentifier.get(uuid);
	}
	
	public void setTemplate(ReqTemplate reqTemplate) {
		Template template;
		if(reqTemplate.isUpdateRequest()) {
			template = (Template) objectsPerIdentifier.get(reqTemplate.getId());
		} else {
			template = new Template();
			template.setId(UUID.randomUUID());
			objectsPerIdentifier.put(template.getId(), template);
			site.addTemplate(template);
			reqTemplate.setId(template.getId());
		}
		template.setName(reqTemplate.getName());
		template.setText(reqTemplate.getText());
		template.setType(reqTemplate.getTemplateType());
	}

	public void setLevel(ReqLevel reqLevel) {
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
	}
	
	public void setPage(ReqPage reqPage) {
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
	}

	public void setConfiguration(Map<String, String> config) {
		site.setConfiguration(config);		
	}

	public UUID setLayoutTemplate(String text) {
		Template layoutTemplate = new Template();
		layoutTemplate.setId(UUID.randomUUID());
		layoutTemplate.setName("layout");
		layoutTemplate.setType(TemplateType.LAYOUT);
		layoutTemplate.setText(text);
		objectsPerIdentifier.put(layoutTemplate.getId(), layoutTemplate);
		site.setLayoutTemplate(layoutTemplate);
		return layoutTemplate.getId();
	}

	public void setSiteResource(GenericRequestSiteResource reqSiteResource) {
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
	}
}
