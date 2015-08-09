package codes.thischwa.bacoma.rest;

import java.nio.file.FileSystems;

public interface Constants {

	public static final String LINK_SITE_PAGE = "/site/render/get/{uuid}";
	public static final String LINK_SITE_CSS = "/site/resource/css/?name={name}";
	public static final String LINK_SITE_OTHER = "/site/resource/other/?name={name}";
	
	public static final String FILENAME_SEPARATOR = FileSystems.getDefault().getSeparator();
}
