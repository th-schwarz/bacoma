package codes.thischwa.bacoma.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import codes.thischwa.bacoma.rest.service.ConfigurationHolder;
import codes.thischwa.bacoma.rest.service.SiteManager;

@Service
public class FileSystemUtil {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SiteManager sm;
	
	@Autowired
	private ConfigurationHolder configurationHolder;
	
	@Value("${site.dir.export}")
	private String exportDir;
	
	@Value("${dir.data}") 
	String dataDirStr;
	
	private Path dataDir;
	
	public void init() throws IOException {
		dataDir = Paths.get(configurationHolder.get("dir.webapp"), dataDirStr);
		if(!Files.exists(dataDir)) {
			Files.createDirectories(dataDir);
			logger.info("Data dir created successful: {}", dataDir.toString());
		} else {
			logger.info("Data dir found: {}", dataDir.toString());
		}
	}
	
	public Path getDataDir() {
		if(dataDir == null)
			throw new IllegalStateException("#init was never called!");
		return dataDir;
	}
	
	public Path getSitesDir(String... parts) {
		if(parts == null)
			return getAndCheckSitesDir();
		Path path = Paths.get(getAndCheckSitesDir().toAbsolutePath().toString(), parts);
		return path;
	}
	
	private Path getAndCheckSitesDir() throws RuntimeException {
		if(sm.getSite() == null)
			throw new IllegalArgumentException("No current site found!");
		Path dir = dataDir.resolve(sm.getSite().getUrl());
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
		return Paths.get(getAndCheckSitesDir().toString(), exportDir);
	}
	
	public Path getStaticResourceDir() {
		return getSitesDir(configurationHolder.get(sm.getSite(), "site.dir.staticresource"));
	}
	
	public String saveStaticSiteResource(String originalName, InputStream in) {
		Path resourceFolder = getStaticResourceDir();
		String fileName = getUniqueName(resourceFolder, originalName);
		Path resourceFile = resourceFolder.resolve(fileName); 
		OutputStream out = null;
		try {
			out = Files.newOutputStream(resourceFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			IOUtils.copy(in, out);
		} catch (IOException e) {
			throw new RuntimeException(e); // TODO smarter handling
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
		return fileName;
	}
	
	protected String getUniqueName(Path path, String name) {
		String baseName = FilenameUtils.getBaseName(name);
		String extension = FilenameUtils.getExtension(name);
		if(!extension.isEmpty())
			extension = ".".concat(extension.toLowerCase());
		int i = 0;
		Path temp = path.resolve(name).toAbsolutePath();
		while(Files.exists(temp)) {
			i++;
			String newName = String.format("%s_%d%s", baseName, i, extension);
			temp = Paths.get(path.toString(), newName);
		}
		return temp.getFileName().toString();
	}
	
}