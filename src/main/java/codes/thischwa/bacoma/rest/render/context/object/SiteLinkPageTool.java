package codes.thischwa.bacoma.rest.render.context.object;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.Constants;
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
		if(siteManager.getViewMode() == ViewMode.EXPORT)
			throw new UnsupportedOperationException();
		
		return Constants.LINK_SITE_PAGE.replace("{uuid}", page.getId().toString());
	}
}
