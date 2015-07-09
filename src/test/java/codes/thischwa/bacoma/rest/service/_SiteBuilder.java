package codes.thischwa.bacoma.rest.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import codes.thischwa.bacoma.rest.model.pojo.site.CascadingStyleSheet;
import codes.thischwa.bacoma.rest.model.pojo.site.Content;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.model.pojo.site.Template;
import codes.thischwa.bacoma.rest.model.pojo.site.TemplateType;

public class _SiteBuilder {

	public _SiteBuilder(File jsonFile) throws Exception {
		Persister persister  = new Persister();
		Site site = new Site();
		site.setId(UUID.fromString("d20e9e25-7e34-4411-a70e-90104b8d6000"));
		site.setUrl("site.test");
		site.setTitle("A Test Site");
		site.getPages().add(buildPage("db47c9ac-3796-435d-ba15-fac8bf64deaf", "707cc97a-b1a3-4278-bce5-acd3874ba527", "frontpage", "welcome", "<h1>This is the welcome page&nbsp;...</h1><p>... of the site object!</p>"));
		site.setLayoutTemplate(buildLayoutTemplate());
		site.setConfiguration(buildConfig());
		site.addTemplate(buildPageTemplate());
		site.getCascadingStyleSheets().add(builtStyleSheet());
		
		Level lev_1 = buildLevel("d20e9e25-7e34-4411-a70e-90104b8d6001", site, "1_level", "1st Level");
		site.add(lev_1);
		lev_1.add(buildPage("d20e9e25-7e34-4411-a70e-90104b8d6010", "707cc97a-b1a3-4278-bce5-acd3874ba527", "frontpage", "welcome", "<h1>This is the welcome page&nbsp;...</h1><p>... of the site object!</p>"));
		Level lev_2 = buildLevel("d20e9e25-7e34-4411-a70e-90104b8d6002", site, "2_level", "2nd Level");
		site.add(lev_2);
		Level lev_2_1 = buildLevel("d20e9e25-7e34-4411-a70e-90104b8d6003", site, "2_1_level", "1st sub-level of the 2nd Level");
		lev_2.add(lev_2_1);
		
		persister.persist(jsonFile, site);
	}
	
	private Level buildLevel(String id, Level parent, String name, String title) {
		Level l = new Level();
		l.setId(UUID.fromString(id));
		l.setName(name);
		l.setTitle(title);
		return l;
	}
	
	private Page buildPage(String id, String templateID, String name, String title, String content) {
		Content c = new Content();
		c.setValue(content);
		Page page = new Page();
		page.setId(UUID.fromString(id));
		page.setName(name);
		page.setTitle(title);
		page.setContent(Arrays.asList(new Content[]{c}));
		page.setTemplateID(UUID.fromString(templateID));
		return page;
	}
	
	private Template buildLayoutTemplate() throws Exception {
		Template t = new Template();
		t.setId(UUID.fromString("f597e595-c1a5-406f-bf7d-467bbfe40631"));
		t.setType(TemplateType.LAYOUT);
		t.setText(IOUtils.toString(_SiteBuilder.class.getResource("/demo.site/layout.template"), "utf-8"));
		return t;
	}
	
	private Template buildPageTemplate() throws Exception {
		Template t = new Template();
		t.setId(UUID.fromString("707cc97a-b1a3-4278-bce5-acd3874ba527"));
		t.setType(TemplateType.PAGE);
		t.setName("Page tamplate");
		t.setText(IOUtils.toString(_SiteBuilder.class.getResource("/demo.site/page.template"), "utf-8"));
		return t;
	}
	
	private CascadingStyleSheet builtStyleSheet() throws Exception {
		CascadingStyleSheet styleSheet = new CascadingStyleSheet();
		styleSheet.setId(UUID.fromString("d20e9e25-7e34-4411-a70e-90104b8d6100"));
		styleSheet.setName("format.css");
		styleSheet.setText(IOUtils.toString(_SiteBuilder.class.getResource("/demo.site/format.css"), "utf-8"));
		return styleSheet;
	}
	
	private Map<String, String> buildConfig() {
		Map<String, String> config = new HashMap<>();
		config.put("site.export.folder", "temp_export");
		return config;
	}
	
	public static void main(String[] args) throws Exception {
		new _SiteBuilder(new File("test_temp"));
	}
}
