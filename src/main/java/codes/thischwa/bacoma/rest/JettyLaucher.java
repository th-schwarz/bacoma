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
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class JettyLaucher {
	private static final Logger logger = LoggerFactory.getLogger(JettyLaucher.class);
	
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		InputStream propIn = null;
		String propertiesFile  = System.getProperty("jetty.properties");
		try {
			propIn = Files.newInputStream(Paths.get(propertiesFile), StandardOpenOption.READ);
			props.load(propIn);
			logger.debug("'jetty.properties' successful read.");
		} catch (IOException e) {
			logger.warn("System property 'jetty.properties' couldn't be read. Defaults are being used.");
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
						logger.info("Server successful stopped.");
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
			}
		});
		
		String host = props.getProperty("host", "127.0.0.1");
		int port = Integer.parseInt(props.getProperty("port", "8080"));
		int timeout = Integer.parseInt(props.getProperty("timeoutSec", "900"));
		logger.info("Try to start server [{}:{}] with connection-timeout: {} sec.", host, port, timeout);
		ServerConnector connector = new ServerConnector(server);
		connector.setHost(host);
		connector.setPort(port);
		connector.setIdleTimeout(timeout * 1000);
		server.addConnector(connector);

		ServletContextHandler servletContextHandler = new ServletContextHandler();
		servletContextHandler.addEventListener(new ContextLoaderListener());
		servletContextHandler.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
		servletContextHandler.setResourceBase(System.getProperty("java.io.tmpdir"));
		
		ServletHolder holder = new ServletHolder("dispatcher", new DispatcherServlet());
		holder.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
		holder.setInitParameter("contextConfigLocation", WebConfig.class.getName());
		
		servletContextHandler.addServlet(holder, "/site/*");
		server.setHandler(servletContextHandler); 
        server.start();
        server.join();        
	}
}
