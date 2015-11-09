package codes.thischwa.bacoma.jetty;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codes.thischwa.bacoma.rest.Constants;

public class JettyLaucher {
	private static final Logger logger = LoggerFactory.getLogger(JettyLaucher.class);
	
	private final static Server server = new Server();
	private static Properties props = new Properties();
	private static ServletContextHandler servletContextHandler;
	
	public static void addServletHolder(ServletHolder servletHolder, String pathSpec) {
		if(server.isRunning()) {
			servletContextHandler.addServlet(servletHolder, pathSpec);
		}
	}
	
	public static void main(String[] args) throws Exception {
		InputStream propIn = null;
		String propertiesFile  = System.getProperty("bacoma-server.properties");
		try {
			propIn = Files.newInputStream(Paths.get(propertiesFile), StandardOpenOption.READ);
			props.load(propIn);
			logger.debug("'jetty.properties' successful read.");
		} catch (Exception e) {
			logger.warn("System property 'bacoma-server.properties' couldn't be read. Defaults are being used.");
		} finally {
			IOUtils.closeQuietly(propIn);
		}
		
		if(!Files.exists(Constants.DIR_TEMP)) {
			Files.createDirectories(Constants.DIR_TEMP);
			logger.info("Temp-directory successful created: {}", Constants.DIR_TEMP);
		} else {
			logger.info("Temp-directory found: {}", Constants.DIR_TEMP);
		}
		
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
		String baseDir  = props.getProperty("baseDir", "webapp");
		logger.info("Try to start server [{}:{}], baseDir={}, connection-timeout={}sec.", host, port, baseDir, timeout);
		System.setProperty("dir.webapp", baseDir);

        // Handler for multiple web apps
        HandlerCollection handlers = new HandlerCollection();
        
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", 
                ".*/classes/*");
        webAppContext.setResourceBase(baseDir);
        webAppContext.setConfigurations(new Configuration[] {
        	    new AnnotationConfiguration(), new WebInfConfiguration(),
                new PlusConfiguration(), new MetaInfConfiguration(),
                new FragmentConfiguration(), new EnvConfiguration()
        	});
        handlers.addHandler(webAppContext);
        
		ServerConnector connector = new ServerConnector(server);
		connector.setHost(host);
		connector.setPort(port);
		connector.setIdleTimeout(timeout * 1000);
		server.addConnector(connector);
		 
//		ServletHolder ckeditorHolder = new ServletHolder(ZipProxyServlet.class);
//		ckeditorHolder.setInitParameter("file", baseDir+"/ckeditor.zip");
//		ckeditorHolder.setInitParameter("zipPathToSkip", "ckeditor");
//		ckeditorHolder.setInitOrder(1);
//		servletContextHandler.addServlet(ckeditorHolder, "/ckeditor/*");
//
//		ServletHolder codeEditorHolder = new ServletHolder(ZipProxyServlet.class);
//		codeEditorHolder.setInitParameter("file", baseDir+"/codemirror.zip");
//		codeEditorHolder.setInitParameter("zipPathToSkip", "codemirror");
//		codeEditorHolder.setInitOrder(2);
//		servletContextHandler.addServlet(codeEditorHolder, "/codemirror/*");
//		
//		ServletHolder formLibHolder = new ServletHolder(StaticServlet.class);
//		formLibHolder.setInitParameter("basePath", baseDir+"/form_lib");
//		formLibHolder.setInitOrder(10);
//		servletContextHandler.addServlet(formLibHolder, "/form_lib/*");
		
		//server.setHandler(servletContextHandler);
		server.setHandler(handlers);
        try {
			server.start();
		} catch (Exception e) {
			logger.error("Start of the server failed!", e);
			System.exit(10);
		}
        server.join();        
	}
	
}
