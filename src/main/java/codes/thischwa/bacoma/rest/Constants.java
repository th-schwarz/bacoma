package codes.thischwa.bacoma.rest;

import java.nio.file.FileSystems;

public interface Constants {

	static final String LINK_PREVIEW = "/site/{siteUrl}/render/get/preview/{uuid}";
	
	static final String FILENAME_SEPARATOR = FileSystems.getDefault().getSeparator();
}
