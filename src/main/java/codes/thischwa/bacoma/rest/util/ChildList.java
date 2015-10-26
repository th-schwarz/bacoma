package codes.thischwa.bacoma.rest.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;

/**
 * Helper object to set the parent of children automatically.
 * 
 * @param <P> type of the parent
 * @param <C> type of the children
 */
public class ChildList<P extends AbstractBacomaObject<?>, C extends AbstractBacomaObject<P>> {
	private P parent;
	private List<C> children = new ArrayList<>();
	
	public ChildList(P parent) {
		this.parent = parent;
	
	}
	public List<C> get() {
		return children;
	}
	
	public void set(List<C> children) {
		this.children.clear();
		addAll(children);
	}
	
	public boolean add(C c) {
		c.setParent(parent);
		return children.add(c);
	}
	
	public boolean remove(C c) {
		return children.remove(c);
	}
	
	public void add(int index, C element) {
		element.setParent(parent);
		children.add(index, element);
	}
	
	public boolean addAll(Collection<? extends C> c) {
		for(C bo : c) 
			bo.setParent(parent);
		return children.addAll(c);
	}
	
	public boolean addAll(int index, Collection<? extends C> c) {
		for(C bo : c) 
			bo.setParent(parent);
		return children.addAll(index, c);
	}
}
