package codes.thischwa.bacoma.jetty;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codes.thischwa.bacoma.rest.util.ServletUtil;

public class ZipProxyServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(ZipProxyServlet.class);
	private static final long serialVersionUID = 1L;
	private String zipPathToSkip = null;
	private Map<String, ZipEntry> zipInfo;
	private ZipFile zipFile;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		zipPathToSkip = config.getInitParameter("zipPathToSkip");
		String fileParam = config.getInitParameter("file");
		if (StringUtils.isBlank(fileParam))
			throw new IllegalArgumentException("No file parameter found!");
		File file = null;
		zipInfo = new HashMap<String, ZipEntry>();
		try {
			file = new File(config.getInitParameter("file"));
			if(!file.exists()) {
				throw new ServletException(String.format("Zip-file not found: %s", file.getPath()));
			}
			zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry ze = entries.nextElement();
				String entryName = ze.getName();
				if (zipPathToSkip != null)
					entryName = entryName.substring(zipPathToSkip.length()+1);
				if (entryName.startsWith("/"))
					entryName = entryName.substring(1);
				zipInfo.put(entryName, ze);
			}
		} catch (IOException e) {
			throw new ServletException("Couldn't read the zip file: " + e.getMessage(), e);
		}
		logger.info("ZipProxyServlet initialized for the file {}, path to skip is {}, entries found {}.", 
				file.getPath(), zipPathToSkip, zipFile.size());
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String reqPath = req.getPathInfo();
		if (reqPath.startsWith("/"))
			reqPath = reqPath.substring(1);
		
		ZipEntry entry = zipInfo.get(reqPath);
		if (entry == null) {
			logger.debug("Requested path not found: {}", reqPath);
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		logger.debug("Requested path: {}", reqPath);
		
		ServletUtil.establishContentType(reqPath, resp);
		
		InputStream in = null;
		try {
			in = new BufferedInputStream(zipFile.getInputStream(entry));
			IOUtils.copy(in, resp.getOutputStream());
			logger.debug("Rendered: " + reqPath);
		} catch (FileNotFoundException e) {
			logger.error("zipped resource not found: " + reqPath);
		} finally {
			IOUtils.closeQuietly(in);
		}		
	}
	
	@Override
	public void destroy() {
		super.destroy();
		IOUtils.closeQuietly(zipFile);
	}
}