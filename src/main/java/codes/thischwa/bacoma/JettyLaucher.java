package codes.thischwa.bacoma;

import java.util.Properties;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JettyLaucher {
	private static final Logger logger = LoggerFactory.getLogger(JettyLaucher.class);
	
	private final Server server = new Server();
	private WebAppContext webAppContext;
	
	JettyLaucher(final Properties props) {
		String host = props.getProperty("host", "127.0.0.1");
		int port = Integer.parseInt(props.getProperty("port", "8080"));
		int timeout = Integer.parseInt(props.getProperty("timeoutSec", "900"));
		String baseDir  = props.getProperty("baseDir", "webapp");
		logger.info("Try to start server [{}:{}], baseDir={}, connection-timeout={}sec.", host, port, baseDir, timeout);
		System.setProperty("dir.webapp", baseDir);

        // Handler for multiple web apps
        HandlerCollection handlers = new HandlerCollection();
        
        webAppContext = new WebAppContext();
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
		 
		server.setHandler(handlers);
	}
	
	JettyLaucher start() {
        try {
			server.start();
		} catch (Exception e) {
			logger.error("Start of the server failed!", e);
			System.exit(10);
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
        try {
			server.join();
		} catch (InterruptedException e) {
			logger.error("Jetty's threadpool was interrupted!", e);
			System.exit(11);
		}
		return this;
	}
	
}
