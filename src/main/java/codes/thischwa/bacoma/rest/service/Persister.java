package codes.thischwa.bacoma.rest.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Persister {

	public void persist(Path dir, Site site) throws IOException {
		persist(dir.toFile(), site);
	}
	
	public void persist(File dir, Site site) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File dataFile = new File(dir, String.format("%s.json", site.getUrl()));
		if(dataFile.exists())
			dataFile.delete();
		mapper.writeValue(dataFile, site);
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
