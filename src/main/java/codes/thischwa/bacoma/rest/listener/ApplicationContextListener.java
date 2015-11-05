package codes.thischwa.bacoma.rest.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import codes.thischwa.bacoma.rest.util.FileSystemUtil;

@Component
public class ApplicationContextListener implements ApplicationListener<ApplicationContextEvent> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		ApplicationContext ctx = event.getApplicationContext();
		if(!(ctx instanceof AnnotationConfigWebApplicationContext))
			throw new RuntimeException("Initialization of the apllication-context failed!");
		
		try {
			FileSystemUtil fsu = ctx.getBean(FileSystemUtil.class);
			fsu.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		logger.info("*** Application-context successful initialized");
	}
}
