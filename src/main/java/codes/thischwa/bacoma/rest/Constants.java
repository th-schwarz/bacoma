package codes.thischwa.bacoma.rest;

import java.nio.file.FileSystems;

public interface Constants {

	public static final String LINK_PREVIEW = "/site/render/get/preview/{uuid}";
	
	public static final String FILENAME_SEPARATOR = FileSystems.getDefault().getSeparator();
}
