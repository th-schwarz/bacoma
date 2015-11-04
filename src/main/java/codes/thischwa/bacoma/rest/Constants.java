package codes.thischwa.bacoma.rest;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface Constants {

	static final String LINK_PREVIEW = "/site/{siteUrl}/render/get/preview/{uuid}";
	
	static final String FILENAME_SEPARATOR = FileSystems.getDefault().getSeparator();
	
	static final Path DIR_TEMP = Paths.get(System.getProperty("java.io.tmpdir"), "bacoma");
}
