package codes.thischwa.bacoma.rest.util;

import static org.junit.Assert.*;

import org.junit.Test;

import codes.thischwa.bacoma.model.pojo.site.SiteResourceType;
import codes.thischwa.bacoma.model.util.EnumUtil;

public class TestEnumUtil {

	@Test
	public void testValueOfIgnoreCase() {
		 SiteResourceType expected = SiteResourceType.CSS;
		 assertEquals(expected, EnumUtil.valueOfIgnoreCase(SiteResourceType.class, "CsS"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void unkmnownEnum() {
		EnumUtil.valueOfIgnoreCase(SiteResourceType.class, "AnUnknownEnum");
	}
}
