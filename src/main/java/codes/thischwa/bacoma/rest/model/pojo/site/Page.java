package codes.thischwa.bacoma.rest.model.pojo.site;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import codes.thischwa.bacoma.rest.model.IOrderable;
import codes.thischwa.bacoma.rest.model.IRenderable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Base object for a page.
 * {@link Gallery} is inherited from this object. 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page extends AbstractBacomaObject<Level> implements IRenderable, IOrderable<Page> {
	protected String name;
	protected String title;
	private UUID templateID;
	private List<Content> content = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public UUID getTemplateID() {
		return this.templateID;
	}
	
	@Override
	public void setTemplateID(UUID uuid) {
		templateID = uuid;
	}

	public List<Content> getContent() {
		return this.content;
	}

	public void setContent(List<Content> content) {
		this.content = content;
	}
	
	public void add(Content content) {
		this.content.add(content);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@JsonIgnore
	@Override
	public TemplateType getTemplateType() {
		return TemplateType.PAGE;
	}

	@Override
	public String toString() {
		return name;
	}

	@JsonIgnore
	@Override
	public List<Page> getFamily() {
		return getParent().getPages();
	}
}
