package codes.thischwa.bacoma.rest.render;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codes.thischwa.bacoma.AbstractBacomaTest;
import codes.thischwa.bacoma.rest.service.SiteManager;

public class TestExportRenderer extends AbstractBacomaTest {

	@Autowired
	private ExportRenderer exportRenderer;

	@Autowired
	private SiteManager siteManager;
	
	@Test
	public void test() throws IOException {
		siteManager.setViewMode(ViewMode.EXPORT);
		exportRenderer.render(siteManager);
	}

}
