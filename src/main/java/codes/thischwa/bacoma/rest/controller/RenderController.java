package codes.thischwa.bacoma.rest.controller;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import org.springframework.web.util.UriComponentsBuilder;

import codes.thischwa.bacoma.Constants;
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
import codes.thischwa.bacoma.rest.service.ConfigurationHolder;
import codes.thischwa.bacoma.rest.util.EnumUtil;
import codes.thischwa.bacoma.rest.util.ServletUtil;

@Controller
public class RenderController extends AbstractRestController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private VelocityRenderer renderer;
		
	@RequestMapping(value = BASEURL + "/render/get/{viewMode}/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> get(@PathVariable String siteUrl, @PathVariable String viewMode, @PathVariable UUID uuid) {
		logger.debug("entered #get: url={}, ViewMode={}, UUID={}", siteUrl, viewMode, uuid);
		ViewMode vm = EnumUtil.valueOfIgnoreCase(ViewMode.class, viewMode);
		if(vm == ViewMode.EXPORT)
			throw new IllegalArgumentException("'export' isn't allowed in this context!");
		Site site = getSite(siteUrl);
		AbstractBacomaObject<?> bo = cu.getObject(siteUrl, uuid);
		byte[] content;
		MediaType mediaType;
		if(InstanceUtil.isRenderable(bo)) {
			IRenderable renderable = (IRenderable) bo;
			content = render(site, renderable, vm);
			mediaType = MediaType.TEXT_HTML;
		} else if(InstanceUtil.isSiteResource(bo)) {
			AbstractSiteResource res = (AbstractSiteResource) bo;
			content = render(site, res, vm);
			mediaType = ServletUtil.parseMediaType(res.getName());
		} else 
			throw new IllegalArgumentException("Unkwon object for rendering!");
		
		return ResponseEntity.ok().contentType(mediaType).contentLength(content.length)
				.body(new ByteArrayResource(content));
	}

	private byte[] render(Site site, IRenderable renderable, ViewMode viewMode) {
		StringWriter contentWriter = new StringWriter();
		renderer.render(contentWriter, renderable, viewMode, null);
		String contentStr = contentWriter.toString();
		if(viewMode == ViewMode.PREVIEW) {
			contentStr = fixLinksForPreview(site, contentStr);
		}
		byte[] content = contentStr.getBytes(getDefaultCharset(site));
		contentWriter.flush();
		IOUtils.closeQuietly(contentWriter);
		return content;
	}
	
	String fixLinksForPreview(Site site, String fullContent) {
		try {
			String resourceDir = "/".concat(getProperty(site, ConfigurationHolder.KEY_EXPORT_DIR_RESOURCES_STATIC));
			Document doc = Jsoup.parse(fullContent);
			for(Element aEl : doc.select("a[href]")) {
				String href = aEl.attr("href");
				if(href.startsWith(resourceDir)) {
					String path = UriComponentsBuilder.fromUri(new URI(href)).build().getQueryParams().getFirst("path");
					String newHref = buildSiteResourceLink(site, path);
					aEl.attr("href", newHref);
				}
			}
			for(Element aEl : doc.select("img[src]")) {
				String href = aEl.attr("src");
				if(href.startsWith(resourceDir)) {
					String path = UriComponentsBuilder.fromUri(new URI(href)).build().getQueryParams().getFirst("path");
					String newSrc = buildSiteResourceLink(site, path);
					aEl.attr("src", newSrc);
				}
			}
			return doc.toString();
		} catch (URISyntaxException e) {
			logger.warn("Error while preview-filtering", e);
			return fullContent;
		}
	}

    String buildSiteResourceLink(Site site, String path) {
    	String link = Constants.LINK_SITE_STATICRESOURCE.replace("{siteUrl}", site.getUrl()).replace("{path}", path);
    	return link;
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
