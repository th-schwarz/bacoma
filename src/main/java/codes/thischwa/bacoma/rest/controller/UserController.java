package codes.thischwa.bacoma.rest.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import codes.thischwa.bacoma.rest.model.pojo.requestcycle.User;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

@Controller
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FileSystemUtil fileSystemUtil;

	@RequestMapping(value="/user/getAll", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> getAll() {
		logger.debug("entered #getAll");
		List<String> users = new ArrayList<>();
		for(File folder : fileSystemUtil.getAndCheckSitesDataDir().listFiles(new FileFilter() {			
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		})) {
			users.add(folder.getName());
		}
		return ResponseEntity.ok(Response.ok(users));
	}
	
	@RequestMapping(value="/user/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> create(@RequestBody User user) {
		logger.debug("entered #create");
		File userDir = new File(fileSystemUtil.getAndCheckSitesDataDir(), user.getName());
		if(userDir.exists())
			return new ResponseEntity<>(Response.error("User not found."), HttpStatus.NOT_FOUND); 
		userDir.mkdirs();
		return ResponseEntity.ok(Response.ok());
	}

	@RequestMapping(value="/user/{userName}/getSites", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> getSites(@PathVariable String userName) {
		logger.debug("entered #getSites");
		File userDir = new File(fileSystemUtil.getAndCheckSitesDataDir(), userName);		
		List<String> sites = new ArrayList<>();
		for(File file : userDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".json");
			}
		})) {
			sites.add(FilenameUtils.getBaseName(file.getName()));
		}
		return ResponseEntity.ok(Response.ok(sites));
	}
}
