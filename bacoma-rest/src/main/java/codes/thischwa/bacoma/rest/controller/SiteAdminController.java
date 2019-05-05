package codes.thischwa.bacoma.rest.controller;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import codes.thischwa.bacoma.Constants;
import codes.thischwa.bacoma.model.pojo.requestcycle.GenericRequestSiteResource;
import codes.thischwa.bacoma.model.pojo.requestcycle.ReqLevel;
import codes.thischwa.bacoma.model.pojo.requestcycle.ReqPage;
import codes.thischwa.bacoma.model.pojo.requestcycle.ReqTemplate;
import codes.thischwa.bacoma.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.AbstractController;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SiteAdminController extends AbstractController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<Response> getAll() {
		logger.debug("entered #getAll");
		Path userDir = fileSystemUtil.getDataDir();
		List<String> sites = new ArrayList<>();
		try {
			for(Path f : Files.newDirectoryStream(userDir, new DirectoryStream.Filter<Path>() {
				@Override
				public boolean accept(Path entry) throws IOException {
					return Files.isRegularFile(entry) && !Files.isHidden(entry) && entry.toString().toLowerCase().endsWith(".json");
				}
			})) {
				sites.add(FilenameUtils.getBaseName(f.getFileName().toString()));
			}
		} catch (IOException e) {
			logger.error("Error while getting all sites.", e);
			return Response.error("Error while getting all sites.", HttpStatus.CONFLICT);
		}
		return Response.ok(sites);
	}

//	@RequestMapping(value = Constants.BASEURL_REST
//			+ "/setConfiguration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Response> setConfiguration(@PathVariable String siteUrl, @RequestBody Map<String, String> config) {
//		logger.debug("entered #setConfiguration");
//		cu.setConfiguration(siteUrl, config);
//		return Response.ok();
//	}
//
//	@RequestMapping(value = Constants.BASEURL_REST
//			+ "/getConfiguration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Response> getConfiguration(@PathVariable String siteUrl) {
//		logger.debug("entered #setConfiguration");
//		Map<String, String> config = cu.getConfiguration(siteUrl);
//		return Response.ok(config);
//	}

	@RequestMapping(value = Constants.BASEURL_REST
			+ "/setLayoutTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> setLayoutTemplate(@PathVariable String siteUrl, @RequestBody String text) {
		logger.debug("entered #setLayoutTemplate");
		if(StringUtils.isEmpty(text))
			return Response.error("Empty request!", HttpStatus.BAD_REQUEST);
		UUID id = cu.setLayoutTemplate(siteUrl, text);
		return Response.ok(id);
	}

	@RequestMapping(value = Constants.BASEURL_REST
			+ "/addResource", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addResource(@PathVariable String siteUrl, @RequestBody GenericRequestSiteResource siteResource) {
		if(!siteResource.isValid())
			return new ResponseEntity<>(Response.error("Request is incomplete"), HttpStatus.BAD_REQUEST);
		cu.addResource(siteUrl, siteResource);
		return Response.ok(siteResource.getId());
	}

	@RequestMapping(value = Constants.BASEURL_REST
			+ "/static/add", method = RequestMethod.POST)
	public ResponseEntity<?> addStaticResource(@PathVariable String siteUrl, @RequestPart("file") Part file, @RequestParam String path) {
		String originalFileName = file.getSubmittedFileName();
		if(originalFileName != null) {
			try {
				String fileName = fileSystemUtil.addStaticResource(getSite(siteUrl), path, file.getInputStream());
				logger.debug("Static resource added successful: {}", fileName);
				return Response.ok(fileName);
			} catch (Exception e) {
				return Response.error(e.getMessage());
			}
		} else {
			return Response.error("Empty file.");
		}
	}

	@RequestMapping(value = Constants.BASEURL_REST
			+ "/static/remove", method = RequestMethod.GET)
	public ResponseEntity<Response> removeStaticResource(@PathVariable String siteUrl, @RequestParam String path) {
		try {
			Path staticResource = fileSystemUtil.removeStaticResource(getSite(siteUrl), path);
			logger.debug("Static resource successful deleted: {}", staticResource.toString());
		} catch (IOException e) {
			logger.error("Error while deleting static resources.", e);
			return Response.error("Error while deleting a static resources.", HttpStatus.CONFLICT);
		}
		return Response.ok();
	}

	@RequestMapping(value = Constants.BASEURL_REST
			+ "/addTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addTemplate(@PathVariable String siteUrl, @RequestBody ReqTemplate template) {
		if(!template.isValid())
			return new ResponseEntity<>(Response.error("Request is incomplete"), HttpStatus.BAD_REQUEST);
		cu.addTemplate(siteUrl, template);
		return Response.ok(template.getId());
	}

	@RequestMapping(value = Constants.BASEURL_REST
			+ "/addLevel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> addLevel(@PathVariable String siteUrl, @RequestBody ReqLevel level) {
		if(!level.isValid())
			return Response.error("Request is incomplete", HttpStatus.BAD_REQUEST);
		cu.addLevel(siteUrl, level);
		return Response.ok(level.getId());
	}

	@RequestMapping(value = Constants.BASEURL_REST
			+ "/addPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> addPage(@PathVariable String siteUrl, @RequestBody ReqPage page) {
		if(!page.isValid())
			return Response.error("Request is incomplete", HttpStatus.BAD_REQUEST);
		cu.addPage(siteUrl, page);
		return Response.ok(page.getId());
	}

	@RequestMapping(value = Constants.BASEURL_REST + "/get/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<Response> get(@PathVariable String siteUrl, @PathVariable UUID uuid) {
		AbstractBacomaObject<?> obj = cu.getObject(siteUrl, uuid);
		return Response.ok(obj);
	}

	@RequestMapping(value = Constants.BASEURL_REST + "/get", method = RequestMethod.GET)
	public ResponseEntity<Response> getCurrent(@PathVariable String siteUrl) {
		return Response.ok(getSite(siteUrl));
	}

	@RequestMapping(value = Constants.BASEURL_REST + "/remove/{uuid}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> remove(@PathVariable String siteUrl, @PathVariable UUID uuid) {
		cu.remove(siteUrl, uuid);
		return Response.ok();
	}
}