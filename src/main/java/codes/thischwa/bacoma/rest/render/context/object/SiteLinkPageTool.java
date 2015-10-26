package codes.thischwa.bacoma.rest.render.context.object;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.IRenderable;
import codes.thischwa.bacoma.rest.model.InstanceUtil;
import codes.thischwa.bacoma.rest.model.OrderableInfo;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.render.ViewMode;
import codes.thischwa.bacoma.rest.service.SiteManager;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class SiteLinkPageTool {

	@Autowired
	private SiteManager siteManager;

	private Page page;
	
	private IRenderable renderable;

	public void setRenderable(IRenderable renderable) {
		this.renderable = renderable;
	}

	public SiteLinkPageTool set(Page page) {
		this.page = page;
		return this;
	}

	public SiteLinkPageTool set(Level level) {
		this.page = level.getPages().get(0);
		return this;
	}

	@Override
	public String toString() {
		if(siteManager.getViewMode() == ViewMode.EXPORT) {
			if(renderable == null)
				throw new IllegalArgumentException("Current renderable not set!");
			if(!InstanceUtil.isPage(page)) {
				throw new UnsupportedOperationException();
			}
			
			String pageName;
			if(BoInfo.isWelcomePage(page) || OrderableInfo.isFirst(page)) 
				pageName = siteManager.getSiteConfig().get("site.export.file.welcome");
			else
				pageName = page.getName().concat(".").concat(siteManager.getSiteConfig().get("site.export.file.extension"));
			
			Page currentPage = (Page)renderable;
			String levelPath = ToolHelperUtilities.getURLRelativePathToLevel(currentPage.getParent(), page.getParent());
			return levelPath.concat(pageName);
		}

		return LinkBuilder.buildPreviewLink(page);
	}
}
