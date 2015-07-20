package codes.thischwa.bacoma.rest.model;

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
		assertEquals(3, site.getSublevels().size());
		
		Level level_3 = site.getSublevels().get(2);
		assertEquals("3_level", level_3.getName());
		assertEquals(2, level_3.getSublevels().size());
		assertEquals(1, level_3.getHierarchy());
		Level level_2_1 = level_3.getSublevels().get(0);
		assertEquals(2, level_2_1.getHierarchy());
		
		assertEquals(3, level_3.getFamily().size());
	}
	
}
