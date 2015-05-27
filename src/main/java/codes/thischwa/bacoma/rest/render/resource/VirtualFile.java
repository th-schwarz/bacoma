package codes.thischwa.bacoma.rest.render.resource;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.render.context.util.PathTool;
import codes.thischwa.bacoma.rest.service.ContextUtility;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class VirtualFile implements IVirtualFile {
	@Autowired
	private PathTool pathTool;
	
	@Autowired
	private ContextUtility cu;
	
	@Autowired
	private FileSystemUtil fileSystemUtil;
	
	protected Site site;
	protected boolean forLayout;
	protected File baseFile;
	protected String resourceFolder;
	
	public void init() {
		this.forLayout = true;
	}
	public void initforLayout() {
		this.forLayout = true;
	}
	
	@Override
	public File getExportFile() {
		File exportFile = fileSystemUtil.getSiteExportDirectory();
		return exportFile;
	}

	@Override
	public String getTagSrcForExport(final Level level) {
		String path = pathTool.getURLFromFile(baseFile.getAbsolutePath());
		StringBuilder tag = new StringBuilder(pathTool.getURLRelativePathToRoot(level));
		tag.append(path.substring(1)); // cut obsolete slash
		return tag.toString();
	}
	
	@Override
	public String getTagSrcForPreview() {
		String path = pathTool.getURLFromFile(baseFile.getAbsolutePath());
		String link = String.format("/%s%s", Constants.LINK_IDENTICATOR_SITE_RESOURCE, path);
		return link;
	}

	@Override
	public File getBaseFile() {
		return baseFile;
	}

	@Override
	public void consructFromTagFromView(final String src) throws IllegalArgumentException {
		String urlResourcePath = String.format("/%s/%s", Constants.LINK_IDENTICATOR_SITE_RESOURCE, resourceFolder);
		if(!src.startsWith(urlResourcePath))
			throw new IllegalArgumentException("Unknown resource folder!");
		String path = PathTool.decodePath(src.substring(Constants.LINK_IDENTICATOR_SITE_RESOURCE.length()+2, src.length()));
		baseFile = new File(PoPathInfo.getSiteDirectory(site), path);
	}
	
	@Override
	public boolean isForLayout() {
		return forLayout;
	}
}
