package codes.thischwa.bacoma.rest.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import codes.thischwa.bacoma.rest.util.ConfigurationUtil;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DefaultConfigurationHolder {
	
	private Map<String, String> defaultConfiguration;
	
	public DefaultConfigurationHolder() throws IOException {
		defaultConfiguration = new HashMap<>();
		Properties props = new Properties();
		props.load(DefaultConfigurationHolder.class.getResourceAsStream("/bacoma.properties"));
		for(Object keyObj : props.keySet()) {
			String key = keyObj.toString();
			defaultConfiguration.put(key, props.getProperty(key));
		}
	}

	public Map<String, String> getVelocityConfiguration() {
		return ConfigurationUtil.getProperties(defaultConfiguration, "velocity", true);
	}
	
	public Map<String, String> getDefaultConfiguration() {
		return defaultConfiguration;
	}
}
