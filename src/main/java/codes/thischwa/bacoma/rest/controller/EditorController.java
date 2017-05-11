package codes.thischwa.bacoma.rest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import codes.thischwa.bacoma.rest.render.VelocityRenderer;

/**
 * Manage the different web editors.
 */
@Controller
public class EditorController extends AbstractRestController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private VelocityRenderer renderer;
	
	@RequestMapping(value = BASEURL + "/editPage", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE})
	public @ResponseBody String editPage(@PathVariable String siteUrl) throws IOException {
		logger.info("#entered #editPage");
		InputStream in = null;
		StringWriter writer = new StringWriter();
		try {
			in = getClass().getResourceAsStream("/vm/sourceeditor.vm");
			IOUtils.copy(in, writer, getDefaultCharset(siteUrl));
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		
		return renderer.renderString(writer.toString(), null);
	}
	
	@RequestMapping(value="/test", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, 
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Response> test(@RequestBody Map<String, String> var) {
		logger.info("#entered #test  entries:{}", var.size());
		
		String msg = "Okay: " + var.get("mode");
		ResponseEntity<Response> resp = Response.ok(msg);
		return resp;
	}
}
