package codes.thischwa.bacoma.rest.util;

import static org.junit.Assert.*;

import org.junit.Test;

import codes.thischwa.bacoma.rest.util.ServletUtil;

public class TestServletUtil {

	@Test
	public void testGetMimeType() {
		assertEquals("image/x-png", ServletUtil.getMimeType("image.png"));
		assertEquals("image/jpeg", ServletUtil.getMimeType("image.jpg"));
		assertEquals("text/javascript", ServletUtil.getMimeType("file.js"));
		assertEquals("text/css", ServletUtil.getMimeType("format.css"));
		assertEquals("application/json", ServletUtil.getMimeType("file.json"));
	}

}
