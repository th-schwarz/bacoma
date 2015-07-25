package codes.thischwa.bacoma.rest.service;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import codes.thischwa.bacoma.rest.model.pojo.site.CascadingStyleSheet;
import codes.thischwa.bacoma.rest.model.pojo.site.Content;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.Macro;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.model.pojo.site.Template;
import codes.thischwa.bacoma.rest.model.pojo.site.TemplateType;

/**
 * UUID-usage:
 * - d20e9e25-0000-0000-0000-000000000000 : site
 * - d20e9e25-0001-0000-0000-000000000 : site-resources
 * - d20e9e25-0002-0000-0000-000000000 : levels
 * - d20e9e25-0003-0000-0000-000000000 : pages
 */
public class _SiteBuilder {
	
	final String commonPageTemplateID = "d20e9e25-0001-0000-0000-000000000001";

	public _SiteBuilder(File jsonFile) throws Exception {
		Persister persister  = new Persister();
		Site site = new Site();
		site.setId(UUID.fromString("d20e9e25-0000-0000-0000-000000000000"));
		site.setUrl("site.test");
		site.setTitle("A Test Site");
		site.getPages().add(buildPage("d20e9e25-0003-0000-0000-000000000001", commonPageTemplateID, "frontpage", "Welcome", load("welcome.page")));
		site.setLayoutTemplate(buildLayoutTemplate());
		site.setConfiguration(buildConfig());
		site.getTemplates().add(buildPageTemplate());
		site.getCascadingStyleSheets().add(builtStyleSheet());
		site.getMacros().add(buildMacro());
		
		Level lev_1 = buildLevel("d20e9e25-0002-0001-0000-000000000000", site, "1_level", "Level with 1 page");
		site.add(lev_1);
		lev_1.getPages().add(buildPage("d20e9e25-0003-0000-0000-000000000010", commonPageTemplateID, "frontpage", null, load("lev1_1.page")));
		
		Level lev_2 = buildLevel("d20e9e25-0002-0002-0000-000000000000", site, "2_level", "Level with pages");
		site.add(lev_2);
		lev_2.getPages().add(buildPage("d20e9e25-0003-0000-0000-000000000021", commonPageTemplateID, "page1", null, load("lev2_1.page")));
		lev_2.getPages().add(buildPage("d20e9e25-0003-0000-0000-000000000022", commonPageTemplateID, "page2", null, load("lev2_2.page")));
		lev_2.getPages().add(buildPage("d20e9e25-0003-0000-0000-000000000023", commonPageTemplateID, "page3", null, load("lev2_3.page")));
		
		Level lev_3 = buildLevel("d20e9e25-0002-0003-0000-000000000000", site, "3_level", "Level with Levels");
		site.add(lev_3);
		lev_3.getPages().add(buildPage("d20e9e25-0003-0000-0000-000000000030", commonPageTemplateID, "frontpage", null, load("lev3_1.page")));
		
		Level lev_3_1 = buildLevel("d20e9e25-0002-0003-00010-000000000000", site, "1_sub", "Sublevel 1");
		lev_3.add(lev_3_1);
		lev_3_1.getPages().add(buildPage("d20e9e25-0003-0000-0000-000000000031", commonPageTemplateID, "frontpage", null, load("lev3-1_1.page")));

		Level lev_3_2 = buildLevel("d20e9e25-0002-0003-00020-000000000000", site, "2_sub", "Sublevel 2");
		lev_3.add(lev_3_2);
		lev_3_2.getPages().add(buildPage("d20e9e25-0003-0000-0000-000000000032", commonPageTemplateID, "frontpage", null, load("lev3-1_2.page")));
		
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
		c.setName("content");
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
		t.setId(UUID.fromString("d20e9e25-0001-0000-0000-000000000010"));
		t.setType(TemplateType.LAYOUT);
		t.setText(load("layout.template"));
		return t;
	}
	
	private Template buildPageTemplate() throws Exception {
		Template t = new Template();
		t.setId(UUID.fromString("d20e9e25-0001-0000-0000-000000000001"));
		t.setType(TemplateType.PAGE);
		t.setName("Page tamplate");
		t.setText(load("page.template"));
		return t;
	}
	
	private CascadingStyleSheet builtStyleSheet() throws Exception {
		CascadingStyleSheet styleSheet = new CascadingStyleSheet();
		styleSheet.setId(UUID.fromString("d20e9e25-0001-0000-0001-000000000000"));
		styleSheet.setName("format.css");
		styleSheet.setText(load("format.css"));
		return styleSheet;
	}
	
	private Macro buildMacro() throws Exception {
		Macro vm = new Macro();
		vm.setName("menu.vm");
		vm.setId(UUID.fromString("d20e9e25-0001-0000-0002-000000000000"));
		vm.setText(load("menu.vm"));
		return vm;
	}
	
	private String load(String path) throws Exception {
		return IOUtils.toString(_SiteBuilder.class.getResource(String.format("/demo.site/%s", path)), "utf-8");
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
