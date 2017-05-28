package codes.thischwa.bacoma.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codes.thischwa.c5c.filemanager.Options.FILE_SORTING;
import codes.thischwa.c5c.filemanager.Options.VIEW_MODE;
import codes.thischwa.c5c.filemanager.Security.UPLOAD_POLICY;
import codes.thischwa.c5c.requestcycle.impl.GlobalFilemanagerLibConfig;

public class FilemanagerConfigBuilderImpl extends GlobalFilemanagerLibConfig {
	private static Logger logger = LoggerFactory.getLogger(FilemanagerConfigBuilderImpl.class);

	@Override
	protected void postLoadConfigFileHook() {
		userConfig.setComment("Built by bacoma.");
		userConfig.getSecurity().setUploadPolicy(UPLOAD_POLICY.ALLOW_ALL);
		userConfig.getUpload().setFileSizeLimit(3);

		userConfig.getOptions().setTheme("default");
		userConfig.getOptions().setServerRoot(false);
		userConfig.getOptions().setFileRoot("/");
		userConfig.getOptions().setSplitterWidth(150);
		userConfig.getOptions().setSplitterMinWidth(150);

		userConfig.getImages().getResize().setEnabled(false);
		userConfig.getAudios().setShowPlayer(false);
		userConfig.getVideos().setShowPlayer(false);
		userConfig.getPdfs().setShowReader(false);

		userConfig.getOptions().setDefaultViewMode(VIEW_MODE.LIST);
		userConfig.getOptions().setFileSorting(FILE_SORTING.DEFAULT);

		logger.debug("BACOMA related configuration done.");
	}
}
