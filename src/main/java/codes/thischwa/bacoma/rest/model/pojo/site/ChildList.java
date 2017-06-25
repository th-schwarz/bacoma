package codes.thischwa.bacoma.rest.model.pojo.site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper object to set the parent of children automatically. It ensures, that the names of the children are unique.
 * 
 * @param <P>
 *            type of the parent
 * @param <C>
 *            type of the children
 */
class ChildList<P extends AbstractBacomaObject<?>, C extends AbstractBacomaObject<P>> {
	private P parent;
	private List<C> children = new ArrayList<>();
	private Set<String> uniqueNames = new HashSet<>();

	ChildList(P parent) {
		this.parent = parent;
	}

	List<C> get() {
		return children;
	}

	void set(List<C> children) {
		this.children.clear();
		addAll(children);
	}

	boolean add(C c) {
		check(c);
		uniqueNames.add(c.getName());
		c.setParent(parent);
		return children.add(c);
	}

	boolean remove(C c) {
		uniqueNames.remove(c.getName());
		return children.remove(c);
	}

	void add(int index, C c) {
		check(c);
		uniqueNames.add(c.getName());
		c.setParent(parent);
		children.add(index, c);
	}

	boolean addAll(Collection<? extends C> children) {
		check(children);
		for(C c : children) {
			uniqueNames.add(c.getName());
			c.setParent(parent);
		}
		return this.children.addAll(children);
	}

	boolean addAll(int index, Collection<? extends C> children) {
		check(children);
		for(C c : children) {
			uniqueNames.add(c.getName());
			c.setParent(parent);
		}
		return this.children.addAll(index, children);
	}

	private void check(C c) {
		if(uniqueNames.contains(c.getName()))
			throw new IllegalArgumentException(String.format("The name of a child is already in use: %s", c.getName()));
	}

	private void check(Collection<? extends C> children) {
		for(C c : children) {
			check(c);
		}
	}
}
