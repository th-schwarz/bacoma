package codes.thischwa.bacoma.rest.controller;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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

import codes.thischwa.bacoma.rest.model.pojo.requestcycle.GenericRequestSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqLevel;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqPage;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqTemplate;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.service.ContextUtility;
import codes.thischwa.bacoma.rest.util.ServletUtil;

@Controller
public class SiteController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ContextUtility cu;
	
	@RequestMapping(value="/site/load/{userName}/{siteUrl}", method = RequestMethod.GET)
	public ResponseEntity<Response> load(@PathVariable String userName, @PathVariable String siteUrl) {
		try {
			cu.load(userName, siteUrl);
		} catch (IOException e) {
			logger.error("Error while loading a site.", e);
			return new ResponseEntity<>(Response.error("Site not found!"), HttpStatus.NOT_FOUND);
		}
		logger.info("site successful loaded: {}", siteUrl);
		
		String baseUrl= ServletUtil.getBaseUrl();
		logger.info("****** base-url: {}", baseUrl);
		return ResponseEntity.ok(Response.ok());
	}

	@RequestMapping(value="/site/setConfiguration", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> setConfiguration(@RequestBody Map<String, String> config) {
		Site site = cu.getSite();
		if(site == null) 
			return new ResponseEntity<>(Response.error("No site loaded!"), HttpStatus.NOT_FOUND);
		cu.setConfiguration(config);

		try {
			cu.persist();
		} catch (IOException e) {
			logger.error("Error while persisting a site.", e);
			return new ResponseEntity<>(Response.error("Error while persisting."), HttpStatus.CONFLICT);
		}
		return ResponseEntity.ok(Response.ok());
	}

	@RequestMapping(value="/site/setLayoutTemplate", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> setLayoutTemplate(@RequestBody String text) {
		Site site = cu.getSite();
		if(site == null) 
			return new ResponseEntity<>(Response.error("No site loaded!"), HttpStatus.NOT_FOUND);
		if(StringUtils.isEmpty(text))
			return new ResponseEntity<>(Response.error("Empty request!"), HttpStatus.BAD_REQUEST);
		UUID id = cu.setLayoutTemplate(text);
		try {
			cu.persist();
		} catch (IOException e) {
			logger.error("Error while persisting a site.", e);
			return new ResponseEntity<>(Response.error("Error while persisting."), HttpStatus.CONFLICT);
		}
		return ResponseEntity.ok(Response.ok(id));
	}

	@RequestMapping(value="/site/setSiteResource", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> setSiteResource(@RequestBody GenericRequestSiteResource siteResource) {
		if(cu.getSite() == null) 
			return new ResponseEntity<>(Response.error("No site loaded!"), HttpStatus.NOT_FOUND);
		if(!siteResource.isValid())
			return new ResponseEntity<>(Response.error("Request is incomplete"), HttpStatus.BAD_REQUEST);
		cu.setSiteResource(siteResource);
		try {
			cu.persist();
		} catch (IOException e) {
			logger.error("Error while persisting a site.", e);
			return new ResponseEntity<>(Response.error("Error while persisting."), HttpStatus.CONFLICT);
		}
		return ResponseEntity.ok(Response.ok(siteResource.getId()));
	}

	@RequestMapping(value="/site/setTemplate", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> setTemplate(@RequestBody ReqTemplate template) {
		if(cu.getSite() == null) 
			return new ResponseEntity<>(Response.error("No site loaded!"), HttpStatus.NOT_FOUND);
		if(!template.isValid())
			return new ResponseEntity<>(Response.error("Request is incomplete"), HttpStatus.BAD_REQUEST);
		cu.setTemplate(template);
		try {
			cu.persist();
		} catch (IOException e) {
			logger.error("Error while persisting a site.", e);
			return new ResponseEntity<>(Response.error("Error while persisting."), HttpStatus.CONFLICT);
		}
		return ResponseEntity.ok(Response.ok(template.getId()));
	}
	
	@RequestMapping(value="/site/setLevel", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> setLevel(@RequestBody ReqLevel level) {
		if(cu.getSite() == null) 
			return new ResponseEntity<>(Response.error("No site loaded!"), HttpStatus.NOT_FOUND);
		if(!level.isValid())
			return new ResponseEntity<>(Response.error("Request is incomplete"), HttpStatus.BAD_REQUEST);
		cu.setLevel(level);
		try {
			cu.persist();
		} catch (IOException e) {
			logger.error("Error while persisting a site.", e);
			return new ResponseEntity<>(Response.error("Error while persisting."), HttpStatus.CONFLICT);
		}
		return ResponseEntity.ok(Response.ok(level.getId()));
	}

	@RequestMapping(value="/site/setPage", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> setPage(@RequestBody ReqPage page) {
		if(cu.getSite() == null) 
			return new ResponseEntity<>(Response.error("No site loaded!"), HttpStatus.NOT_FOUND);
		if(!page.isValid())
			return new ResponseEntity<>(Response.error("Request is incomplete"), HttpStatus.BAD_REQUEST);
		cu.addPage(page);
		try {
			cu.persist();
		} catch (IOException e) {
			logger.error("Error while persisting a site.", e);
			return new ResponseEntity<>(Response.error("Error while persisting."), HttpStatus.CONFLICT);
		}
		return ResponseEntity.ok(Response.ok(page.getId()));
	}
	
	@RequestMapping(value="/site/get/{uuid}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> get(@PathVariable UUID uuid) {
		if(cu.getSite() == null) 
			return new ResponseEntity<>(Response.error("No site loaded!"), HttpStatus.NOT_FOUND);
		AbstractBacomaObject<?> obj = cu.getObject(uuid);
		return ResponseEntity.ok(Response.ok(obj));
	}
}