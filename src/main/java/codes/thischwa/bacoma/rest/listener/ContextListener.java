package codes.thischwa.bacoma.rest.listener;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;

import codes.thischwa.bacoma.rest.util.FileSystemUtil;

public class ContextListener implements ApplicationListener<ApplicationContextEvent> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		FileSystemUtil fu = event.getApplicationContext().getBean(FileSystemUtil.class);
		try {
			fu.checkAndCreateDataDir();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		logger.info("*** Application-Context successful initialized");
	}
}
