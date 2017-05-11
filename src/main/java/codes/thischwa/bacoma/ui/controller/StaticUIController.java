package codes.thischwa.bacoma.ui.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class StaticUIController {

	private static final String BASEURL = "/ui/";

	private static final Logger logger = LoggerFactory.getLogger(StaticUIController.class);

	@RequestMapping(value = BASEURL + "*", method = RequestMethod.GET)
	public void handle(HttpServletRequest req, HttpServletResponse resp) {

		String path = ServletUriComponentsBuilder.fromRequest(req).build().getPath();
		Resource requestedResource = new ClassPathResource(path);
		if(!requestedResource.exists()) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.warn("Requested path not found: {}", path);
			return;
		}
		OutputStream out = null;
		try {
			InputStream in = requestedResource.getInputStream();
			out = resp.getOutputStream();
			FileCopyUtils.copy(in, out);
		} catch (IOException e) {
			logger.warn("Error while reading requested resource: ", path, e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
}
