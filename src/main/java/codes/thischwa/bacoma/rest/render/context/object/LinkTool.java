package codes.thischwa.bacoma.rest.render.context.object;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.render.context.IContextObjectCommon;

/**
 * Context object for building query strings for links. <br>
 * <pre>$linktool.addParameter('id', '1').addParameter('name', 'foo')</pre> generates: id=1&name=foo 
 */
@Component("linktool")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LinkTool implements IContextObjectCommon {
	private String linkTo = null;
	private Map<String, String> params = new HashMap<>();
	
	public LinkTool addParameter(String key, String value) {
		this.params.put(key, value);
		return this;
	}

	public LinkTool addParameter(String key, Serializable value) {
		addParameter(key, value.toString());
		return this;
	}

	@Override
	public String toString() {
		StringBuilder link = new StringBuilder();
		String paramStr = null;

		if (this.linkTo == null)
			return "error_in_linktool-destination_not_set";

		if (!this.params.isEmpty()) {
			String amp = "&amp;";
			link.append("?");
			for (String key : this.params.keySet()) {
				link.append(key);
				link.append("=");
				link.append(this.params.get(key));
				link.append(amp);
			}
			paramStr = link.substring(0, link.length() - amp.length());
		}
		String linkString = this.linkTo.concat(StringUtils.defaultString(paramStr));
		clear();
		this.linkTo = "";
		return linkString;
	}

	private void clear() {
		this.params.clear();
	}
}
