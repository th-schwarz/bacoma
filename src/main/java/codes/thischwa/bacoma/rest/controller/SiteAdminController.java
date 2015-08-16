package codes.thischwa.bacoma.rest.controller;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
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
import org.springframework.web.bind.annotation.RequestPart;

import codes.thischwa.bacoma.rest.model.pojo.requestcycle.GenericRequestSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqLevel;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqPage;
import codes.thischwa.bacoma.rest.model.pojo.requestcycle.ReqTemplate;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.util.ServletUtil;

@Controller
@MultipartConfig(location = ServletContext.TEMPDIR)
public class SiteAdminController extends AbstractController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value="/load/{siteUrl}", method = RequestMethod.GET)
	public ResponseEntity<Response> load(@PathVariable String siteUrl) {
		try {
			cu.load(siteUrl);
		} catch (IOException e) {
			logger.error("Error while loading a site.", e);
			return new ResponseEntity<>(Response.error("Site not found!"), HttpStatus.NOT_FOUND);
		}
		logger.info("site successful loaded: {}", siteUrl);
		
		String baseUrl = ServletUtil.getBaseUrl();
		logger.info("****** base-url: {}", baseUrl);
		return ResponseEntity.ok(Response.ok());
	}
	
	@RequestMapping(value="/getAll", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> getAll() {
		logger.debug("entered #getSites");
		Path userDir = fileSystemUtil.getDataDir();		
		List<String> sites = new ArrayList<>();
		try {
			for(Path f : Files.newDirectoryStream(userDir, new DirectoryStream.Filter<Path>() {
				@Override
				public boolean accept(Path entry) throws IOException {
					return Files.isRegularFile(entry) && entry.toString().toLowerCase().endsWith(".json");
				}
				})) {
				sites.add(FilenameUtils.getBaseName(f.getFileName().toString()));			
			}
		} catch (IOException e) {
			logger.error("Error while getting all sites.", e);
			return new ResponseEntity<>(Response.error("Error while getting all sites."), HttpStatus.CONFLICT);
		}
		return ResponseEntity.ok(Response.ok(sites));
	}
	

	@RequestMapping(value="/getAllStaticResources", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> getAllStaticResources() {
		Path resourceFolder = fileSystemUtil.getStaticResourceDir();
		List<String> resources = new ArrayList<>();
		try {
			for(Path p : Files.newDirectoryStream(resourceFolder, new DirectoryStream.Filter<Path>() {
				@Override
				public boolean accept(Path entry) throws IOException {
					return Files.isRegularFile(entry) && !Files.isHidden(entry);
				}
			})) {
				resources.add(p.getFileName().toString());
			}
			return ResponseEntity.ok(Response.ok(resources));
		} catch (IOException e) {
			logger.error("Error while getting static resources.", e);
			return new ResponseEntity<>(Response.error("Error while getting static resources."), HttpStatus.CONFLICT);
		}
	}
	
	@RequestMapping(value="/setConfiguration", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> setConfiguration(@RequestBody Map<String, String> config) {
		cu.setConfiguration(config);
		cu.persist();
		return ResponseEntity.ok(Response.ok());
	}

	@RequestMapping(value="/setLayoutTemplate", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> setLayoutTemplate(@RequestBody String text) {
		if(StringUtils.isEmpty(text))
			return new ResponseEntity<>(Response.error("Empty request!"), HttpStatus.BAD_REQUEST);
		UUID id = cu.setLayoutTemplate(text);
		cu.persist();
		return ResponseEntity.ok(Response.ok(id));
	}

	@RequestMapping(value="/addResource", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> addResource(@RequestBody GenericRequestSiteResource siteResource) {
		if(!siteResource.isValid())
			return new ResponseEntity<>(Response.error("Request is incomplete"), HttpStatus.BAD_REQUEST);
		cu.addResource(siteResource);
		cu.persist();
		return ResponseEntity.ok(Response.ok(siteResource.getId()));
	}
	
	@RequestMapping(value="/addStaticResource", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> addStaticResource(@RequestPart("file") Part file) {
		String originalFileName = file.getSubmittedFileName();
    	if(originalFileName != null) {
            try {
            	String fileName = fileSystemUtil.saveStaticSiteResource(originalFileName, file.getInputStream());
                logger.debug("Static resource added successful: {}", fileName);
            	return ResponseEntity.ok(Response.ok(fileName));
            } catch (Exception e) {
                return ResponseEntity.ok(Response.error(e.getMessage()));
            }
        } else {
            return ResponseEntity.ok(Response.error("Empty file."));
        }
	}
	
	@RequestMapping(value="/addTemplate", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> addTemplate(@RequestBody ReqTemplate template) {
		if(!template.isValid())
			return new ResponseEntity<>(Response.error("Request is incomplete"), HttpStatus.BAD_REQUEST);
		cu.addTemplate(template);
		cu.persist();
		return ResponseEntity.ok(Response.ok(template.getId()));
	}
	
	@RequestMapping(value="/addLevel", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> addLevel(@RequestBody ReqLevel level) {
		if(!level.isValid())
			return new ResponseEntity<>(Response.error("Request is incomplete"), HttpStatus.BAD_REQUEST);
		cu.addLevel(level);
		cu.persist();
		return ResponseEntity.ok(Response.ok(level.getId()));
	}

	@RequestMapping(value="/addPage", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> addPage(@RequestBody ReqPage page) {
		if(!page.isValid())
			return new ResponseEntity<>(Response.error("Request is incomplete"), HttpStatus.BAD_REQUEST);
		cu.addPage(page);
		cu.persist();
		return ResponseEntity.ok(Response.ok(page.getId()));
	}
	
	@RequestMapping(value="/get/{uuid}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> get(@PathVariable UUID uuid) {
		AbstractBacomaObject<?> obj = cu.getObject(uuid);
		return ResponseEntity.ok(Response.ok(obj));
	}
	
	@RequestMapping(value="/get", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> getCurrent() {
		return ResponseEntity.ok(Response.ok(getSite()));
	}

	@RequestMapping(value="/remove/{uuid}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> remove(@PathVariable UUID uuid) {
		cu.remove(uuid);
		return ResponseEntity.ok(Response.ok());
	}
	
	@RequestMapping(value="/removeStaticResource/{name}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> removeStaticResource(@PathVariable String name) {
		Path resourceFolder = fileSystemUtil.getStaticResourceDir();
		Path staticResource = resourceFolder.resolve(name);
		if(!Files.exists(staticResource))
			return new ResponseEntity<>(Response.error("File not found!"), HttpStatus.BAD_REQUEST);
		try {
			Files.delete(staticResource);
			logger.debug("Static resource successful deleted: {}", staticResource.toString());
		} catch (IOException e) {
			logger.error("Error while deleting static resources.", e);
			return new ResponseEntity<>(Response.error("Error while deleting a static resources."), HttpStatus.CONFLICT);
		}
		return ResponseEntity.ok(Response.ok());
	}
}