package codes.thischwa.bacoma.model;

import java.util.UUID;

import codes.thischwa.bacoma.model.pojo.site.TemplateType;

/**
 * Interface to identify the renderable pojos.
 */
public interface IRenderable {
	public UUID getId();
	public UUID getTemplateID();
	public void setTemplateID(UUID uuid);
	public TemplateType getTemplateType();
}
