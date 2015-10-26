package codes.thischwa.bacoma.rest.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import codes.thischwa.bacoma.rest.exception.ResourceNotFoundException;
import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.pojo.site.CascadingStyleSheet;
import codes.thischwa.bacoma.rest.model.pojo.site.OtherResource;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.model.pojo.site.SiteResourceType;
import codes.thischwa.bacoma.rest.util.EnumUtil;
import codes.thischwa.bacoma.rest.util.ServletUtil;

/**
 * Controller which serves all resources of a {@link Site}, which are required
 * for the final rendered site. Resources are:
 * <ul>
 * <li>static resources: files in the file system like javascript, additional
 * css, images (won't be rendered)
 * <li>css from a {@link Site}-object
 * <li>other text-base resources from a {@link Site}-object
 * </ul>
 */
@Controller
public class SiteResourceController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = BASEURL + "/resource/static", method = RequestMethod.GET)
	public ResponseEntity<?> getStaticResource(@PathVariable String siteUrl, @RequestParam String path) {
		logger.debug("enterded #getStaticResource, url={}, path={}", siteUrl, path);
		Site site = getSite(siteUrl);
		Path resource = fileSystemUtil.getSitesDir(site, getProperty(site, "site.dir.staticresource"), path);
		if (!Files.exists(resource)) {
			throw new ResourceNotFoundException(site, path);
		}
		MediaType mt = ServletUtil.parseMediaType(path);
		try {
			return ResponseEntity.ok().contentLength(Files.size(resource)).contentType(mt)
					.body(new InputStreamResource(Files.newInputStream(resource)));
		} catch (IOException e) {
			logger.error("Error while fetching a static resource.", e);
			return ResponseEntity.ok(Response.error("Error while fetching a static resource."));
		}
	}

	@RequestMapping(value = BASEURL + "/resource/{type}", method = RequestMethod.GET, params = {"name"})
	public ResponseEntity<?> getSiteResource(@PathVariable String siteUrl, @PathVariable String type, @RequestParam("name") String name) {
		logger.debug("enterded #getSiteResource: url={}, type={}, name={}", siteUrl, type, name);
		Site site = getSite(siteUrl);
		SiteResourceType resourceType = EnumUtil.valueOfIgnoreCase(SiteResourceType.class, type);
		byte[] content;
		switch(resourceType) {
			case CSS: 
				CascadingStyleSheet css = BoInfo.getNamedCascadingStyleSheet(site, name);
				content = css.getText().getBytes(getDefaultCharset(site));
				break;
			case OTHER:
				OtherResource or = BoInfo.getNamedOtherResource(site, name);
				content = or.getText().getBytes(getDefaultCharset(site));
				break;
			default:
				throw new IllegalArgumentException(String.format("Type not allowed in this context: ", type));
		}
		MediaType mt = ServletUtil.parseMediaType(name);

		return ResponseEntity.ok().contentLength(content.length).contentType(mt)
				.body(new ByteArrayResource(content));
	}
}
