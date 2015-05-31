package codes.thischwa.bacoma.Model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import codes.thischwa.bacoma.GenericSpringJUnitTest;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.service.Persister;

public class TestPojo extends GenericSpringJUnitTest {

	@Test
	public void testStructure() throws Exception {
		Persister persister = new Persister();
		Site site = persister.load(testFolder, "site.test");
		assertEquals("site.test", site.getUrl());
		assertEquals("A Test Site", site.getTitle());
		assertEquals(1, site.getPages().size());
		assertEquals(2, site.getSublevels().size());
		
	}
	
}
