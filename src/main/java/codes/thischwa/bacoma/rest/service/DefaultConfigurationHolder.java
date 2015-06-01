package codes.thischwa.bacoma.rest.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

@Service
public class DefaultConfigurationHolder {
	
	private Map<String, String> defaultConfiguration;
	
	public DefaultConfigurationHolder() throws IOException {
		defaultConfiguration = new TreeMap<>();
		Properties props = new Properties();
		props.load(DefaultConfigurationHolder.class.getResourceAsStream("/bacoma.properties"));
		for(Object keyObj : props.keySet()) {
			String key = keyObj.toString();
			defaultConfiguration.put(key, props.getProperty(key));
		}
	}

	public Map<String, String> getDefaultConfiguration() {
		return defaultConfiguration;
	}
}
