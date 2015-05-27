package codes.thischwa.bacoma.rest.service;

import java.io.File;
import java.io.IOException;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Persister {
	
	public void persist(File dataDir, String user, Site site) throws IOException {
		File userDir = new File(dataDir, user);
		if(!userDir.exists())
			userDir.mkdirs();
		
		ObjectMapper mapper = new ObjectMapper();
		File dataFile = new File(userDir, String.format("%s.json", site.getUrl()));
		if(dataFile.exists())
			dataFile.delete();
		mapper.writeValue(dataFile, site);
	}
	
	public Site load(File dataDir, String userName, String siteUrl) throws IOException {
		File userDir = new File(dataDir, userName);
		File dataFile = new File(userDir, String.format("%s.json", siteUrl));
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(dataFile, Site.class);
	}
}
