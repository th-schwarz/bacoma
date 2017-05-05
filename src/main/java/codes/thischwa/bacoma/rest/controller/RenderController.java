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
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
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
	
	@RequestMapping(value = BASEURL + "/render/get/{viewMode}/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> get(@PathVariable String siteUrl, @PathVariable String viewMode, @PathVariable UUID uuid) {
		logger.debug("entered #get: url={}, ViewMode={}, UUID={}", siteUrl, viewMode, uuid);
		ViewMode vw = EnumUtil.valueOfIgnoreCase(ViewMode.class, viewMode);
		if(vw == ViewMode.EXPORT)
			throw new IllegalArgumentException("'export' isn't allowed in this context!");
		Site site = getSite(siteUrl);
		AbstractBacomaObject<?> bo = cu.getObject(siteUrl, uuid);
		byte[] content;
		MediaType mediaType;
		if(InstanceUtil.isRenderable(bo)) {
			IRenderable renderable = (IRenderable) bo;
			content = render(site, renderable, vw);
			mediaType = MediaType.TEXT_HTML;
		} else if(InstanceUtil.isSiteResource(bo)) {
			AbstractSiteResource res = (AbstractSiteResource) bo;
			content = render(site, res, vw);
			mediaType = ServletUtil.parseMediaType(res.getName());
		} else 
			throw new IllegalArgumentException("Unkwon object for rendering!");
		
		return ResponseEntity.ok().contentType(mediaType).contentLength(content.length)
				.body(new ByteArrayResource(content));
	}

	private byte[] render(Site site, IRenderable renderable, ViewMode viewMode) {
		StringWriter contentWriter = new StringWriter();
		renderer.render(contentWriter, renderable, viewMode, null);
		byte[] content = contentWriter.toString().getBytes(getDefaultCharset(site));
		contentWriter.flush();
		IOUtils.closeQuietly(contentWriter);
		return content;
	}
	
	private byte[] render(Site site, AbstractSiteResource siteResource, ViewMode viewMode) {
		byte[] content;
		String name = siteResource.getName();
		SiteResourceType type = siteResource.getResourceType();
		switch(type) {
			case CSS: 
				CascadingStyleSheet css = BoInfo.getNamedCascadingStyleSheet(site, name);
				content = css.getText().getBytes(getDefaultCharset(site));
				break;
			case OTHER:
				OtherResource or = BoInfo.getNamedOtherResource(site, name);
				content = or.getText().getBytes(getDefaultCharset(site));
				break;
			default:
				throw new IllegalArgumentException(String.format("Type not allowed in this context: ", type));
		}
		return content;
	}
}
