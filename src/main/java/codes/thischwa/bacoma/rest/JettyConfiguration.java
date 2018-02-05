package codes.thischwa.bacoma.rest;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
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
	public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() throws UnknownHostException {
		logger.info("*** Jetty [{}:{}], baseDir={}, connection-timeout={}sec.", host, port, baseDir, sessionTimeout);
		
		JettyEmbeddedServletContainerFactory jc = new JettyEmbeddedServletContainerFactory();
		InetAddress addr = InetAddress.getByName(host);
		jc.setAddress(addr);
		jc.setPort(port);
		jc.setContextPath(context);
		jc.setSessionTimeout(sessionTimeout, TimeUnit.SECONDS);
		jc.setDocumentRoot(new File(baseDir));
		return jc;
	}
}
