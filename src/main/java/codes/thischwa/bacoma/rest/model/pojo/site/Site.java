package codes.thischwa.bacoma.rest.model.pojo.site;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base object for the site.
 */
public class Site extends Level {
	private String url;
	private ChildList<Site, Template> templates = new ChildList<>(this);
	private ChildList<Site, Macro> macros = new ChildList<>(this);
	private ChildList<Site, CascadingStyleSheet> cascadingStyleSheets = new ChildList<>(this);
	private ChildList<Site, OtherResource> otherResources = new ChildList<>(this);
	private Template layoutTemplate;
	
	private Map<String, String> configuration = new HashMap<>();
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Template> getTemplates() {
		return templates.get();
	}
	public boolean addTemplate(Template template) {
		return templates.add(template);
	}
	public void setTemplates(List<Template> templates) {
		this.templates.set(templates);;
	}
		
	public List<Macro> getMacros() {
		return macros.get();
	}
	public boolean addMacro(Macro macro) {
		return macros.add(macro);
	}
	public void setMacros(List<Macro> macros) {
		this.macros.set(macros);
	}
	public List<CascadingStyleSheet> getCascadingStyleSheets() {
		return cascadingStyleSheets.get();
	}
	public boolean addCascadingStyleSheet(CascadingStyleSheet cascadingStyleSheet) {
		return cascadingStyleSheets.add(cascadingStyleSheet);
	}
	public void setCascadingStyleSheets(List<CascadingStyleSheet> cascadingStyleSheets) {
		this.cascadingStyleSheets.set(cascadingStyleSheets);
	}
	
	public List<OtherResource> getOtherResources() {
		return otherResources.get();
	}
	public void setOtherResources(List<OtherResource> otherResources) {
		this.otherResources.set(otherResources);
	}
	public boolean addOtherResource(OtherResource or) {
		return otherResources.add(or);
	}
				
	public Template getLayoutTemplate() {
		return layoutTemplate;
	}
	
	public void setLayoutTemplate(Template layoutTemplate) {
		this.layoutTemplate = layoutTemplate;
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
