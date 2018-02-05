package codes.thischwa.bacoma.rest.render;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.AbstractBacomaTest;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.service.Persister;
import codes.thischwa.bacoma.rest.service.SiteManager;

public class TestVelocityRenderer extends AbstractBacomaTest {
	
	@Autowired
	private VelocityRenderer velocityRenderer;

	@Autowired
	private SiteManager siteManager;

	@Before
	public void setUp() throws Exception {
		Persister persister = new Persister();
		Site site = persister.load(testFolder, "site.test");
		siteManager.init(site);
	}

	@Test
	public void testRenderString() {
		Map<String, Object> ctxObjs = new HashMap<>();
		ctxObjs.put("test", "this is a test");
		assertEquals("Msg: this is a test", velocityRenderer.renderString("Msg: $test", ctxObjs));
	}
}
