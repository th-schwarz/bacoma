package codes.thischwa.bacoma.rest.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import codes.thischwa.bacoma.rest.service.ContextUtility;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

@Controller
public class RenderController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ContextUtility cu;
	
	@Autowired
	private FileSystemUtil fileSystemUtil;
	

	@RequestMapping(value = "/render/get/{uuid}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public ResponseEntity<Response> get(@PathVariable UUID uuid) {
		// TODO implement
		return ResponseEntity.ok(Response.ok());
	}

}
