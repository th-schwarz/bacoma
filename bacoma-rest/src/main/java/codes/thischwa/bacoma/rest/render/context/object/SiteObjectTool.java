package codes.thischwa.bacoma.rest.render.context.object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.model.BoInfo;
import codes.thischwa.bacoma.model.IOrderable;
import codes.thischwa.bacoma.model.IRenderable;
import codes.thischwa.bacoma.model.InstanceUtil;
import codes.thischwa.bacoma.model.OrderableInfo;
import codes.thischwa.bacoma.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.model.pojo.site.Level;
import codes.thischwa.bacoma.model.pojo.site.Page;
import codes.thischwa.bacoma.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.render.VelocityRenderer;
import codes.thischwa.bacoma.rest.render.context.IContextObjectNeedRenderable;
import codes.thischwa.bacoma.rest.render.context.object.tagtool.LinkTagTool;

/**
 * Context object to provide information about the current site and dependent objects.
 */
@Component("sitetool")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SiteObjectTool implements IContextObjectNeedRenderable {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Site site;
	private IRenderable renderable;
	@Autowired private VelocityRenderer velocityRenderer;
	@Autowired private LinkTagTool linkTagTool; 
	
	@Override
	public void setRenderable(IRenderable renderable) {
		this.renderable = renderable;
		site = BoInfo.getSite(renderable);
	}
	
	/**
	 * @return The current {@link Site}.
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * @return The current {@link Level} or null.
	 */
	public Level getLevel() {
		return BoInfo.getLevel(renderable);
	}

	/**
	 * It tries to get a 'direct' sublevel of the current level by name.
	 * 
	 * @param name
	 *            Name of the desired sublevel.
	 * @return Sublevel with the desired name.
	 * @see BoInfo#getLevelByName(Level, String)
	 */
	public Level getLevelByName(String name) {
		return BoInfo.getLevelByName(getLevel(), name);
	}

	/**
	 * It tries to get a 'direct' sublevel of the desired level by name.
	 * 
	 * @param level The parent level.
	 * @param name
	 *            Name of the desired sublevel.
	 * @return Sublevel with the desired name.
	 * @see BoInfo#getLevelByName(Level, String)
	 */	
	public Level getLevelByName(Level level, String name) {
		return BoInfo.getLevelByName(level, name);
	}

	/**
	 * Tries to find a {@link Level} by name from all levels of a {@link Site}. If there are more levels with the same name, the first one
	 * is taken.
	 * 
	 * @param name
	 *            Name of the desired {@link Level}.
	 * @return A {@link Level} with the desired name or null.
	 */
	public Level getLevelByNameGlobal(final String name) {
		List<Level> levels = BoInfo.getAllLevels(site);
		for (Level level : levels)
			if (level.getName().equals(name))
				return level;
		return null;
	}

	/**
	 * Tries to find a {@link Page} by name of a {@link Level}.
	 * 
	 * @param level
	 *            {@link Level} where to find the {@link Page}.
	 * @param pageName
	 *            Desired name of the {@link Page}.
	 * @return The {@link Page} with the desired name or null.
	 * @see BoInfo#getPageByName(Level, String).
	 */
	public Page getPageByName(final Level level, final String pageName) {
		return BoInfo.getPageByName(level, pageName);
	}

	/**
	 * @return The current {@link Page} or null.
	 */
	public Page getPage() {
		return (InstanceUtil.isPage(renderable)) ? (Page)renderable : null;
	}

//	/**
//	 * @return The current {@link Image} or null.
//	 */
//	public Image getImage() {
//		return this.pojoHelper.getImage();
//	}

	/**
	 * @return True if the {@link IOrderable} has a previous one.
	 */
	public boolean getHasPrevious(final IOrderable<?> orderable) {
		return OrderableInfo.hasPrevious(orderable);
	}

	/**
	 * @return True if the {@link IOrderable} has a next one.
	 */
	public boolean getHasNext(final IOrderable<?> orderable) {
		return OrderableInfo.hasNext(orderable);
	}

	public IOrderable<?> getNext(final IOrderable<?> orderable) {
		return OrderableInfo.getNext(orderable);
	}

	public IOrderable<?> getPrevious(final IOrderable<?> orderable) {
		return OrderableInfo.getPrevious(orderable);
	}

	/**
	 * @return The content field with the desired name of the current {@link Page}.
	 */
	public String getContent(final String fieldName) {
		if(InstanceUtil.isPage(renderable)) {
			logger.warn("No current page found!");
			return "";
		}
		Page page = (Page) renderable;
		if (CollectionUtils.isEmpty(page.getContent())) {
			logger.debug("Page has no content!");
			return "";
		}

		String value = BoInfo.getValue(page, fieldName);
		if (value == null) {
			logger.warn("No value found for content named: ".concat(fieldName));
			return "";
		}

//		imageTagTool.setViewMode(viewMode);
//		imageTagTool.setPojoHelper(this.pojoHelper);
		linkTagTool.setRenderable(page);
		Map<String, Object> ctxObjs = new HashMap<>(1);
//		ctxObjs.put("imagetagtool", imageTagTool);
		ctxObjs.put("linktagtool", linkTagTool);
		return velocityRenderer.renderString(value, ctxObjs);
	}

	/**
	 * @param renderable
	 * @return True, if the desired object is inside the current object path.
	 */
	public boolean hierarchyContains(AbstractBacomaObject<?> bo) {
		List<AbstractBacomaObject<?>> crumbs = BoInfo.getBreadcrumbs((AbstractBacomaObject<?>) renderable);
		return crumbs.contains(bo);
	}

//	/**
//	 * @return True if the current {@link Page} is a {@link Gallery}. Remember: gallery is inherited from page.
//	 */
//	public boolean getPageIsGallery() {
//		return getPageIsGallery(this.pojoHelper.getPage());
//	}
	
//	/**
//	 * Check if the desired {@link Page} if a {@link Gallery}.
//	 * @param page
//	 * 
//	 * @return True if the desired {@link Page} is a {@link Gallery}, otherwise false.
//	 */
//	public boolean getPageIsGallery(final Page page) {
//		return InstanceUtil.isGallery(page);
//	}

	/**
	 * @return Welcome page of the {@link Site} or null.
	 */
	public Page getWelcomePage() {
		List<Page> pages = BoInfo.getSite(renderable).getPages();
		return (CollectionUtils.isEmpty(pages)) ? null : pages.get(0);
	}

	/**
	 * @return A list of breadcrumbs without the beginning site object.
	 */
	public List<AbstractBacomaObject<?>> getBreadcrumbs() {
		List<AbstractBacomaObject<?>> crumbs = BoInfo.getBreadcrumbs((AbstractBacomaObject<?>) renderable);
		if (!crumbs.isEmpty())
			crumbs.remove(0);
		return crumbs;
	}

//	/**
//	 * @return True, if pojo is an {@link Image}.
//	 */
//	public boolean isImage(final AbstractBacomaObject<?> pojo) {
//		return InstanceUtil.isImage(pojo);
//	}
//	
//	public boolean isGallery(final AbstractBacomaObject<?> pojo) {
//		return InstanceUtil.isGallery(pojo);
//	}
}
