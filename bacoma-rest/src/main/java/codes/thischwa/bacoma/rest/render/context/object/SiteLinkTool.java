package codes.thischwa.bacoma.rest.render.context.object;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.model.IRenderable;
import codes.thischwa.bacoma.model.OrderableInfo;
import codes.thischwa.bacoma.model.pojo.site.Level;
import codes.thischwa.bacoma.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.render.context.IContextObjectNeedRenderable;

@Component("sitelinktool")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SiteLinkTool implements IContextObjectNeedRenderable {

	@Autowired
	private SiteLinkPageTool linkPageTool;
	
	@Autowired
	private SiteLinkResourceTool linkResourceTool;
	
	@Override
	public void setRenderable(IRenderable renderable) {
		linkPageTool.setRenderable(renderable);
		linkResourceTool.setRenderable(renderable);
	}
	
	public String getCss() {
		return getCss("format.css");
	}
	
	public String getCss(String name) {
		return linkResourceTool.setCss(name).toString();
	}

	public String getOtherResource(String name) {
		return linkResourceTool.setOther(name).toString();
	}
	
	public String get(Page page) {
		return linkPageTool.set(page).toString();
	}	
		
	public String get(Level level) {
		return linkPageTool.set(level).toString();
	}
	
	public String getPrevious(Page page) {
		return linkPageTool.set(OrderableInfo.getPrevious(page)).toString();
	}

	public String getNext(Page page) {
		return linkPageTool.set(OrderableInfo.getNext(page)).toString();
	}
}
