package codes.thischwa.bacoma.rest.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import codes.thischwa.bacoma.Constants;
import codes.thischwa.bacoma.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.SiteConfiguration;

@Service
public class FileSystemUtil {

	private final Logger logger = LoggerFactory.getLogger(getClass());
		
	@Autowired
	private SiteConfiguration siteConfiguration;
	
	@Value("${site.dir.export}")
	private String exportDir;
	
	@Value("${dir.data}") 
	private String dataDirStr;

	@Value("${dir.webapp}") 
	private String dirWebapp;
	
	private Path dataDir;
	
	@PostConstruct
	public void init() throws IOException {
		dataDir = Paths.get(dirWebapp, dataDirStr);
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
		return getSitesDir(site, siteConfiguration.getSite().get(SiteConfiguration.KEY_DIR_STATICRESOURCE));
	}


	public Path removeStaticResource(Site site, String requestPath) throws IOException {
		Path resourceFolder = getStaticResourceDir(site);
		if(requestPath.startsWith(Constants.FILENAME_SEPARATOR))
			requestPath = requestPath.substring(Constants.FILENAME_SEPARATOR.length());
		Path resourceFile = resourceFolder.resolve(requestPath);
		if(Files.exists(resourceFile)) {
			Files.delete(resourceFile);
			return resourceFile;
		} else {
			throw new FileNotFoundException(String.format("File not found: %s", resourceFile.toString()));
		}
	}
	
	public String addStaticResource(Site site, String requestPath, InputStream in) {
		Path resourceFolder = getStaticResourceDir(site);
		if(requestPath.startsWith(Constants.FILENAME_SEPARATOR))
			requestPath = requestPath.substring(Constants.FILENAME_SEPARATOR.length());
		Path resourceFile = resourceFolder.resolve(requestPath);
		OutputStream out = null;
		try {
			if(!Files.exists(resourceFile.getParent()))
				Files.createDirectories(resourceFile.getParent());
			String fileName = getUniqueName(resourceFile.getParent(), resourceFile.getFileName().toString());
			resourceFile = resourceFile.getParent().resolve(fileName);
			out = Files.newOutputStream(resourceFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			IOUtils.copy(in, out);
			String uniqiueRequestPath = resourceFile.toAbsolutePath().toString().substring(resourceFolder.toAbsolutePath().toString().length());
			return uniqiueRequestPath;
		} catch (IOException e) {
			throw new RuntimeException(e); // TODO smarter handling
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
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