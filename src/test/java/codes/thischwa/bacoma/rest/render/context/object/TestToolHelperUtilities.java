package codes.thischwa.bacoma.rest.render.context.object;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import codes.thischwa.bacoma.GenericSpringJUnitTest;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;

public class TestToolHelperUtilities extends GenericSpringJUnitTest {
	
	private Level lev1;
	private Level Lev3_sub1;

	@Before
	@Override
	public void init() throws Exception {
		super.init();
		lev1 = (Level) siteManager.getObject(UUID.fromString("d20e9e25-0002-0001-0000-000000000000"));
		Lev3_sub1 = (Level) siteManager.getObject(UUID.fromString("d20e9e25-0002-0003-0010-000000000000"));
	}
	
	@Test
	public void testGetURLRelativePathToRoot() {
		assertEquals("../", ToolHelperUtilities.getURLRelativePathToRoot(lev1));
		assertEquals("../../", ToolHelperUtilities.getURLRelativePathToRoot(Lev3_sub1));
	}

	@Test
	public void testGetURLRelativePathToLevel() {
		assertEquals("../3_level/1_sub/", ToolHelperUtilities.getURLRelativePathToLevel(lev1, Lev3_sub1));
	}

}
