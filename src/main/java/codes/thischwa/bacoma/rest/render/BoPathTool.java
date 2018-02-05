package codes.thischwa.bacoma.rest.render;

import java.awt.Image;
import java.nio.file.Path;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import codes.thischwa.bacoma.Constants;
import codes.thischwa.bacoma.rest.model.IRenderable;
import codes.thischwa.bacoma.rest.model.InstanceUtil;
import codes.thischwa.bacoma.rest.model.OrderableInfo;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;

class BoPathTool implements Constants {

	private Map<String, String> siteConfig;
	
	BoPathTool(Map<String, String> siteConfig) {
		this.siteConfig = siteConfig;
	}

	/**
	 * @return Export file path of an {@link AbstractSiteResource}.
	 */
	Path getExportFile(AbstractSiteResource res, Path exportDir) {
		String folder;
		switch(res.getResourceType()) {
			case CSS:
				folder = siteConfig.get(KEY_EXPORT_DIR_RESOURCES_CSS);
				break;
			case OTHER:
				folder = siteConfig.get(KEY_EXPORT_DIR_RESOURCES_OTHER);
				break;
			default:
				throw new IllegalArgumentException(
						String.format("Illegal resourcee-type in this context: %s", res.getResourceType().toString()));
		}
		return exportDir.resolve(folder).resolve(res.getName());	
	}
	
	/**
	 * @return Export file path of an {@link IRenderable}.
	 */
	Path getExportFile(IRenderable renderable, Path exportDir) {
		Level parent;
		AbstractBacomaObject<?> po = (AbstractBacomaObject<?>) renderable;
//		if (InstanceUtil.isImage(renderable)) {
//			Gallery gallery = (Gallery) po.getParent();
//			parent = (Level) gallery.getParent();
//		} else
			parent = (Level) po.getParent();
		String containerPath = getFSHierarchicalPathSegment(parent);
		Path outDir;
		if (StringUtils.isNotBlank(containerPath)) 
			outDir = exportDir.resolve(containerPath);
		else
			outDir = exportDir;
		
		Path outFile = outDir.resolve(getExportBaseFilename(renderable));
		return outFile;
	}
	
	/**
	 * Generates the path segment of the hierarchical container, needed e.g. as part of the export path.
	 * 
	 * @param level
	 * @return Hierarchical path segment.
	 */
	private String getFSHierarchicalPathSegment(Level level) {
		if (level == null)
			return "";
		StringBuilder path = new StringBuilder();
		if (!InstanceUtil.isSite(level))
			path.append(level.getName());
		Level tmpContainer = level.getParent();
		while (tmpContainer != null && !InstanceUtil.isSite(tmpContainer)) {
			path.insert(0, tmpContainer.getName().concat(Constants.FILENAME_SEPARATOR));
			tmpContainer = tmpContainer.getParent();
		}		
		return path.toString();
	}

    /**
     * Generates the file name of an {@link IRenderable}.<br>
     * If 'renderable' is an {@link Image}, the name will be constructed by the following pattern: <br>
     * <code>[gallery name]-[image base name].[ext]</code>.
     * 
     * @param renderable
     * @return Return the file name without path!
     */
    String getExportBaseFilename(final IRenderable renderable) {
    	if (renderable == null)
    		throw new IllegalArgumentException("Can't handle IRenderable is null!");
    	StringBuilder name = new StringBuilder();
    	if (InstanceUtil.isPage(renderable)) {
    		Page page = (Page) renderable;
            if (OrderableInfo.isFirst(page))
            	name.append(siteConfig.get(KEY_EXPORT_FILE_WELCOME));
            else {
                name.append(page.getName());
            	name.append('.');
            	name.append(siteConfig.get(KEY_EXPORT_FILE_EXTENSION));
            }
//    	} else if (InstanceUtil.isImage(renderable)) {
//    		Image image = (Image) renderable;
//        	name.append(image.getParent().getName());   
//        	name.append('-');
//        	name.append(FilenameUtils.getBaseName(image.getFileName())); 
//        	name.append('.');    
//        	name.append(extension);		
    	} else
    		throw new IllegalArgumentException("Unknown object TYPE!");

    	return name.toString();
    }
    
    /**
     * Generates the relative path to the root of the site.
     * 
     * @param level
     * @return Relative path to the root, starting point is container.
     */
    public static String getURLRelativePathToRoot(final Level level) {
    	if (level == null || InstanceUtil.isSite(level))
			return "";
		StringBuilder link = new StringBuilder();
		for (int i = 1; i < level.getHierarchy(); i++) {
			link.append("../");
		}
		return link.toString();
    }
}
