package codes.thischwa.bacoma.rest.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
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

	public static String fetchContent(Part part, String key) {
		final String partHeader = part.getHeader("content-disposition");
		for(String content : partHeader.split(";")) {
			if(content.trim().startsWith(key)) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

	public static void write(Path reqPath, HttpServletResponse resp) throws IOException {
		if(!Files.exists(reqPath)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		InputStream in = Files.newInputStream(reqPath, StandardOpenOption.READ);
		write(in, resp);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	public static void write(String classPathPath, HttpServletResponse resp) throws IOException {
		Resource requestedResource = new ClassPathResource(classPathPath);
		if(!requestedResource.exists()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			throw new FileNotFoundException(requestedResource.getURI().toString());
		}
		MediaType mt = ServletUtil.parseMediaType(classPathPath);
		resp.setContentType(mt.toString());
		write(requestedResource.getInputStream(), resp);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	private static void write(InputStream in, HttpServletResponse resp) throws IOException {
		OutputStream out = resp.getOutputStream();
		int len = FileCopyUtils.copy(in, out);
		resp.setContentLength(len);
		out.flush();
	}
}
