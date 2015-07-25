package codes.thischwa.bacoma.rest.model.pojo.site;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
	public List<Macro> getMacros() {
		return macros;
	}

	public void setMacros(List<Macro> macros) {
		this.macros = macros;
	}

	public List<CascadingStyleSheet> getCascadingStyleSheets() {
		return cascadingStyleSheets;
	}
	
	public void setCascadingStyleSheets(List<CascadingStyleSheet> cascadingStyleSheets) {
		this.cascadingStyleSheets = cascadingStyleSheets;
	}
	
	public List<OtherResource> getOtherResources() {
		return otherResources;
	}
	
	public void setOtherResources(List<OtherResource> otherResources) {
		this.otherResources = otherResources;
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
