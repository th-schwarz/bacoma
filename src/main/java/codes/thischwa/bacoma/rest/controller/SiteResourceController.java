package codes.thischwa.bacoma.rest.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.pojo.site.CascadingStyleSheet;
import codes.thischwa.bacoma.rest.model.pojo.site.OtherResource;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
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
@RequestMapping(value = "/site")
public class SiteResourceController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/resource/static", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getStaticResource(@RequestParam String path) {
		logger.debug("enterded #getStaticResource, path={}", path);
		Path resource = fileSystemUtil.getDataDir(getProperty("site.dir.staticresource"), path);
		if (!Files.exists(resource)) {
			return new ResponseEntity<>(Response.error("Resource not found!"), HttpStatus.NOT_FOUND);
		}
		MediaType mt = ServletUtil.parseMediaType(path);
		try {
			return ResponseEntity.ok().contentLength(Files.size(resource)).contentType(mt)
					.body(new InputStreamResource(Files.newInputStream(resource)));
		} catch (IOException e) {
			logger.error("Error while fetching a static resource.", e);
			return new ResponseEntity<>(Response.error("Error while fetching a static resource."), HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/resource/css", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getNamedCss(@RequestParam String name) {
		logger.debug("enterded #getNamedCss, name={}", name);
		CascadingStyleSheet css = BoInfo.getNamedCascadingStyleSheet(cu.getSite(), name);
		byte[] cssContent = css.getText().getBytes(getDefaultCharset());
		return ResponseEntity.ok().contentLength(cssContent.length).contentType(ServletUtil.MEDIATYPE_TEXT_CSS)
				.body(new ByteArrayResource(cssContent));
	}

	@RequestMapping(value = "/resource/other", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getNamedOther(@RequestParam String name) {
		logger.debug("enterded #getNamedOther, name={}", name);
		OtherResource or = BoInfo.getNamedOtherResource(cu.getSite(), name);
		byte[] orContent = or.getText().getBytes(getDefaultCharset());
		MediaType mt = ServletUtil.parseMediaType(name);

		return ResponseEntity.ok().contentLength(orContent.length).contentType(mt)
				.body(new ByteArrayResource(orContent));
	}
}
