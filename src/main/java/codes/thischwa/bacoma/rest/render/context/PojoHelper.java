package codes.thischwa.bacoma.rest.render.context;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.IRenderable;
import codes.thischwa.bacoma.rest.model.InstanceUtil;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;


/**
 * Helper class to provide infos about the current rendered {@link AbstractBacomaObject}.
 */
public class PojoHelper {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Site site = null;
	private Level level = null;
	private Page page = null;
//	private Gallery gallery = null;
//	private Image image = null;
	private AbstractSiteResource siteResource = null;

	public void putpo(final AbstractBacomaObject<?> po) {
		if (po == null)
			throw new IllegalArgumentException("Can't work with object is null!");
		logger.debug("Entered putPersitentPojo with: ".concat(po.toString()));
		if (InstanceUtil.isSite(po))
			putSite((Site) po);
		else if (InstanceUtil.isJustLevel(po))
			putLevel((Level) po);
		else if (InstanceUtil.isPage(po))
			putPage((Page) po);
//		else if (InstanceUtil.isGallery(po))
//			putGallery((Gallery) po);
//		else if (InstanceUtil.isImage(po))
//			putImage((Image) po);
		else if (InstanceUtil.isSiteResource(po))
			putSiteResource((AbstractSiteResource) po);
		else {
			logger.warn("Unknown object or content: " + po.getClass().getName());
			return;
		}
	}

	private void putSite(final Site site) {
		deleteLevel();
		this.site = site;
	}

	private void putLevel(final Level level) {
		if (level == null) {
			logger.warn("Can't handle a Level = null !");
			return;
		}
		site = BoInfo.getSite(level);
		this.level = level;
		deletePage();
//		deleteGallery();
		deleteSiteResource();
	}

	private void putPage(final Page page) {
		if (page == null) {
			logger.warn("Can't handle a Page = null !");
			return;
		}
		site = BoInfo.getSite((AbstractBacomaObject<Level>)page);
		level = page.getParent();
		this.page = page;
//		deleteGallery();
		deleteSiteResource();
	}

//	private void putGallery(final Gallery gallery) {
//		if (gallery == null) {
//			logger.warn("Can't handle a Gallery = null !");
//			return;
//		}
//		site = BoInfo.getSite(gallery);
//		level = gallery.getParent();
//		this.gallery = gallery;
//		deletePage();
//		deleteSiteResource();
//	}
//
//	private void putImage(final Image image) {
//		if (image == null) {
//			logger.warn("Can't handle an Image = null !");
//			return;
//		}
//		gallery = image.getParent();
//		putGallery(gallery);
//		this.image = image;
//		deleteSiteResource();
//	}

	private void putSiteResource(final AbstractSiteResource siteResource) {
		this.siteResource = siteResource;
	}

	/**
	 * @return The {@link Site} currently working on.
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * @return The {@link Level} currently working on.
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * @return The {@link Page} currently working on.
	 */
	public Page getPage() {
		return page;
	}

//	/**
//	 * @return The {@link Gallery} currently working on.
//	 */
//	public Gallery getGallery() {
//		return gallery;
//	}
//
//	/**
//	 * @return The {@link de.thischwa.pmcms.model.domain.pojo.Image} currently working on.
//	 */
//	public Image getImage() {
//		return image;
//	}

	public void deleteSite() {
		site = null;
		level = null;
		page = null;
//		gallery = null;
//		image = null;
	}

	public void deleteLevel() {
		level = null;
		page = null;
//		gallery = null;
//		image = null;
	}

	public void deletePage() {
		page = null;
//		image = null;
	}

//	public void deleteGallery() {
//		gallery = null;
//		image = null;
//	}
//
//	public void deleteImage() {
//		image = null;
//	}

	public void deleteSiteResource() {
		siteResource = null;
	}

	/**
	 * @return The 'deepest' available {@link IRenderable}.
	 */
	public IRenderable getRenderable() {
//		if (image != null)
//			return image;
//		if (gallery != null)
//			return image;
		if (page != null)
			return page;
		return null;
	}

	/**
	 * @return The 'deepest' available {@link AbstractBacomaObject}.
	 */
	public AbstractBacomaObject<?> get() {
//		if (image != null)
//			return image;
//		if (gallery != null)
//			return gallery;
		if (page != null)
			return page;
		if (level != null)
			return level;
		if (site != null)
			return site;
		return null;
	}

	/**
	 * @return The 'deepest' available {@link AbstractSiteResource}.
	 */
	public AbstractSiteResource getSiteResource() {
		return siteResource;
	}

	/**
	 * Wrapper for {@link BoInfo#getBreadcrumbs(IPoorMansObject)} to get the breadcrumbs objects of the 'deepest' current object.
	 */
	public List<AbstractBacomaObject<?>> getCurrentBreadcrumbs() {
//		if (image != null)
//			return BoInfo.getBreadcrumbs(getImage());
//		else if (gallery != null)
//			return BoInfo.getBreadcrumbs(getGallery());
//		else
			if (page != null)
			return BoInfo.getBreadcrumbs(getPage());
		else if (level != null)
			return BoInfo.getBreadcrumbs(getLevel());
		else
			return new ArrayList<>();
	}

	public boolean containsInBreadcrumbs(final AbstractBacomaObject<?> po) {
		return getCurrentBreadcrumbs().contains(po);
	}
}
