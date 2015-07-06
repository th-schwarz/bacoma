package codes.thischwa.bacoma.rest.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import codes.thischwa.bacoma.rest.service.ConfigurationHolder;
import codes.thischwa.bacoma.rest.service.ContextUtility;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;
import codes.thischwa.bacoma.rest.util.ServletUtil;

@Controller
public class SiteResourceController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ContextUtility cu;

	@Autowired
	private FileSystemUtil fileSystemUtil;

	@Autowired
	private ConfigurationHolder configurationHolder;

	@RequestMapping(value = "/staticResource", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getStaticResource(@RequestParam String path) {
		logger.debug("enterded #getStaticResource, path={}", path);
		Path resource = fileSystemUtil.getDataDir(configurationHolder.get(cu.getSite(), "site.dir.staticresource"),
				path);
		if (!Files.exists(resource)) {
			return new ResponseEntity<>(Response.error("Resource not found!"), HttpStatus.NOT_FOUND);
		}

		try {
			return ResponseEntity.ok().contentLength(Files.size(resource))
					.contentType(MediaType.parseMediaType(ServletUtil.getMimeType(path)))
					.body(new InputStreamResource(Files.newInputStream(resource)));
		} catch (IOException e) {
			logger.error("Error while fetching a static resource.", e);
			return new ResponseEntity<>(Response.error("Error while fetching a static resource."), HttpStatus.CONFLICT);
		}
	}
}
