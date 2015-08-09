package codes.thischwa.bacoma.rest.render.context.object.tagtool;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.GenericSpringJUnitTest;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.render.ViewMode;
import codes.thischwa.bacoma.rest.render.context.object.SiteLinkTool;
import codes.thischwa.bacoma.rest.service.SiteBuilder;

public class TestSiteLinkTool extends GenericSpringJUnitTest {

	@Autowired
	private SiteLinkTool siteLinkTool;
	
	@Test
	public void testGetCss() {
		assertEquals("/site/resource/css/?name=format.css", siteLinkTool.getCss("format.css"));
		assertEquals("/site/resource/css/?name=format.css", siteLinkTool.getCss());
	}

	@Test
	public void testGetCssExport() {
		siteManager.setViewMode(ViewMode.EXPORT);
		Page page = (Page) siteManager.getObject(SiteBuilder.uuidWelcomePage);
		siteLinkTool.setRenderable(page);
		assertEquals("index.html", siteLinkTool.getCss());
	}

	@Test
	public void testGetPage() {
		AbstractBacomaObject<?> bo = siteManager.getObject(SiteBuilder.uuidWelcomePage);
		assertEquals(String.format("/site/render/get/%s", SiteBuilder.uuidWelcomePage), 
				siteLinkTool.get((Page)bo));
	}
	
	@Test
	public void testGetPageExport() {
		siteManager.setViewMode(ViewMode.EXPORT);
		Page page = (Page) siteManager.getObject(SiteBuilder.uuidWelcomePage);
		siteLinkTool.setRenderable(page);
		assertEquals("index.html", siteLinkTool.get(page));
		
		page = (Page) siteManager.getObject(SiteBuilder.uuidLev3Sub2Page1);
		assertEquals("3_level/2_sub/index.html", siteLinkTool.get(page));
		
		siteLinkTool.setRenderable(page);
		page = (Page) siteManager.getObject(SiteBuilder.uuidWelcomePage);
		assertEquals("../../index.html", siteLinkTool.get(page));
	}
}
