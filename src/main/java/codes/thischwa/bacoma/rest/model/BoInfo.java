package codes.thischwa.bacoma.rest.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import codes.thischwa.bacoma.rest.exception.ResourceNotFoundException;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.CascadingStyleSheet;
import codes.thischwa.bacoma.rest.model.pojo.site.Content;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.OtherResource;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.model.pojo.site.Template;
import codes.thischwa.bacoma.rest.model.pojo.site.TemplateType;

/**
 * Helper to provide some informations of an {@link AbstractBacomaObject}, which you can get without a database call.
 * All methods are null-save!
 */
public class BoInfo {

	/**
	 * Get the value of a field.
	 * 
	 * @param fieldName
	 * @return Value of the field or null, if not found.
	 */
	public static String getValue(final Page page, final String fieldName) {
		if (page == null || StringUtils.isBlank(fieldName) || CollectionUtils.isEmpty(page.getContent()))
			return null;

		for (Content content : page.getContent()) {
			if (content.getName().equalsIgnoreCase(fieldName))
				return content.getValue();
		}
		return null;
	}
	
	public static Content getContentByName(final Page page, final String fieldName) {
		if (StringUtils.isBlank(fieldName) || page == null || page.getContent() == null)
			return null;
		for (Content field : page.getContent()) {
			if (field.getName().equals(fieldName))
				return field;
		}
		return null;
	}
	
	public static Page getPageByName(final Level level, final String pageName) {
		if(level == null)
			throw new IllegalArgumentException("Level is null!");
		if (CollectionUtils.isEmpty(level.getPages()) || StringUtils.isBlank(pageName))
			return null;
		for (Page page : level.getPages()) {
			if (page.getName().equals(pageName))
				return page;
		}
		return null;
	}
	
	/**
	 * Collects all (iterative) sublevels of the specified level.
	 * 
	 * @param level
	 * @return all sublevels of the specified level.
	 */
	public static List<Level> getAllLevels(final Level level) {
		List<Level> levels = new ArrayList<>();
		collectLevels(level, levels);
		return levels;
	}
	
	private static void collectLevels(final Level level, List<Level> levels) {
		if (!InstanceUtil.isSite(level))
			levels.add(level);
		for (Level sublevel : level.getSublevels())
			collectLevels(sublevel, levels);
	}
	
	/**
	 * Tries to get a 'direct' sublevel of the desired level by name.
	 *  
	 * @param level
	 * @param name
	 * @return The sublevel of the desired level with the desired name.
	 */
	public static Level getLevelByName(final Level level, final String name) throws IllegalArgumentException {
		if(level == null)
			throw new IllegalArgumentException("Level is null!");
		if (StringUtils.isBlank(name) || level == null)
			return null;
		for (Level tmpLevel : level.getSublevels())
			if (tmpLevel.getName().equals(name))
				return tmpLevel;
		
		throw new IllegalArgumentException("No level named [" + name + "] found!");
	}

	public static Level getRootLevel(final Level level) {
		return (level.getSublevels().isEmpty()) ? null : level.getSublevels().iterator().next();
	}

	public static int getMaxChildContainerRank(final Level level) {
		return (CollectionUtils.isEmpty(level.getSublevels())) ? -1 : level.getSublevels().size();
	}

	public static Page getRootPage(final Level level) {
		return (CollectionUtils.isEmpty(level.getPages())) ? null : level.getPages().iterator().next();
	}
	
	public static Site getSite(final AbstractBacomaObject<?> po) {
		if (po == null)
			return null;
		if (InstanceUtil.isSite(po))
			return (Site) po;
		AbstractBacomaObject<?> parent = (AbstractBacomaObject<?>) po.getParent();
		while (parent.getParent() != null)
			parent = (AbstractBacomaObject<?>) parent.getParent();
		if (!(parent instanceof Site))
			throw new IllegalArgumentException("Fatal hierarchy error!");
		return (Site) parent;
	}
	
