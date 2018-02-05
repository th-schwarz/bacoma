package codes.thischwa.bacoma.rest.model.pojo.site;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import codes.thischwa.bacoma.rest.model.IOrderable;

/**
 * Base object for the level of a {@link Site}, a structural container element with no content. 
 * {@link Site} is inherited from this object.
 */
public class Level extends AbstractBacomaObject<Level> implements IOrderable<Level> {
	protected String title;
	protected ChildList<Level, Level> sublevels = new ChildList<>(this);
	protected ChildList<Level, Page> pages = new ChildList<>(this);
		
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
		this.sublevels.set(sublevels);
	}
	
	public boolean hasSublevels() {
		return sublevels.get().size() > 0;
	}
	public void add(Level level) {
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

	@JsonIgnore
	@Override
	public List<Level> getFamily() {
		if(getParent() == null)
			return new ArrayList<>();
		return getParent().getSublevels();
	}
}
