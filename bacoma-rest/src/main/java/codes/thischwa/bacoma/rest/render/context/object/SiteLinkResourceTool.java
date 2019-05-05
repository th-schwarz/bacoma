package codes.thischwa.bacoma.rest.render.context.object;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.Constants;
import codes.thischwa.bacoma.model.BoInfo;
import codes.thischwa.bacoma.model.IRenderable;
import codes.thischwa.bacoma.model.pojo.site.AbstractSiteResource;
import codes.thischwa.bacoma.model.pojo.site.Page;
import codes.thischwa.bacoma.model.pojo.site.SiteResourceType;
import codes.thischwa.bacoma.rest.SiteConfiguration;
import codes.thischwa.bacoma.rest.render.ViewMode;
import codes.thischwa.bacoma.rest.service.SiteManager;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class SiteLinkResourceTool implements Constants {

	@Autowired
	private SiteManager siteManager;
	
	@Autowired
	private SiteConfiguration siteConfiguration;
	
	private AbstractSiteResource resource;
	
	private IRenderable renderable;
	
	public void setRenderable(IRenderable renderable) {
		this.renderable = renderable;
	}

	public SiteLinkResourceTool setCss(String name) {
		resource = BoInfo.getNamedCascadingStyleSheet(siteManager.getSite(), name);
		return this;
	}

	public SiteLinkResourceTool setOther(String name) {
		resource = BoInfo.getNamedOtherResource(siteManager.getSite(), name);
		return this;
	}

	@Override
	public String toString() {
		if(siteManager.getViewMode() == ViewMode.EXPORT) {
			if(renderable == null)
				throw new IllegalArgumentException("Current renderable not set!");
			String resourceDir;
			Map<String, String> config = siteConfiguration.getSite();
			switch(resource.getResourceType()) {
				case CSS:
					resourceDir = config.get(KEY_EXPORT_DIR_RESOURCES_CSS);
					break;
				case OTHER:
					resourceDir = config.get(KEY_EXPORT_DIR_RESOURCES_OTHER);
					break;
				default:
					throw new IllegalArgumentException(
							String.format("Illegal resource-type in this context: %s", resource.getResourceType().toString()));
			}
			String name = resource.getName();
			Page currentPage = (Page)renderable;
			String levelPath = ContextObjectUtilities.getURLRelativePathToRoot(currentPage.getParent());
			return levelPath.concat(resourceDir).concat("/").concat(name);
		}
		
		SiteResourceType type = resource.getResourceType();
		if(type != SiteResourceType.CSS && type != SiteResourceType.OTHER) {
			throw new IllegalArgumentException(
						String.format("Illegal resource-type in this context: %s", type.toString()));
		}
		return LinkBuilder.buildPreviewLink(resource);
	}
}
