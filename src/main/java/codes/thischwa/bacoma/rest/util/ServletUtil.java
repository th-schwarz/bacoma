package codes.thischwa.bacoma.rest.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServletUtil {
	private static ConfigurableMimeFileTypeMap mimeTypeMap = new ConfigurableMimeFileTypeMap();
	public static final MediaType MEDIATYPE_TEXT_CSS = new MediaType("text", "css");
	
	static {
		mimeTypeMap.setMappings("application/json json JSON");
	}
	
	public static String getMimeType(String fileName) {
		return mimeTypeMap.getContentType(fileName);
	}
	
	public static MediaType parseMediaType(String path) {
		String mimeType = getMimeType(path);
		if(mimeType.equals("text/css"))
			return MEDIATYPE_TEXT_CSS;
		return MediaType.parseMediaType(mimeType);
	}
	
	public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }
	
	public static String getBaseUrl() {
		HttpServletRequest req = getCurrentRequest();
		StringBuffer url = req.getRequestURL();
		String uri = req.getRequestURI();
		String ctx = req.getContextPath();
		String base = url.substring(0, url.length() - uri.length() + ctx.length()) + "/";
		return base;
	}
	
	public static void establishContentType(String fileName, HttpServletResponse resp) {
		if(StringUtils.isBlank(fileName))
			return;
		String contentType = getMimeType(fileName);
		if(StringUtils.isNotBlank(contentType))
			resp.setContentType(contentType);
	}
}
