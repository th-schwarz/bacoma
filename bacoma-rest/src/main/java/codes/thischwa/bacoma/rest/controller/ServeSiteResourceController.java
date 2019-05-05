package codes.thischwa.bacoma.rest.controller;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import codes.thischwa.bacoma.exception.ResourceNotFoundException;
import codes.thischwa.bacoma.model.BoInfo;
import codes.thischwa.bacoma.model.pojo.site.AbstractSiteResource;
import codes.thischwa.bacoma.model.pojo.site.CascadingStyleSheet;
import codes.thischwa.bacoma.model.pojo.site.OtherResource;
import codes.thischwa.bacoma.model.pojo.site.Site;
import codes.thischwa.bacoma.model.pojo.site.SiteResourceType;
import codes.thischwa.bacoma.rest.AbstractController;
import codes.thischwa.bacoma.rest.Constants;
import codes.thischwa.bacoma.rest.util.EnumUtil;
import codes.thischwa.bacoma.rest.util.ServletUtil;

/**
 * Controller which serves all resources of a {@link Site}, which are required for the final rendered site. Resources are:
 * <ul>
 * <li>static resources: files in the file system like javascript, additional css, images (won't be rendered)
 * <li>css from a {@link Site}-object
 * <li>other text-base resources from a {@link Site}-object
 * </ul>
 */
@Controller
@RequestMapping(value = Constants.BASEURL_REST + "/resource")
public class ServeSiteResourceController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/static/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getAll(@PathVariable String siteUrl) {
		logger.debug("entered #getAll");
		Path resourceFolder = fileSystemUtil.getStaticResourceDir(getSite(siteUrl));
		String resDir = resourceFolder.toAbsolutePath().toString();
		final int resourceFolderLen = resDir.length();
		final List<String> resources = new ArrayList<>();
		try {
			Files.walkFileTree(resourceFolder, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if(!Files.isHidden(file)) {
						String absolutePath = file.toAbsolutePath().toString();
						String resFile = absolutePath.substring(resourceFolderLen);
						resources.add(resFile);
					}
					return FileVisitResult.CONTINUE;
				}
			});
			return Response.ok(resources);
		} catch (IOException e) {
			logger.error("Error while getting static resources.", e);
			return Response.error("Error while getting static resources.", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Serves a desired static resource (files in the file system), e.g.
	 * <tt>http://localhost:8080/site/site.test/resource/static/get?path=/sub_folder/test.js</tt>.<p>
	 * The content- / media-type depends on the extension of the 'path'.
	 * 
	 * @param siteUrl
	 *            the url of the referred site
	 * @param path
	 *            must start with '/' and could contain sub-folders
	 * @return
	 */
	@RequestMapping(value = "/static/get", method = RequestMethod.GET)
	public ResponseEntity<?> getStatic(@PathVariable String siteUrl, @RequestParam String path) {
		logger.debug("enterded #getStaticResource, url={}, path={}", siteUrl, path);
		Site site = getSite(siteUrl);
		Path resource = fileSystemUtil.getSitesDir(site, getProperty(site, "site.dir.staticresource"), path);
		if(!Files.exists(resource)) {
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
	
	/**
	 * Serves a desired {@link AbstractSiteResource} defined by its 'type' and 'name', e.g.
	 * <tt>http://localhost:8080/site/site.test/resource/css/format.css</tt>
	 * 
	 * @param siteUrl
	 *            the url of the referred site
	 * @param type
	 *            the {@link SiteResourceType} of the desired {@link AbstractSiteResource}. Only <tt>CSS</tt> and <tt>OTHER</tt> are
	 *            allowed.
	 * @param name
	 *            the name of the desired {@link AbstractSiteResource}
	 * @return
	 */
	@RequestMapping(value = "/{type}/{name}", method = RequestMethod.GET)
	public ResponseEntity<?> getSiteResource(@PathVariable String siteUrl, @PathVariable String type, @PathVariable String name) {
		logger.debug("enterded #getSiteResource: url={}, type={}, name={}", siteUrl, type, name);
		Site site = getSite(siteUrl);
		SiteResourceType resourceType = EnumUtil.valueOfIgnoreCase(SiteResourceType.class, type);
		byte[] content;
		switch(resourceType) {
			case CSS:
				CascadingStyleSheet css = BoInfo.getNamedCascadingStyleSheet(site, name);
				content = css.getText().getBytes(Constants.DEFAULT_CHARSET);
				break;
			case OTHER:
				OtherResource or = BoInfo.getNamedOtherResource(site, name);
				content = or.getText().getBytes(Constants.DEFAULT_CHARSET);
				break;
			default:
				throw new IllegalArgumentException(String.format("Type not allowed in this context: ", type));
		}
		MediaType mt = ServletUtil.parseMediaType(name);

		return ResponseEntity.ok().contentLength(content.length).contentType(mt).body(new ByteArrayResource(content));
	}
}
