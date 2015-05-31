package codes.thischwa.bacoma.rest.model;

import java.util.List;

import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;

/**
 * All {@link codes.thischwa.bacoma.rest.model.pojo.site database objects}, which can be ordered, have to implement this
 * interface.
 */
public interface IOrderable<T extends AbstractBacomaObject<?>> {
	public List<T> getFamily();
}
