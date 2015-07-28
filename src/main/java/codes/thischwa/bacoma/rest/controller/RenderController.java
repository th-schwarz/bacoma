package codes.thischwa.bacoma.rest.controller;

import java.io.StringWriter;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import codes.thischwa.bacoma.rest.exception.IsNotARenderableException;
import codes.thischwa.bacoma.rest.model.IRenderable;
import codes.thischwa.bacoma.rest.model.InstanceUtil;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.render.VelocityRenderer;
import codes.thischwa.bacoma.rest.render.ViewMode;

@Controller
@RequestMapping(value = "/site")
public class RenderController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private VelocityRenderer renderer;
	
	@RequestMapping(value = "/render/get/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> get(@PathVariable UUID uuid) {
		logger.debug("entered #get: {}", uuid);
		AbstractBacomaObject<?> bo = cu.getObject(uuid);
		if(!InstanceUtil.isRenderable(bo))
			throw new IsNotARenderableException(getSite(), uuid);
		IRenderable renderable = (IRenderable) bo;
		StringWriter contentWriter = new StringWriter();
		renderer.render(contentWriter, renderable, ViewMode.PREVIEW, null);
		byte[] content = contentWriter.toString().getBytes(getDefaultCharset());
		contentWriter.flush();
		IOUtils.closeQuietly(contentWriter);
		
		return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).contentLength(content.length)
				.body(new ByteArrayResource(content));
	}

}
