package codes.thischwa.bacoma.rest.render.context;

import codes.thischwa.bacoma.rest.render.ViewMode;

/**
 * IInteface for velocity context objects, managed by spring.
 * Have to be implemented by all beans needed in every template, which needs the {@link ViewMode} object.
 */
public interface IContextObjectNeedViewMode {
	public void setViewMode(final ViewMode viewMode);
}
