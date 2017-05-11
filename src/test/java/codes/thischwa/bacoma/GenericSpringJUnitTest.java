package codes.thischwa.bacoma;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.render.ViewMode;
import codes.thischwa.bacoma.rest.service.Persister;
import codes.thischwa.bacoma.rest.service.SiteBuilder;
import codes.thischwa.bacoma.rest.service.SiteManager;
import codes.thischwa.bacoma.rest.util.FileSystemUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/bacoma-rest.xml")
public class GenericSpringJUnitTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	protected SiteManager siteManager;
	
	protected static File testFolder = new File("test_temp");
	
	public static final Path getDemoSitePath() {
		try {
			return Paths.get(GenericSpringJUnitTest.class.getResource("/demo.site/").toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	@BeforeClass 
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("dir.webapp", "webapp");
		new SiteBuilder(testFolder);
	}
	
	@Before
	public void init() throws IOException {
		try {
			FileSystemUtil fsu = applicationContext.getBean(FileSystemUtil.class);
			fsu.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Persister persister = new Persister();
		Site site = persister.load(testFolder, "site.test");
		siteManager.setViewMode(ViewMode.PREVIEW);
		siteManager.init(site);
	}
	
	@Test
	public void test()  throws Exception {
		Persister persister = new Persister();
		Site site = persister.load(testFolder, "site.test");
		assertNotNull(site);
	}
}
