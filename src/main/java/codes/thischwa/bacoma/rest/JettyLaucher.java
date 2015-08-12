package codes.thischwa.bacoma.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyLaucher {
	private static final Logger logger = LoggerFactory.getLogger(JettyLaucher.class);
	
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		InputStream propIn = null;
		String propertiesFile  = System.getProperty("jetty.properties");
		try {
			propIn = Files.newInputStream(Paths.get(propertiesFile), StandardOpenOption.READ);
			props.load(propIn);
			logger.debug("jetty.properties successful read.");
		} catch (IOException e) {
		} finally {
			IOUtils.closeQuietly(propIn);
		}
		
		final Server server = new Server();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if(server.isRunning())
					try {
						server.stop();
						logger.debug("server successful stopped");
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
			}
		});
		
		ServerConnector connector = new ServerConnector(server);
		connector.setHost(props.getProperty("host"));
		connector.setPort(Integer.parseInt(props.getProperty("port")));
		connector.setIdleTimeout(Integer.MAX_VALUE);
		server.addConnector(connector);
		
		WebAppContext context = new WebAppContext();
		context.setContextPath("/");
		context.setDescriptor(JettyLaucher.class.getResource("/web.xml").toString());
		context.setResourceBase(props.getProperty("baseDir"));
		server.setHandler(context);   
        server.start();
        server.join();        
	}
	
}
