package codes.thischwa.bacoma.jetty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codes.thischwa.bacoma.rest.util.ServletUtil;

public class StaticServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(StaticServlet.class); 

	private static final long serialVersionUID = 1L;
	
	private Path basePath;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		basePath = Paths.get(config.getInitParameter("basePath"));
		if(!Files.exists(basePath))
			throw new ServletException("'basePath' not found!");
		logger.info("ResourceServlet initialized for the directory: " + basePath);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if(pathInfo.startsWith("/"))
			pathInfo = pathInfo.substring(1);
		Path reqPath = basePath.resolve(pathInfo);
		logger.debug("Requested path: {}", reqPath);
		ServletUtil.write(reqPath, resp);
	}
}
