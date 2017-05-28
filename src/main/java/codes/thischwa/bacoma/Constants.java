package codes.thischwa.bacoma;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface Constants {

	static final String BASEURL_REST = "/site/{siteUrl}";
	
	static final String LINK_PREVIEW = BASEURL_REST + "/render/get/preview/{uuid}";
	
	static final String LINK_SITE_STATICRESOURCE = BASEURL_REST + "/resource/static/get?path={path}";
	
	static final String FILENAME_SEPARATOR = FileSystems.getDefault().getSeparator();
	
	static final String URLPATH_SEPARATOR = "/";
	
	static final Path DIR_TEMP = Paths.get(System.getProperty("java.io.tmpdir"), "bacoma");
	
	static final String SYSPROP_DIR_WEBAPP = "dir.webapp";

	static final String SYSPROP_DIR_FILEMANAGER = "dir.filemanager";

}
