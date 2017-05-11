package codes.thischwa.bacoma;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Starter {

	private static final Logger logger = LoggerFactory.getLogger(Starter.class);

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
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
		
		// TODO optional temp-dir-setting with system-properties
		if(!Files.exists(Constants.DIR_TEMP)) {
			Files.createDirectories(Constants.DIR_TEMP);
			logger.info("Temp-directory successful created: {}", Constants.DIR_TEMP);
		} else {
			logger.info("Temp-directory found: {}", Constants.DIR_TEMP);
		}
		
		new JettyLaucher(props).start();
	}

}
