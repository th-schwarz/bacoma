package codes.thischwa.bacoma.rest.util;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.GenericSpringJUnitTest;

public class TestFileSystemUtil extends GenericSpringJUnitTest {
	
	@Autowired
	private FileSystemUtil fileSystemUtil;

	@Test
	public void testGetDataDir() {
		Path actual = fileSystemUtil.getSitesDir();
		assertEquals(Paths.get(System.getProperty("user.dir"), "webapp", "data", "site.test"), actual);
		
		actual = fileSystemUtil.getSitesDir("a", "b");
		assertEquals(Paths.get(System.getProperty("user.dir"), "webapp", "data", "site.test", "a", "b"), actual);
	}

	@Test
	public void testGetUniqueName() throws Exception {
		Path path = GenericSpringJUnitTest.getDemoSitePath();
		assertEquals("format_1.css", fileSystemUtil.getUniqueName(path, "format.css"));
	}
}
