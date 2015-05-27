package codes.thischwa.bacoma.rest.render.context.object.tagtool;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Helper to construct a common xhtml-tag.
 */
public class GenericXhtmlTagTool {
	protected Map<String, String> attributes = new HashMap<>();
	private String name = null;
	private String value = null;
	
	private boolean usedFromEditor = false;
	
	protected GenericXhtmlTagTool(final String name) {
		if(StringUtils.isBlank(name))
			throw new IllegalArgumentException("The name of the tag must be set!");
		this.name = name;
	}
	
	protected void setValue(final String value) {
		this.value = value;
	}
	
	protected void putAttr(final String key, final String value) {
		attributes.put(key, value);
	}
	
	protected String getAttr(final String key) {
		return attributes.get(key);
	}
	
	protected boolean isUsedFromEditor() {
		return usedFromEditor;
	}
	
	/**
	 * Setter to signal that the inherited tag-tool was used inside the wysiwyg-editor. 
	 * For internal use only! 
	 * 
	 * @param usedFromEditor
	 */
	protected void setUsedFromEditor(boolean usedFromEditor) {
		this.usedFromEditor = usedFromEditor;
	}
		
	/**
	 * Does the basic construction of the tag.
	 */
	protected String contructTag() {
		if (attributes.isEmpty())
			throw new IllegalArgumentException("Attributes are missing!");
		StringBuilder tag = new StringBuilder();
		tag.append("<");
		tag.append(name);
		for (String key : attributes.keySet()) {
			tag.append(String.format(" %s=\"%s\"", key, StringUtils.defaultString(attributes.get(key))));
		}
		if (StringUtils.isBlank(value))
			tag.append(" />");
		else 
			tag.append(">").append(value).append('<').append('/').append(name).append('>');
		
		attributes.clear();
		return tag.toString();
	}
}
