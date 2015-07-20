package codes.thischwa.bacoma.rest.render.context.object.tagtool;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.GenericSpringJUnitTest;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.render.ViewMode;
import codes.thischwa.bacoma.rest.render.context.PojoHelper;
import codes.thischwa.bacoma.rest.render.context.object.SiteLinkTool;

public class TestSiteLinkTool extends GenericSpringJUnitTest {

	@Autowired
	private SiteLinkTool siteLinkTool;
	
	@Test
	public void testGetCss() {
		PojoHelper ph = new PojoHelper();
		AbstractBacomaObject<?> bo = siteManager.getObject(UUID.fromString("d20e9e25-0003-0000-0000-000000000001"));
		ph.putpo(bo);
		siteLinkTool.setPojoHelper(ph);
		siteLinkTool.setViewMode(ViewMode.PREVIEW);
		assertEquals("/resource/css/?name=format.css", siteLinkTool.getCss("format.css"));
		assertEquals("/resource/css/?name=format.css", siteLinkTool.getCss());
	}

	@Test
	public void testGetPage() {
		PojoHelper ph = new PojoHelper();
		AbstractBacomaObject<?> bo = siteManager.getObject(UUID.fromString("d20e9e25-0003-0000-0000-000000000001"));
		ph.putpo(bo);
		siteLinkTool.setPojoHelper(ph);
		siteLinkTool.setViewMode(ViewMode.PREVIEW);
		assertEquals("/render/get/d20e9e25-0003-0000-0000-000000000001", siteLinkTool.getPage((Page)bo));
	}
}
