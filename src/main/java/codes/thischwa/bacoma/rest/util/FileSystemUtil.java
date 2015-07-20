package codes.thischwa.bacoma.rest.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import codes.thischwa.bacoma.rest.service.SiteManager;

@Service
public class FileSystemUtil {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SiteManager sm;
	
	@Value("${site.export.folder}")
	private String exportFolder;
	
	private Path dataDir;
	
	@Autowired
	public FileSystemUtil(@Value("${dir.data}") String dataDirStr) throws IOException {
		dataDir = Paths.get(dataDirStr);
		if(!Files.exists(dataDir)) {
			Files.createDirectories(dataDir);
			logger.info("Data dir created successful: {}", dataDir.toString());
		} else {
			logger.info("Data dir found: {}", dataDir.toString());
		}
	}
	
	public Path getDataDir() {
		return dataDir;
	}
	
	public Path getSitesDir(String... parts) {
		if(parts == null)
			return getAndCheckSitesDir();
		Path path = Paths.get(getAndCheckSitesDir().toAbsolutePath().toString(), parts);
		return path;
	}
	
	public Path getAndCheckSitesDir() throws RuntimeException {
		if(sm.getSite() == null)
			throw new IllegalArgumentException("No current site found!");
		Path dir = Paths.get(dataDir.toString(), sm.getSite().getUrl());
		if(!Files.exists(dir)) {
			try {
				Files.createDirectories(dir);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return dir;
	}
	
	public Path getSiteExportDirectory() {
		return Paths.get(getAndCheckSitesDir().toString(), exportFolder);
	}
	
}