package codes.thischwa.bacoma.rest.render.context.object;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.Constants;
import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.IRenderable;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.render.ViewMode;
import codes.thischwa.bacoma.rest.service.ConfigurationHolder;
import codes.thischwa.bacoma.rest.service.SiteManager;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class SiteLinkResourceTool {

	@Autowired
	private SiteManager siteManager;

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
			switch(resource.getResourceType()) {
				case CSS:
					resourceDir = siteManager.getSiteConfig().get(ConfigurationHolder.KEY_EXPORT_DIR_RESOURCES_CSS);
					break;
				case OTHER:
					resourceDir = siteManager.getSiteConfig().get(ConfigurationHolder.KEY_EXPORT_DIR_RESOURCES_OTHER);
					break;
				default:
					throw new IllegalArgumentException(
							String.format("Illegal resourcee-type in this context: %s", resource.getResourceType().toString()));
			}
			String name = resource.getName();
			Page currentPage = (Page)renderable;
			String levelPath = ToolHelperUtilities.getURLRelativePathToRoot(currentPage.getParent());
			return levelPath.concat(resourceDir).concat("/").concat(name);
		}
		
		switch(resource.getResourceType()) {
			case CSS:
				return Constants.LINK_SITE_CSS.replace("{name}", resource.getName());
			case OTHER:
				return Constants.LINK_SITE_OTHER.replace("{name}", resource.getName());
			default:
				throw new IllegalArgumentException(
						String.format("Illegal resourcee-type in this context: %s", resource.getResourceType().toString()));
		}
	}
}
