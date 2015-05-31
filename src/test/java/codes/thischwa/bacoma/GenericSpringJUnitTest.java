package codes.thischwa.bacoma;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import codes.thischwa.bacoma.rest.service._SiteBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/bacoma-rest.xml")
public class GenericSpringJUnitTest {

	protected static File testFolder = new File("test_temp");
	
	@BeforeClass 
	public static void setUpBeforeClass() throws Exception {
		new _SiteBuilder(testFolder);
	}
	
}
