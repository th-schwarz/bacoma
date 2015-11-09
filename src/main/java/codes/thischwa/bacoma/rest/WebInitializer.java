package codes.thischwa.bacoma.rest;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan("codes.thischwa.*")
@PropertySource("classpath:/bacoma.properties")
public class WebInitializer extends WebMvcConfigurerAdapter implements WebApplicationInitializer {
	private static final Logger logger = LoggerFactory.getLogger(WebInitializer.class);

	// Just for using @Value and ${...} when referencing properties
	@Bean
	public static PropertySourcesPlaceholderConfigurer getPlaceHolderConfigurer() {
		logger.info("*** entered #getPlaceHolderConfigurer");
		PropertySourcesPlaceholderConfigurer propertySources = new PropertySourcesPlaceholderConfigurer();
		propertySources.setLocation(new ClassPathResource("bacoma.properties"));
		return propertySources;
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		logger.info("*** entered #onStartup");
		WebApplicationContext context = getContext();
		servletContext.addListener(new ContextLoaderListener(context));
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher-servlet", new DispatcherServlet(context));
		dispatcher.setLoadOnStartup(0);
		dispatcher.addMapping("/site/*");
		dispatcher.setMultipartConfig(new MultipartConfigElement("/tmp", 1024*1024*20, 1024*1024*5, 0));
	}

	private AnnotationConfigWebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation("codes.thischwa.*");
		return context;
	}
}
