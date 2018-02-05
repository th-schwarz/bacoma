package codes.thischwa.bacoma.rest.render.context.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.Constants;

/**
 * Wrapper object for {@link URI} to find out, if a link is an internal or an external one. 
 * The parameters are decoded with 'default.encoding'.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Link {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private boolean isExternal = false;
	private boolean isMailto = false;
	private boolean isFile = false;
	private String path = null;
	private Map<String, String> parameters = new HashMap<>();


	public void init(final String link) {
		try {
			URI uri = new URI(link);
			String schema = uri.getScheme();
			if (schema != null) {
				if (schema.equalsIgnoreCase("file")) {
					isFile = true;
				} else if (schema.equalsIgnoreCase("javascript")) {
					isExternal = true;
				} else if (schema.equalsIgnoreCase("mailto")) {
					isMailto = true;
					isExternal = true;
//				} else if (schema.equalsIgnoreCase("http") && uri.getHost() != null && uri.getHost().equalsIgnoreCase(host)) {
//					isExternal = false;
				} else if (schema.equalsIgnoreCase("http") || schema.equalsIgnoreCase("https") || schema.equalsIgnoreCase("ftp") || schema.equalsIgnoreCase("ftps"))
					isExternal = true;
			}
			path = uri.getPath();
		} catch (Exception e) {
			logger.warn("While trying to get the URI: " + e.getMessage(), e);
		}

		try {
			URL url = new URL(link);
			if (path == null)
				path = url.getPath();
			String query = url.getQuery();
			if (query != null) {
				String[] parameterPairs = StringUtils.split(query, "&");
				for (String parameterPair : parameterPairs) {
					if (StringUtils.isNotBlank(parameterPair)) {
						KeyValue kv = new KeyValue(parameterPair);
						String val = decodeQuietly(kv.val);
						parameters.put(kv.key, val);
					}
				}
			}
		} catch (Exception e) {
			if (path == null)
				throw new IllegalArgumentException(e);
		}
	}

	public boolean isExternal() {
		return isExternal;
	}

	public boolean hasParameter() {
		return !parameters.isEmpty();
	}

	public Map<String, String> getParameters() {
		return this.parameters;
	}

	public String getParameter(final String name) {
		return parameters.get(name);
	}

	public String getPath() {
		return path;
	}

	public boolean isMailTo() {
		return isMailto;
	}
	
	public boolean isFile() {
		return isFile;
	}
	
	public boolean pathStartsWith(String str) {
		return (path != null && path.startsWith("/"+str));
	}
	
	private String decodeQuietly(String str) {
		try {
			return URLDecoder.decode(str, Constants.DEFAULT_CHARSET.displayName());
		} catch (UnsupportedEncodingException e) {
			return "Couldn't decode! Charset not found!";
		}
	}
	
//	public static String buildUrl(final String baseUrl, final AbstractBacomaObject<?> po, final Action action) {
//		if(po == null || action == null)
//			throw new IllegalArgumentException("all params must be set");
//		StringBuilder sb = new StringBuilder(baseUrl);
//		if(!baseUrl.endsWith("/"))
//			sb.append("/");
//		sb.append(action.getName());
//		sb.append("?");
//		sb.append("id=").append(po.getId());
//		sb.append("&");
//		sb.append(Constants.LINK_TYPE_DESCRIPTOR).append("=").append(ContextUtil.getTypDescriptor(po.getClass()));
//		return sb.toString();
//	}
	
	
	private class KeyValue {
		String key;
		String val = "";

		KeyValue(String pair) {
			int pos = pair.indexOf("=");
			if (pos == -1) {
				key = pair;
				return;
			}
			key = pair.substring(0, pos);
			if (pos < pair.length() - 1)
				val = pair.substring(pos + 1, pair.length());
		}
	}
}
