package codes.thischwa.bacoma.ui.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import codes.thischwa.bacoma.Constants;
import codes.thischwa.bacoma.rest.AbstractController;
import codes.thischwa.bacoma.rest.util.ServletUtil;
import codes.thischwa.c5c.UserObjectProxy;
import codes.thischwa.c5c.filemanager.FilemanagerConfig;

/**
 * Filter for serving all filemanager files and respect the special handling of the configuration files.
 */
@Controller
public class FilemanagerController extends AbstractController {
	private static final String BASEURL = "/filemanager/";
	private static Logger logger = LoggerFactory.getLogger(FilemanagerController.class);

	@RequestMapping(value = "{siteUrl}" + BASEURL + "**", method = RequestMethod.GET)
	public void handleFM(@PathVariable String siteUrl, HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		logger.debug("entered #handleFM");
		Path resourceFolder = fileSystemUtil.getStaticResourceDir(getSite(siteUrl));
		String fmDir = resourceFolder.toString().replaceAll(Constants.FILENAME_SEPARATOR, Constants.URLPATH_SEPARATOR);
		System.setProperty(Constants.SYSPROP_DIR_FILEMANAGER, fmDir);
		String urlPath = ServletUriComponentsBuilder.fromRequest(req).build().getPath();
		urlPath = urlPath.substring(urlPath.indexOf(siteUrl) + siteUrl.length());
		if(urlPath.startsWith(BASEURL + "scripts") && urlPath.contains("filemanager.config")) {
			// set some default headers
			// ConnectorServlet.initResponseHeader(resp);
			logger.debug("Filemanager config request: {}", urlPath);
			FilemanagerConfig config = (urlPath.endsWith(".default.json")) ? UserObjectProxy.getFilemanagerDefaultConfig()
					: UserObjectProxy.getFilemanagerUserConfig(req);

			ObjectMapper mapper = new ObjectMapper();
			OutputStream out = null;
			try {
				out = resp.getOutputStream();
				mapper.writeValue(out, config);
			} catch (Exception e) {
				logger.error(String.format("Handling of '%s' failed.", urlPath), e);
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(out);
			}
		} else if(urlPath.contains(BASEURL + "connectors")) {
			RequestDispatcher fmDispatcher = req.getServletContext().getRequestDispatcher(urlPath);
			try {
				fmDispatcher.forward(req, resp);
			} catch (IOException e) {
				throw new ServletException(e);
			}
		} else {
			try {
				ServletUtil.write(urlPath, resp);
			} catch (IOException e) {
				logger.warn("Error while reading requested resource: ", urlPath, e);
			}
		}
	}
}
