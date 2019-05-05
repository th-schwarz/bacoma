package codes.thischwa.bacoma.rest;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class JettyConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(JettyConfiguration.class);

	@Value("${jetty.host}")
	private String host;

	@Value("${jetty.port}")
	private Integer port;

	@Value("${jetty.sessionTimeout}")
	private Integer sessionTimeout;

	@Value("${jetty.context}")
	private String context;

	@Value("${jetty.baseDir}")
	private String baseDir;

	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() throws UnknownHostException {
		logger.info("*** Jetty [{}:{}], baseDir={}, connection-timeout={}sec.", host, port, baseDir, sessionTimeout);
		
		JettyServletWebServerFactory jc = new JettyServletWebServerFactory();
		InetAddress addr = InetAddress.getByName(host);
		jc.setAddress(addr);
		jc.setPort(port);
		jc.setContextPath(context);
//		jc.setSessionTimeout(sessionTimeout, TimeUnit.SECONDS);
		jc.setDocumentRoot(new File(baseDir));
		return jc;
	}
}
