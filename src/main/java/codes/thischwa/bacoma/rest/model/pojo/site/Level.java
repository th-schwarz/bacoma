package codes.thischwa.bacoma.rest.model.pojo.site;

import java.util.ArrayList;
import java.util.List;

import codes.thischwa.bacoma.rest.model.IOrderable;
import codes.thischwa.bacoma.rest.util.ChildList;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base object for the level of a {@link Site}, a structural container element with no content. 
 * {@link Site} is inherited from this object.
 */
public class Level extends AbstractBacomaObject<Level> implements IOrderable<Level> {
	protected String name;
	protected String title;
	protected ChildList<Level, Level> sublevels = new ChildList<>(this);
	protected ChildList<Level, Page> pages = new ChildList<>(this);
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<Level> getSublevels() {
		return sublevels.get();
	}
	public void setSublevels(List<Level> sublevels) {
		this.setSublevels(sublevels);
	}
	
	public boolean hasSublevels() {
		return sublevels.get().size() > 0;
	}
	public void add(Level level) {
		// TODO check if there is already a level with the same name
		sublevels.add(level);
	}
	public boolean remove(Level level) {
		return sublevels.remove(level);
	}
	
	public List<Page> getPages() {
		return pages.get();
	}
	public void setPages(List<Page> pages) {
		this.pages.set(pages);
	}
	public void add(Page page) {
		// TODO check if there is already a page with the same name
		pages.add(page);
	}
	public boolean remove(Page page) {
		return pages.remove(page);
	}
		
	/**
	 * @return The number of the hierarchy. (Starts with 1, site has hierarchy 0.)
	 */
	@JsonIgnore
	public int getHierarchy() {
		return (getParent() == null) ? 0 : (getParent().getHierarchy() + 1);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@JsonIgnore
	@Override
	public List<Level> getFamily() {
		if(getParent() == null)
			return new ArrayList<>();
		return getParent().getSublevels();
	}
}
