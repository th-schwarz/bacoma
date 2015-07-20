package codes.thischwa.bacoma.rest.render.context.object;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.Constants;
import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.OrderableInfo;
import codes.thischwa.bacoma.rest.model.pojo.site.CascadingStyleSheet;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.render.ViewMode;
import codes.thischwa.bacoma.rest.render.context.IContextObjectNeedPojoHelper;
import codes.thischwa.bacoma.rest.render.context.IContextObjectNeedViewMode;
import codes.thischwa.bacoma.rest.render.context.PojoHelper;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

@Component("sitelinktool")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SiteLinkTool implements IContextObjectNeedPojoHelper, IContextObjectNeedViewMode {

	@Autowired
	private FileSystemUtil fileSystemUtil;
	
	private Site site;
	private Level currentLevel = null;
	private boolean isExportView;
    private PojoHelper pojoHelper; 
    private Path siteDir;
    
    private String link;

	@Override
	public void setPojoHelper(final PojoHelper pojoHelper) {
		this.pojoHelper = pojoHelper;
		site = pojoHelper.getSite();
		this.currentLevel = pojoHelper.getLevel();
		siteDir = fileSystemUtil.getSitesDir();
	}

	@Override
	public void setViewMode(final ViewMode viewMode) {
		isExportView = (viewMode == ViewMode.EXPORT);
	}
	
	public String getCss() {
		return getCss("format.css");
	}
	
	public String getCss(String name) {
		link = Constants.LINK_SITE_CSS.replace("{name}", name);
		return toString();
	}

	public String getOtherResource(String name) {
		link = Constants.LINK_SITE_OTHER.replace("{name}", name);
		return toString();
	}
	
	public String getPage(Page page) {
		link = Constants.LINK_SITE_PAGE.replace("{uuid}", page.getId().toString());
		return toString();
	}
	
	@Override
	public String toString() {
		if(isExportView)
			throw new UnsupportedOperationException();
		return link;
	}
}
