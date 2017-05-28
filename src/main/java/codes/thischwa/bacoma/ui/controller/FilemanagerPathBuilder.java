package codes.thischwa.bacoma.ui.controller;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;

import codes.thischwa.bacoma.Constants;
import codes.thischwa.c5c.requestcycle.BackendPathBuilder;
import codes.thischwa.c5c.requestcycle.Context;

public class FilemanagerPathBuilder implements BackendPathBuilder {

	@Override
	public String getBackendPath(String urlPath, Context context, ServletContext servletContext) {
		String baseDir = System.getProperty(Constants.SYSPROP_DIR_FILEMANAGER);
		if(StringUtils.isEmpty(baseDir))
			throw new IllegalArgumentException("system-property 'dir.filemanager' not found!");
		String backendPath = baseDir + urlPath;
		return backendPath;
	}

}
