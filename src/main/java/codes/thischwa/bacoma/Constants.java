package codes.thischwa.bacoma;

import java.nio.charset.Charset;
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
	
	static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");

	static final String KEY_DIR_STATICRESOURCE = "dir.staticresource";
	static final String KEY_DIR_EXPORT = "dir.export";
	
	static final String KEY_EXPORT_FILE_EXTENSION = "export.file.extension";
	static final String KEY_EXPORT_FILE_WELCOME = "export.file.welcome";
	static final String KEY_EXPORT_DIR_RESOURCES_CSS = "export.dir.resources.css";
	static final String KEY_EXPORT_DIR_RESOURCES_OTHER = "export.dir.resources.other";
	static final String KEY_EXPORT_DIR_RESOURCES_STATIC = "export.dir.resources.static";
	
}
