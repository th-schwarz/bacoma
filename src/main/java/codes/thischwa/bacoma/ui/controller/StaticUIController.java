package codes.thischwa.bacoma.ui.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import codes.thischwa.bacoma.rest.util.ServletUtil;

@Controller
public class StaticUIController {

	private static final String BASEURL = "/ui/";

	private static final Logger logger = LoggerFactory.getLogger(StaticUIController.class);

	@RequestMapping(value = BASEURL + "*", method = RequestMethod.GET)
	public void handle(HttpServletRequest req, HttpServletResponse resp) {
		String path = ServletUriComponentsBuilder.fromRequest(req).build().getPath();
		try {
			ServletUtil.write(path, resp);
		} catch (IOException e) {
			logger.warn("Error while reading requested resource: {}", path, e);
		}
	}
}
