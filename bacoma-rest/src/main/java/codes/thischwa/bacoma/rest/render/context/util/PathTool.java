 package codes.thischwa.bacoma.rest.render.context.util;

import java.awt.Image;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.model.BoInfo;
import codes.thischwa.bacoma.model.IRenderable;
import codes.thischwa.bacoma.model.InstanceUtil;
import codes.thischwa.bacoma.model.OrderableInfo;
import codes.thischwa.bacoma.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.model.pojo.site.Level;
import codes.thischwa.bacoma.model.pojo.site.Page;
import codes.thischwa.bacoma.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;


/**
 * Helper for constructing, converting paths and urls.<br>
 * All methods are null-safe and return an empty string if one or more arguments are null.
 */
@Component
public class PathTool {
	
	@Value("${site.export.file.welcome}")
	private String welcomeFileName;
	
	@Autowired
	private FileSystemUtil fileSystemUtil;
	
	/**
	 * Generates the path segment of the hierarchical container, needed e.g. as part of the export path.
	 * 
	 * @param level
	 * @return Hierarchical path segment.
	 */
	public String getFSHierarchicalContainerPathSegment(final Level level) {
		if (level == null)
			return "";
		StringBuilder path = new StringBuilder();
		if (!InstanceUtil.isSite(level))
			path.append(level.getName());
		Level tmpContainer = level.getParent();
		while (tmpContainer != null && !InstanceUtil.isSite(tmpContainer)) {
			path.insert(0, tmpContainer.getName().concat(File.separator));
			tmpContainer = tmpContainer.getParent();
		}		
		return path.toString();
	}

    public String getURLFromFile(Site site, String fileName) {
    	return getURLFromFile(site, fileName, true);
    }

    /**
     * Changes an application context relative file to an absolute url.
     * @param site 
     * @param fileName
     * 
     * @return url
     */
    public String getURLFromFile(Site site, final String fileName, boolean encode) {
        File file = new File(fileName);
        String temp = file.getPath();
        temp = temp.substring(fileSystemUtil.getSitesDir(site).toAbsolutePath().toString().length()); // cut the data dir
        temp = temp.replace(File.separatorChar, '/');
        if(encode)
        	temp = encodePath(temp);
        return (temp.startsWith("/")) ? temp : "/".concat(temp);
    }

    /**
     * Generates the file name of an {@link IRenderable}.<br>
     * If 'renderable' is an {@link Image}, the name will be constructed by the following pattern: <br>
     * <code>[gallery name]-[image base name].[ext]</code>.
     * 
     * @param renderable
     * @param extension file extension
     * @return Return the file name without path!
     */
    public String getExportBaseFilename(final IRenderable renderable, String extension) {
    	if (renderable == null)
    		throw new IllegalArgumentException("Can't handle IRenderable is null!");
    	StringBuilder name = new StringBuilder();
    	if (InstanceUtil.isPage(renderable)) {
    		Page page = (Page) renderable;
            if (OrderableInfo.isFirst(page))
            	name.append(welcomeFileName);
            else {
                name.append(page.getName());
            	name.append('.');
            	name.append(extension);
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
	 * @return Export file path of an {@link IRenderable}.
	 */
	public Path getExportFile(final IRenderable renderable, final String extension) {
		Path exportDirectory = fileSystemUtil.getSiteExportDirectory(BoInfo.getSite(renderable));
		Level parent;
		AbstractBacomaObject<?> po = (AbstractBacomaObject<?>) renderable;
//		if (InstanceUtil.isImage(renderable)) {
//			Gallery gallery = (Gallery) po.getParent();
//			parent = (Level) gallery.getParent();
//		} else
			parent = (Level) po.getParent();
		String containerPath = getFSHierarchicalContainerPathSegment(parent);
		Path outDir;
		if (StringUtils.isNotBlank(containerPath)) 
			outDir = exportDirectory.resolve(containerPath);
		else
			outDir = exportDirectory;
		
		Path outFile = outDir.resolve(getExportBaseFilename(renderable, extension));
		return outFile;
	}
	
	
    /**
     * Generates the relative path to the root of the site.
     * 
     * @param level
     * @return Relative path to the root, starting point is container.
     */
    public String getURLRelativePathToRoot(final Level level) {
    	if (level == null || InstanceUtil.isSite(level))
			return "";
		StringBuilder link = new StringBuilder();
		for (int i = 1; i < level.getHierarchy(); i++) {
			link.append("../");
		}
		return link.toString();
    }
    
    /**
     * Generates a relative path from one {@link Level} to another.
     * 
     * @param startLevel
     * @param destLevel
     * @return Relative path.
     */
    public String getURLRelativePathToLevel(final Level startLevel, final Level destLevel) {
		if ((startLevel == null && destLevel == null))
			return "";
		if (destLevel == null)
			return getURLRelativePathToRoot(startLevel);
		if (destLevel.equals(startLevel))
			return "";
		StringBuilder link = new StringBuilder();
		link.append(getURLRelativePathToRoot(startLevel));
		for (AbstractBacomaObject<?> po : BoInfo.getBreadcrumbs(destLevel)) {
			if (InstanceUtil.isJustLevel(po)) {
				Level level = (Level) po;
				link.append(level.getName());
				link.append("/");
			}
		}
		return link.toString();
	}

	/**
	 * Encodes a path for using in links.
	 * 
	 * @param path
	 * @return Encoded path.
	 * @throws RuntimeException, if the (internal used) {@link URI} throws an {@link URISyntaxException}.
	 */
	public String encodePath(final String path) {
		try {
			URI uri = new URI(null, path, null);
			return uri.toASCIIString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Decodes an {@link URI}-encoded path. UTF-8 encoding is using.
	 * 
	 * @param path
	 * @return Decoded path.
	 * @throws RuntimeException, if the (internal used) {@link URLDecoder} throws an {@link UnsupportedEncodingException}. 
	 */
	public static String decodePath(final String path) {
		try {
			return URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
