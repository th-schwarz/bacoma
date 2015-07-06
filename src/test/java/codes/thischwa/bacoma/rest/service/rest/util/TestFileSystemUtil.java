package codes.thischwa.bacoma.rest.service.rest.util;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.GenericSpringJUnitTest;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

public class TestFileSystemUtil extends GenericSpringJUnitTest {
	
	@Autowired
	private FileSystemUtil fileSystemUtil;

	@Test
	public void testGetDataDir() {
		Path actual = fileSystemUtil.getDataDir("a", "b");
		assertEquals(Paths.get(System.getProperty("user.dir"), "data", "a", "b"), actual);
	}

}
