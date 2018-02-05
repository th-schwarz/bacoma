package codes.thischwa.bacoma.rest.render.resource;

import java.io.File;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.model.BoInfo;
import codes.thischwa.bacoma.model.pojo.site.Level;
import codes.thischwa.bacoma.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.render.context.util.PathTool;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VirtualFile implements IVirtualFile {
	
	@Autowired
	private PathTool pathTool; 
		
	protected Site site;
	protected boolean forLayout;
	protected File baseFile;
	protected String resourceFolder;
	
	public void init() {
		this.forLayout = false;
	}
	public void initforLayout() {
		this.forLayout = true;
	}
	
	@Override
	public Path getExportFile() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTagSrcForExport(final Level level) {
		String path = pathTool.getURLFromFile(BoInfo.getSite(level), baseFile.getAbsolutePath());
		StringBuilder tag = new StringBuilder(pathTool.getURLRelativePathToRoot(level));
		tag.append(path.substring(1)); // cut obsolete slash
		return tag.toString();
	}
	
	@Override
	public String getTagSrcForPreview() {
//		String path = pathTool.getURLFromFile(baseFile.getAbsolutePath());
//		String link = String.format("/%s%s", Constants.LINK_IDENTICATOR_SITE_RESOURCE, path);
//		return link;
		throw new UnsupportedOperationException();
	}

	@Override
	public File getBaseFile() {
		return baseFile;
	}

	@Override
	public void consructFromTagFromView(final String src) throws IllegalArgumentException {
//		String urlResourcePath = String.format("/%s/%s", Constants.LINK_IDENTICATOR_SITE_RESOURCE, resourceFolder);
//		if(!src.startsWith(urlResourcePath))
//			throw new IllegalArgumentException("Unknown resource folder!");
//		String path = PathTool.decodePath(src.substring(Constants.LINK_IDENTICATOR_SITE_RESOURCE.length()+2, src.length()));
//		baseFile = new File(PoPathInfo.getSiteDirectory(site), path);
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isForLayout() {
		return forLayout;
	}
}
