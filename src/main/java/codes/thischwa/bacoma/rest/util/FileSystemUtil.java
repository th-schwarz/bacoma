package codes.thischwa.bacoma.rest.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import codes.thischwa.bacoma.rest.service.ContextUtility;
import codes.thischwa.bacoma.rest.service.SiteManager;

@Service
public class FileSystemUtil {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SiteManager sm;
	
	@Autowired
	private ContextUtility cu;
	
	@Value("${site.export.folder}")
	private String exportFolder;
	
	private File dataDir;
	
	public FileSystemUtil(@Value("${dir.data}") String dataDirStr) {
		dataDir = new File(dataDirStr);
	}
	
	public void checkAndCreateDataDir() throws IOException {
		if(!dataDir.exists()) {
			if(!dataDir.mkdirs())
				throw new IOException(String.format("Couldn't construct directory: %s", dataDir.getAbsolutePath()));
			logger.info("Data dir created successful: {}", dataDir.getAbsolutePath());
		}
	}

	public File getAndCheckSitesDataDir() {
		if(cu.getUser() == null || sm.getSite() == null)
			throw new IllegalArgumentException("No current user or site found!");
		File dir = new File(new File(dataDir, cu.getUser()), sm.getSite().getUrl());
		if(!dir.exists())
			dir.mkdirs();
		return dir;
	}
	
	public File getSiteExportDirectory() {
		return new File(getAndCheckSitesDataDir(), exportFolder);
	}
	
}