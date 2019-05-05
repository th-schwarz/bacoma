package codes.thischwa.bacoma.rest.util;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.AbstractBacomaTest;

public class TestFileSystemUtil extends AbstractBacomaTest {
	
	@Autowired
	private FileSystemUtil fileSystemUtil;

	@Test
	public void testGetDataDir() {
		Path actual = fileSystemUtil.getSitesDir(siteManager.getSite());
		assertEquals(Paths.get(System.getProperty("user.dir"), "webapp", "data", "site.test"), actual);
		
		actual = fileSystemUtil.getSitesDir(siteManager.getSite(), "a", "b");
		assertEquals(Paths.get(System.getProperty("user.dir"), "webapp", "data", "site.test", "a", "b"), actual);
	}

	@Test
	public void testGetUniqueName() throws Exception {
		Path path = AbstractBacomaTest.getDemoSitePath();
		assertEquals("format_1.css", fileSystemUtil.getUniqueName(path, "format.css"));
	}
}
