package codes.thischwa.bacoma.model.pojo.site;

import org.apache.commons.lang3.StringUtils;

import codes.thischwa.bacoma.model.IRenderable;

/**
 * Type of template for {@link IRenderable}s.
 */
public enum TemplateType {
	LAYOUT,
	PAGE,
	GALLERY,
	IMAGE;
		
	public static TemplateType getType(final String name) {
		if (StringUtils.isEmpty(name))
			return null;
		for (TemplateType type : TemplateType.values()) {
			if (type.toString().toLowerCase().equals(name.toLowerCase()))
				return type;
		}
		return null;
	}
}
