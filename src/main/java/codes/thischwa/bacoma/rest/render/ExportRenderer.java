package codes.thischwa.bacoma.rest.render;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codes.thischwa.bacoma.Constants;
import codes.thischwa.bacoma.rest.SiteConfiguration;
import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.IRenderable;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractSiteResource;
import codes.thischwa.bacoma.rest.service.SiteManager;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

@Service
public class ExportRenderer {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FileSystemUtil fileSystemUtil;

	@Autowired
	private SiteConfiguration siteConfig;
	
	@Autowired
	private VelocityRenderer velocityRenderer;
	
	public void render(SiteManager sm) throws IOException {
		logger.info("try to render : {}", sm.getSite().getUrl());
		Path exportDir = fileSystemUtil.getSiteExportDirectory(sm.getSite());
		if(!Files.exists(exportDir)) {
			Files.createDirectories(exportDir);
			logger.debug("Export-dir successful created: {}", exportDir.toString());
		} else {
			logger.debug("Export-dir already exists: {}", exportDir.toString());
			FileUtils.deleteDirectory(exportDir.toFile());
			Files.createDirectories(exportDir);
		}
		
		StringBuilder messages = new StringBuilder();
		Set<IRenderable> renderables = BoInfo.collectRenderables(sm.getSite(), messages);
		if(messages.length() >  0) {
			logger.warn(messages.toString());
			return;
		}
		 BoPathTool pathTool = new BoPathTool(siteConfig.getSite());
		for(IRenderable renderable : renderables) {
			StringBuilderWriter writer = new StringBuilderWriter();
			velocityRenderer.render(writer, renderable, ViewMode.EXPORT);
			Path exportPath = pathTool.getExportFile(renderable, exportDir);
			Path parent = exportPath.getParent();
			if(!Files.exists(parent))
				Files.createDirectories(parent);
			Files.createFile(exportPath);
			Files.write(exportPath, writer.toString().getBytes(Constants.DEFAULT_CHARSET), 
					StandardOpenOption.WRITE);
		}
		List<AbstractSiteResource> resources = new ArrayList<>();
		resources.addAll(sm.getSite().getCascadingStyleSheets());
		resources.addAll(sm.getSite().getOtherResources());
		for(AbstractSiteResource res : resources) {
			Path exportPath = pathTool.getExportFile(res, exportDir);
			Path parent = exportPath.getParent();
			if(!Files.exists(parent))
				Files.createDirectories(parent);
			Files.createFile(exportPath);
			Files.write(exportPath, res.getText().getBytes(Constants.DEFAULT_CHARSET), 
					StandardOpenOption.WRITE);
		}
	}
	
}
