package codes.thischwa.bacoma.rest.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

import codes.thischwa.bacoma.exception.PersitException;
import codes.thischwa.bacoma.model.pojo.site.Site;

public class Persister {

	public void persist(Path dir, Site site) throws PersitException {
		persist(dir.toFile(), site);
	}
	
	public void persist(File dir, Site site) throws PersitException {
		ObjectMapper mapper = new ObjectMapper();
		File dataFile = new File(dir, String.format("%s.json", site.getUrl()));
		if(dataFile.exists())
			dataFile.delete();
		try {
			mapper.writeValue(dataFile, site);
		} catch (IOException e) {
			throw new PersitException(site, e);
		}
	}

	public Site load(Path dataDir, String url) throws IOException {
		return load(dataDir.toFile(), url);
	}
	
	public Site load(File dataDir, String url) throws IOException {
		File dataFile = new File(dataDir, String.format("%s.json", url));
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(dataFile, Site.class);
	}
}
