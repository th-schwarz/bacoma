package codes.thischwa.bacoma.rest.render.context.object.tagtool;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.model.IRenderable;
import codes.thischwa.bacoma.rest.render.context.IContextObjectCommon;
import codes.thischwa.bacoma.rest.render.context.IContextObjectNeedRenderable;
import codes.thischwa.bacoma.rest.render.context.RenderData;
import codes.thischwa.bacoma.rest.render.context.util.Link;
import codes.thischwa.bacoma.rest.render.context.util.PathTool;

/**
 * Construct an a-tag. Mainly used by other context objects.
 */
@Component(value="linktagtool")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LinkTagTool extends GenericXhtmlTagTool implements IContextObjectCommon, IContextObjectNeedRenderable {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private IRenderable renderable;
	private boolean isExternalLink = false;
	
	@Autowired
	private Link link;	
	
	@Autowired
	private RenderData renderData;
	
	@Autowired
	private PathTool pathTool;
	
	public LinkTagTool() {
		super("a");
	}
	
	public void setRenderable(IRenderable renderable) {
		this.renderable = renderable;
	}

	public LinkTagTool setHref(final String href) {
		link.init(href);
		isExternalLink = link.isExternal();
		String tempHref;
		if (isExternalLink)
			tempHref = href;
		else
			tempHref = href.replace(File.separatorChar, '/');
		return setAttribute("href", pathTool.encodePath(tempHref));
	}

	public LinkTagTool setAttribute(final String name, final String value) {
		super.putAttr(name, value);
		return this;
	}

	public LinkTagTool setTagValue(final String tagValue) {
		super.setValue(tagValue);
		return this;
	}

	/**
	 * Construct the a-tag.
	 */
	@Override
	public String toString() {
		String hrefString = super.getAttr("href");
		// 1. check, if the base attributes are set
		if (StringUtils.isBlank(hrefString))
			throw new IllegalArgumentException("'href' isn't set!");

		// 2. construct the tag for preview or export
		if (!isExternalLink) {
//			VirtualFile vf = new VirtualFile(this.pojoHelper.getSite(), false);
//			vf.consructFromTagFromView(hrefString);
//			if (viewMode == ViewMode.EXPORT) {
//				try {
//					File srcFile = vf.getBaseFile();
//					File destFile = vf.getExportFile();
//					FileUtils.copyFile(srcFile, destFile);
//					renderData.addFile(vf);
//				} catch (IOException e) {
//					logger.error("Error while copy [" + vf.getBaseFile().getPath() + "] to [" + vf.getExportFile().getPath() + "]: " + e.getMessage(), e);
//					throw new FatalException("Error while copy [" + vf.getBaseFile().getPath() + "] to [" + vf.getExportFile().getPath() + "]: " + e.getMessage(), e);
//				}
//				this.setHref(vf.getTagSrcForExport(this.pojoHelper.getLevel()));
//			} else
//				this.setHref(vf.getTagSrcForPreview());
			throw new UnsupportedOperationException();
		}
		
		isExternalLink = false;
		return super.contructTag();
	}
}
