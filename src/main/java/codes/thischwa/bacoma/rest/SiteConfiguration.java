package codes.thischwa.bacoma.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class SiteConfiguration {

	public static final String KEY_DIR_STATICRESOURCE = "dir.staticresource";
	public static final String KEY_DIR_EXPORT = "dir.export";
	
	public static final String KEY_EXPORT_FILE_EXTENSION = "export.file.extension";
	public static final String KEY_EXPORT_FILE_WELCOME = "export.file.welcome";
	public static final String KEY_EXPORT_DIR_RESOURCES_CSS = "export.dir.resources.css";
	public static final String KEY_EXPORT_DIR_RESOURCES_OTHER = "export.dir.resources.other";
	public static final String KEY_EXPORT_DIR_RESOURCES_STATIC = "export.dir.resources.static";
	
	private Map<String, String> site = new HashMap<>();
	private Map<String, String> velocity = new HashMap<>();

	public Map<String, String> getSite() {
		return site;
	}

	public void setSite(Map<String, String> site) {
		this.site = site;
	}

	public Map<String, String> getVelocity() {
		return velocity;
	}

	public void setVelocity(Map<String, String> renderer) {
		this.velocity = renderer;
	}

}
