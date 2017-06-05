package codes.thischwa.bacoma.rest.model.pojo.site;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import codes.thischwa.bacoma.rest.model.IOrderable;
import codes.thischwa.bacoma.rest.model.IRenderable;

/**
 * Base object for a page.
 * {@link Gallery} is inherited from this object. 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page extends AbstractBacomaObject<Level> implements IRenderable, IOrderable<Page> {
	protected String name;
	protected String title;
	private UUID templateID;
	private Set<Content> content = new HashSet<>();

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

	public Set<Content> getContent() {
		return this.content;
	}

	public void setContent(Set<Content> content) {
		this.content = content;
	}
	
	public void add(Content contentItem) {
		String  currentContentName = contentItem.getName();
		for(Content c : this.content) {
			if(c.getName().equalsIgnoreCase(currentContentName))
				throw new IllegalArgumentException(String.format("A content-item#%s already exists!", currentContentName));
		}
		if(contentItem.getId() == null)
			contentItem.setId(UUID.randomUUID());
		this.content.add(contentItem);
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