	public static Site getSite(final IRenderable renderable) {
		return getSite((AbstractBacomaObject<?>)renderable);
	}
	
	public static IRenderable getFirstRenderable(final Site site) {
		if (site == null) 
			return null;
		if (!CollectionUtils.isEmpty(site.getPages()))
			return site.getPages().iterator().next();
		Level level = getRootLevel(site);
		return (level == null) ? null : getRootPage(level);
	}
	
	public static boolean isWelcomePage(final Page page) {
		return (page != null && InstanceUtil.isSite(page.getParent()));
	}
		
	public static String[] getTemplateNames(IRenderable renderable) {
		List<String> names = new ArrayList<>();
		for (Template template : getTemplates(getSite(renderable), renderable.getTemplateType()))
			names.add(template.getName());
		return names.toArray(new String[0]);
	}
	
	public static List<Template> getTemplates(final Site site, TemplateType type) {
		List<Template> templates = new ArrayList<>();
		if(site == null)
			return templates;
		for (Template template : site.getTemplates()) {
			if (type == template.getType())
				templates.add(template);
		}
		
		return templates;
	}

	/**
	 * Generate all breadcrumbs objects for persitentPojo (bottom-up).
	 * 
	 * @param bo
	 */
	public static List<AbstractBacomaObject<?>> getBreadcrumbs(final AbstractBacomaObject<?> bo) {
		List<AbstractBacomaObject<?>> list = new ArrayList<>();
		if (bo != null) {
			AbstractBacomaObject<?> temppo = bo;
			list.add(temppo);
			while (temppo.getParent() != null) {
				temppo = (AbstractBacomaObject<?>) temppo.getParent();
				list.add(temppo);
			}
		}
		Collections.reverse(list);
		return list;
	}
	
	public static CascadingStyleSheet getNamedCascadingStyleSheet(Site site, String name) {
		for(CascadingStyleSheet css : site.getCascadingStyleSheets()) {
			if(css.getName().equals(name))
				return css;
		}
		throw new ResourceNotFoundException(site, name);
	}

	public static OtherResource getNamedOtherResource(Site site, String name) {
		for(OtherResource or : site.getOtherResources()) {
			if(or.getName().equals(name))
				return or;
		}
		throw new ResourceNotFoundException(site, name);
	}

	public static Level getLevel(IRenderable renderable) {
		// TODO respect special case for image (gallery)
		if(InstanceUtil.isPage(renderable)) {
			Page page = (Page)renderable;
			return page.getParent();
		} else
			throw new UnsupportedOperationException();
	}
	

	public static Set<IRenderable> collectRenderables(final Level level) {
		return collectRenderables(level, null);
	}
	
	public static Set<IRenderable> collectRenderables(final Level level, StringBuilder messages) {
		Set<IRenderable> renderables = new HashSet<IRenderable>();
		collectRenderables(level, renderables, messages);
		return renderables;
	}
	
	private static void collectRenderables(final Level level, Set<IRenderable> renderables, StringBuilder messages) {
		for (Level tmpContainer : level.getSublevels()) {
			if (messages != null && !InstanceUtil.isSite(level) && CollectionUtils.isEmpty(level.getPages())) {
				messages.append("Level has no page"); //$NON-NLS-1$
				messages.append(level.getName());
				messages.append('\n');
				renderables.clear();
				return;
			}
			collectRenderables(tmpContainer, renderables, messages);
		}
		for (Page page : level.getPages()) {
			renderables.add(page);
			// TODO respect gallery
//			if (InstanceUtil.isGallery(page)) {
//				List<Image> images = ((Gallery)page).getImages();
//				if (renderables != null && CollectionUtils.isEmpty(images)) {
//					messages.append(LabelHolder.get("task.export.error.pojo.galleryhasnoimage")); //$NON-NLS-1$
//					messages.append(page.getDecorationString());
//					messages.append('\n');
//					renderables.clear();
//					return;
//				} else
//					renderables.addAll(images);
//			}
		}
	}
	
}
