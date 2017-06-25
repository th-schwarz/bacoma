package codes.thischwa.bacoma.rest.controller;

import static org.junit.Assert.*;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.AbstractBacomaTest;
import codes.thischwa.bacoma.rest.controller.RenderController;

public class TestRenderController extends AbstractBacomaTest {

	@Autowired
	private RenderController controller;
	
	@Test
	public void testFixLinksForPreview() throws Exception {
		String html = IOUtils.toString(this.getClass().getResourceAsStream("fixLinks_actual.html"));
		String actual = controller.fixLinksForPreview(siteManager.getSite(), html);
		String expected = IOUtils.toString(this.getClass().getResourceAsStream("fixLinks_expected.html"));
		assertEquals(expected, actual);
	}

	@Test
	public void testBuildSiteResourceLink() {
		assertEquals("/site/site.test/resource/static/get?path=/sub/test.zip", controller.buildSiteResourceLink(siteManager.getSite(), "/sub/test.zip"));
	}

}
