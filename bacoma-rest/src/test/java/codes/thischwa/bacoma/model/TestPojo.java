package codes.thischwa.bacoma.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import codes.thischwa.bacoma.AbstractBacomaTest;
import codes.thischwa.bacoma.model.pojo.site.Level;
import codes.thischwa.bacoma.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.service.Persister;

public class TestPojo extends AbstractBacomaTest {

	private Persister persister = new Persister();

	private Site site;

	@Before
	public void initMe() throws IOException {
		site = persister.load(testFolder, "site.test");
	}

	@Test
	public void testStructure() {
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

	@Test(expected = IllegalArgumentException.class)
	public void testWrongStructure() throws Exception {
		Level l = new Level();
		l.setId(UUID.randomUUID());
		l.setName("1_level");
		site.add(l);
	}
}
