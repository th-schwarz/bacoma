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

import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.IRenderable;
import codes.thischwa.bacoma.rest.model.InstanceUtil;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.site.CascadingStyleSheet;
import codes.thischwa.bacoma.rest.model.pojo.site.OtherResource;
import codes.thischwa.bacoma.rest.model.pojo.site.SiteResourceType;
import codes.thischwa.bacoma.rest.render.VelocityRenderer;
import codes.thischwa.bacoma.rest.render.ViewMode;
import codes.thischwa.bacoma.rest.util.EnumUtil;
import codes.thischwa.bacoma.rest.util.ServletUtil;

@Controller
public class RenderController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private VelocityRenderer renderer;
	
	@RequestMapping(value = "/render/get/{viewMode}/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> get(@PathVariable String viewMode, @PathVariable UUID uuid) {
		logger.debug("entered #get: ViewMode={}, UUID={}", viewMode, uuid);
		ViewMode vw = EnumUtil.valueOfIgnoreCase(ViewMode.class, viewMode);
		if(vw == ViewMode.EXPORT)
			throw new IllegalArgumentException("'export' isn't allowed in this context!");
		AbstractBacomaObject<?> bo = cu.getObject(uuid);
		byte[] content;
		MediaType mediaType;
		if(InstanceUtil.isRenderable(bo)) {
			IRenderable renderable = (IRenderable) bo;
			content = render(renderable, vw);
			mediaType = MediaType.TEXT_HTML;
		} else if(InstanceUtil.isSiteResource(bo)) {
			AbstractSiteResource res = (AbstractSiteResource) bo;
			content = render(res, vw);
			mediaType = ServletUtil.parseMediaType(res.getName());
		} else 
			throw new IllegalArgumentException("Unkwon object for rendering!");
		
		return ResponseEntity.ok().contentType(mediaType).contentLength(content.length)
				.body(new ByteArrayResource(content));
	}

	private byte[] render(IRenderable renderable, ViewMode viewMode) {
		StringWriter contentWriter = new StringWriter();
		renderer.render(contentWriter, renderable, viewMode, null);
		byte[] content = contentWriter.toString().getBytes(getDefaultCharset());
		contentWriter.flush();
		IOUtils.closeQuietly(contentWriter);
		return content;
	}
	
	private byte[] render(AbstractSiteResource siteResource, ViewMode viewMode) {
		byte[] content;
		String name = siteResource.getName();
		SiteResourceType type = siteResource.getResourceType();
		switch(type) {
			case CSS: 
				CascadingStyleSheet css = BoInfo.getNamedCascadingStyleSheet(cu.getSite(), name);
				content = css.getText().getBytes(getDefaultCharset());
				break;
			case OTHER:
				OtherResource or = BoInfo.getNamedOtherResource(cu.getSite(), name);
				content = or.getText().getBytes(getDefaultCharset());
				break;
			default:
				throw new IllegalArgumentException(String.format("Type not allowed in this context: ", type));
		}
		return content;
	}
}
