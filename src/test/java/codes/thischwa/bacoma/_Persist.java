package codes.thischwa.bacoma;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import codes.thischwa.bacoma.rest.model.pojo.site.Content;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.model.pojo.site.Template;
import codes.thischwa.bacoma.rest.model.pojo.site.TemplateType;
import codes.thischwa.bacoma.rest.service.ContextUtility;
import codes.thischwa.bacoma.rest.service.Persister;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/bacoma-rest.xml")
public class _Persist {


	@Autowired
	private ContextUtility cu;
	
	@Test
	public void doPersist() throws IOException {
		Persister persister  = new Persister();
		Site site = new Site();
		site.setId(UUID.fromString("d20e9e25-7e34-4411-a70e-90104b8d60d0"));
		site.setUrl("site.test");
		site.setTitle("A Test Site");
		site.setPages(Arrays.asList(new Page[]{buildPage("frontpage", "welcome", "<h1>This is the welcome page&nbsp;...</h1><p>... of the site object!</p>")}));
		site.setLayoutTemplate(buildLayoutTemplate());
		//site.setConfiguration(buildConfig());
		site.addTemplate(buildPageTemplate());
		persister.persist(cu.getDataDir(), "admin", site);
	}
	
	private Page buildPage(String name, String title, String content) {
		Content c = new Content();
		c.setValue(content);
		c.setId(UUID.randomUUID());
		Page page = new Page();
		page.setId(UUID.fromString("db47c9ac-3796-435d-ba15-fac8bf64deaf"));
		page.setName(name);
		page.setTitle(title);
		page.setContent(Arrays.asList(new Content[]{c}));
		page.setTemplateID(UUID.fromString("707cc97a-b1a3-4278-bce5-acd3874ba527"));
		return page;
	}
	
	private Template buildLayoutTemplate() {
		Template t = new Template();
		t.setId(UUID.fromString("f597e595-c1a5-406f-bf7d-467bbfe40631"));
		t.setType(TemplateType.LAYOUT);
		String text =
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + 
				"\n" + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">\n" + 
				"<head>\n" + 
				"  <title>Test!</title>\n" + 
				"  <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n" + 
				"</head>\n" + 
				"\n" + 
				"<body>\n" + 
				"<div id=\"body\">\n" + 
				"<div id=\"nested\">\n" + 
				"<div id=\"header\">\n" + 
				"  <h1>This is the header.</h1>\n" + 
				"  <p>It is an uneditable text and is hard-coded in the layout.html.</p>\n" + 
				"</div>\n" + 
				"\n" + 
				"<!-- ********** begin content area ********** -->\n" + 
				"\n" + 
				"$content\n" + 
				"\n" + 
				"<!-- ********** end content area ********** -->\n" + 
				"\n" + 
				"<div id=\"footer\">\n" + 
				"  <p>generated by BaCoMa</p>\n" + 
				"</div>\n" + 
				"</div>\n" + 
				"</div>\n" + 
				"</body>\n" + 
				"</html>";
		t.setText(text);
		return t;
	}
	
	private Template buildPageTemplate() {
		Template t = new Template();
		t.setId(UUID.fromString("707cc97a-b1a3-4278-bce5-acd3874ba527"));
		t.setType(TemplateType.PAGE);
		t.setName("Page tamplate");
		String text = "#parse('content.vm')\n" + 
				"##parse('user_menu.vm')\n" + 
				"\n" + 
				"<div id=\"content\">\n" + 
				"  #contentview(\"field1\" 605 520)\n" + 
				"#pagemenu()\n" + 
				"</div>";
		t.setText(text);
		return t;
	}
	
	private Map<String, String> buildConfig() {
		Map<String, String> config = new HashMap<>();
		config.put("velocity.string.resource.loader.description", "Velocity StringResourceLoader");
		return config;
	}
}
