package codes.thischwa.bacoma.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan("codes.thischwa.*")
@PropertySource("classpath:/bacoma.properties")
public class WebConfig extends WebMvcConfigurerAdapter {
	
	// Just for using @Value and ${...} when referencing properties
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer propertySources = new PropertySourcesPlaceholderConfigurer();
		propertySources.setLocation(new ClassPathResource("bacoma.properties"));
		return propertySources;
	}
}