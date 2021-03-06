package codes.thischwa.bacoma.ui.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import codes.thischwa.bacoma.model.util.ServletUtil;

/**
 * Serves static resources.
 */
@Controller
public class StaticUIController {
	private static final Logger logger = LoggerFactory.getLogger(StaticUIController.class);
	
	@Value("${dir.webapp}")
	private String baseDir;

	@RequestMapping(value = "/ui/**", method = RequestMethod.GET)
	public void handleClassPathResource(HttpServletRequest req, HttpServletResponse resp) {
		String path = ServletUriComponentsBuilder.fromRequest(req).build().getPath();
		try {
			ServletUtil.write(path, resp);
		} catch (IOException e) {
			logger.warn("Error while reading requested resource: {}", path, e);
		}
	}

	@RequestMapping(value = "/webapp/**", method = RequestMethod.GET)
	public void handleWebappResource(HttpServletRequest req, HttpServletResponse resp) {
		String urlPath = ServletUriComponentsBuilder.fromRequest(req).build().getPath();
		urlPath = urlPath.substring("/webapp/".length());
		Path reqPath = Paths.get(baseDir, urlPath);
		try {
			ServletUtil.write(reqPath, resp);
		} catch (IOException e) {
			logger.warn("Error while reading requested resource: {}", urlPath, e);
		}
	}
}
