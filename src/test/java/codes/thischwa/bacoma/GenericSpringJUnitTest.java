package codes.thischwa.bacoma;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.service.Persister;
import codes.thischwa.bacoma.rest.service.SiteManager;
import codes.thischwa.bacoma.rest.service._SiteBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/bacoma-rest.xml")
public class GenericSpringJUnitTest {
	
	@Autowired
	protected SiteManager siteManager;

	protected static File testFolder = new File("test_temp");
	
	@BeforeClass 
	public static void setUpBeforeClass() throws Exception {
		new _SiteBuilder(testFolder);
	}
	
	@Before
	public void init() throws Exception {
		Persister persister = new Persister();
		Site site = persister.load(testFolder, "site.test");
		siteManager.init(site);
	}
	
	@Test
	public void test()  throws Exception {
		Persister persister = new Persister();
		Site site = persister.load(testFolder, "site.test");
		assertNotNull(site);
	}
}
