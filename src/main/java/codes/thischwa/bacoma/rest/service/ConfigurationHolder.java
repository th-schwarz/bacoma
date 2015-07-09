package codes.thischwa.bacoma.rest.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;

@Service
public class ConfigurationHolder {
	
	private Map<String, String> defaultConfiguration;
	
	public ConfigurationHolder() throws IOException {
		defaultConfiguration = new TreeMap<>();
		Properties props = new Properties();
		props.load(ConfigurationHolder.class.getResourceAsStream("/bacoma.properties"));
		for(Object keyObj : props.keySet()) {
			String key = keyObj.toString();
			defaultConfiguration.put(key, props.getProperty(key));
		}
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
