package codes.thischwa.bacoma.rest.listener;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * Listener to prevent running jetty, if the initialization of the spring-context failed.
 */
public class ContextListener extends ContextLoaderListener {

	private static final Logger logger = LoggerFactory.getLogger(ContextListener.class);
	
	public ContextListener(WebApplicationContext context) {
		super(context);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			super.contextInitialized(event);
		} catch (Exception e) {
			logger.error("The initialization of the servletContext failed! Server will be stopped!", e);
			System.exit(12);
		}
	}

}
