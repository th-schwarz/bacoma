package codes.thischwa.bacoma.rest.model.pojo.site;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codes.thischwa.bacoma.rest.model.InstanceUtil;

/**
 * Base object for the site.
 */
public class Site extends Level {
	private String url;
	private List<Template> templates = new ArrayList<>();
	private List<Macro> macros = new ArrayList<>();
	private List<CascadingStyleSheet> cascadingStyleSheets = new ArrayList<>();
	private List<OtherResource> otherResources = new ArrayList<>();
	private Template layoutTemplate;
	
	private Map<String, String> configuration = new HashMap<>();
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}
	
	public void addTemplate(Template template) {
		templates.add(template);
	}
	
	public List<Macro> getMacros() {
		return macros;
	}

	public void setMacros(List<Macro> macros) {
		this.macros = macros;
	}

	public void addMacro(Macro macro) {
		macros.add(macro);
	}
	
	public List<CascadingStyleSheet> getCascadingStyleSheets() {
		return cascadingStyleSheets;
	}
	
	public void setCascadingStyleSheets(List<CascadingStyleSheet> cascadingStyleSheets) {
		this.cascadingStyleSheets = cascadingStyleSheets;
	}
	
	public void addCascadingStyleSheet(CascadingStyleSheet cascadingStyleSheet) {
		cascadingStyleSheets.add(cascadingStyleSheet);
	}
	
	public List<OtherResource> getOtherResources() {
		return otherResources;
	}
	
	public void setOtherResources(List<OtherResource> otherResources) {
		this.otherResources = otherResources;
	}
	
	public void addOtherResource(OtherResource otherResource) {
		otherResources.add(otherResource);
	}
	
	public Template getLayoutTemplate() {
		return layoutTemplate;
	}
	
	public void setLayoutTemplate(Template layoutTemplate) {
		this.layoutTemplate = layoutTemplate;
	}

	public void remove(AbstractSiteResource res) {
		if (InstanceUtil.isTemplate(res))
			templates.remove(res);
		else if (InstanceUtil.isMacro(res))
			macros.remove(res);
		else
			throw new IllegalArgumentException("unknown site resource type");
	}
	
	public Map<String, String> getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

	@Override
	public String toString() {
		return url;
	}
}
