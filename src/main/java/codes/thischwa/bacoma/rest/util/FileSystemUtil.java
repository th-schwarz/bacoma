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

import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.service.ConfigurationHolder;

@Service
public class FileSystemUtil {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
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
	
	public Path getSitesDir(Site site, String... parts) {
		if(parts == null)
			return getAndCheckSitesDir(site);
		Path path = Paths.get(getAndCheckSitesDir(site).toAbsolutePath().toString(), parts);
		return path;
	}
	
	private Path getAndCheckSitesDir(Site site) throws RuntimeException {
		if(site == null)
			throw new IllegalArgumentException("No current site found!");
		Path dir = dataDir.resolve(site.getUrl());
		if(!Files.exists(dir)) {
			try {
				Files.createDirectories(dir);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return dir;
	}
	
	public Path getSiteExportDirectory(Site site) {
		return Paths.get(getAndCheckSitesDir(site).toString(), exportDir);
	}
	
	public Path getStaticResourceDir(Site site) {
		return getSitesDir(site, configurationHolder.get(site, "site.dir.staticresource"));
	}
	
	public String saveStaticSiteResource(Site site, String originalName, InputStream in) {
		Path resourceFolder = getStaticResourceDir(site);
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