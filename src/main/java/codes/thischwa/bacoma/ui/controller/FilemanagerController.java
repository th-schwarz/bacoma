package codes.thischwa.bacoma.ui.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import codes.thischwa.c5c.Constants;
import codes.thischwa.c5c.UserObjectProxy;
import codes.thischwa.c5c.filemanager.FilemanagerConfig;

/**
 * Filter for serving all filemanager files except the configuration files.
 */
@Controller
public class FilemanagerController {
	private static final String BASEURL = "/filemanager/";
	private static Logger logger = LoggerFactory.getLogger(FilemanagerController.class);

	@RequestMapping(value = BASEURL + "scripts/*", method = RequestMethod.GET)
	public void handleScripts(HttpServletRequest req, HttpServletResponse resp) {
		String path = ServletUriComponentsBuilder.fromRequest(req).build().getPath();
		if(path.contains("filemanager.config")) {
			// set some default headers 
			//ConnectorServlet.initResponseHeader(resp);
			logger.debug("Filemanager config request: {}", path);
			FilemanagerConfig config = (path.endsWith(".default.json")) ? UserObjectProxy.getFilemanagerDefaultConfig()
					: UserObjectProxy.getFilemanagerUserConfig(req);

			ObjectMapper mapper = new ObjectMapper();
			OutputStream out = null;
			try {
				out = resp.getOutputStream();
				mapper.writeValue(out, config);
			} catch (Exception e) {
				logger.error(String.format("Handling of '%s' failed.", path), e);
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(out);
			}
		}
	}
	
	@RequestMapping(value = BASEURL + "**", method = RequestMethod.GET)
	public void handleResources(HttpServletRequest req, HttpServletResponse resp) {
		String path = ServletUriComponentsBuilder.fromRequest(req).build().getPath();
		if(!path.contains("filemanager.config.") && !path.startsWith(Constants.REQUEST_PATH_TOIGNORE)) {
			InputStream in = FilemanagerController.class.getResourceAsStream(path);
			OutputStream out = null;
			try {
				if(in == null) {
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					logger.warn("Requested path not found: {}", path);
				} else {
					out = resp.getOutputStream(); // shouldn't be flushed, because of the filter-chain
					IOUtils.copy(in,out);
				}
			} catch (IOException e) {
				logger.warn("Error while reading requested resource: ", path, e);
			} finally {
				IOUtils.closeQuietly(out);
			}
		}
	}
}
