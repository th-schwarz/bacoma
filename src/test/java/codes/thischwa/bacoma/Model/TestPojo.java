package codes.thischwa.bacoma.Model;

import static org.junit.Assert.*;

import org.junit.Test;

import codes.thischwa.bacoma.GenericSpringJUnitTest;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.service.Persister;

public class TestPojo extends GenericSpringJUnitTest {

	@Test
	public void testStructure() throws Exception {
		Persister persister = new Persister();
		Site site = persister.load(testFolder, "site.test");
		assertEquals("site.test", site.getUrl());
		assertEquals("A Test Site", site.getTitle());
		assertEquals(0, site.getHierarchy());
		assertEquals(1, site.getPages().size());
		assertEquals(2, site.getSublevels().size());
		
		Level level_2 = site.getSublevels().get(1);
		assertEquals("2_level", level_2.getName());
		assertEquals(1, level_2.getSublevels().size());
		assertEquals(1, level_2.getHierarchy());
		Level level_2_1 = level_2.getSublevels().get(0);
		assertEquals(2, level_2_1.getHierarchy());
		
		assertEquals(2, level_2.getFamily().size());
	}
	
}
