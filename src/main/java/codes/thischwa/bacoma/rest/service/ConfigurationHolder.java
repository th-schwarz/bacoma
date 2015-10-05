package codes.thischwa.bacoma.rest.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;

@Service
public class ConfigurationHolder {
	
	public static final String KEY_DEFAULT_ENCODING = "default.encoding";
	public static final String KEY_DIR_STATICRESOURCE = "site.dir.staticresource";
	public static final String KEY_DIR_EXPORT = "site.dir.export";
	
	public static final String KEY_EXPORT_FILE_EXTENSION = "site.export.file.extension";
	public static final String KEY_EXPORT_FILE_WELCOME = "site.export.file.welcome";
	public static final String KEY_EXPORT_DIR_RESOURCES_CSS = "site.export.dir.resources.css";
	public static final String KEY_EXPORT_DIR_RESOURCES_OTHER = "site.export.dir.resources.other";
	public static final String KEY_EXPORT_DIR_RESOURCES_STATIC = "site.export.dir.resources.static";
	
	private Map<String, String> defaultConfiguration;
	
	public ConfigurationHolder() throws IOException {
		defaultConfiguration = new TreeMap<>();
		Properties props = new Properties();
		props.load(ConfigurationHolder.class.getResourceAsStream("/bacoma.properties"));
		for(Object keyObj : props.keySet()) {
			String key = keyObj.toString();
			defaultConfiguration.put(key, props.getProperty(key));
		}
		String dirWebapp = System.getProperty("dir.webapp");
		if(dirWebapp != null)
			defaultConfiguration.put("dir.webapp", dirWebapp);
	}

	public Map<String, String> getDefaultConfiguration() {
		return defaultConfiguration;
	}
	
	public String get(String key) {
		return defaultConfiguration.get(key);
	}
	
	public String get(Site site, String key) {
		if(site.getConfiguration().containsKey(key))
			return site.getConfiguration().get(key);
		return get(key);
	}
}
