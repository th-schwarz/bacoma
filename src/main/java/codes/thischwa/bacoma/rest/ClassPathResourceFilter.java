package codes.thischwa.bacoma.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class ClassPathResourceFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(ClassPathResourceFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String path = req.getServletPath();
		InputStream in = this.getClass().getResourceAsStream(path);
		OutputStream out = null;
		try {
			if(in == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				logger.warn("Requested path not found: {}", path);
			} else {
				out = resp.getOutputStream(); // shouldn't be flushed, because of the filter-chain
				IOUtils.copy(in, out);
			}
		} catch (IOException e) {
			logger.warn("Error while reading requested resource: ", path, e);
		} finally {
			IOUtils.closeQuietly(out);
		}

	}

}
