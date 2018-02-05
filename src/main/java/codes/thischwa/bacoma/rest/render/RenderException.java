package codes.thischwa.bacoma.rest.render;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import codes.thischwa.bacoma.model.pojo.site.TemplateType;

public class RenderException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private TemplateType type;
	private UUID id;
	
	public RenderException(String message, Throwable cause) {
		super(message, cause);
	}

	public RenderException(TemplateType type, UUID id, String message, Throwable cause) {
		 this(buildMessage(type, id, message), cause);
	}

	public RenderException(TemplateType type, UUID id, Throwable cause) {
		 this(buildMessage(type, id, null), cause);
	}
	
	public RenderException(TemplateType type, UUID id, String message) {
		super(buildMessage(type, id, message));
	}
	
	public TemplateType getType() {
		return type;
	}
	
	public UUID getId() {
		return id;
	}
	
	private static String buildMessage(TemplateType type, UUID id, String message) {
		if(StringUtils.isBlank(message))
			return String.format("Rendering of %s#%s causes an exception.", type.toString(), id);
	
		return String.format("Rendering of %s#%s causes an exception: %s", type.toString(), id, message);
	}

}
