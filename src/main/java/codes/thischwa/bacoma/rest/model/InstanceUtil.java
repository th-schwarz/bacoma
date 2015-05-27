package codes.thischwa.bacoma.rest.model;

import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractSiteResource;
import codes.thischwa.bacoma.rest.model.pojo.site.Content;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;
import codes.thischwa.bacoma.rest.model.pojo.site.Macro;
import codes.thischwa.bacoma.rest.model.pojo.site.Page;
import codes.thischwa.bacoma.rest.model.pojo.site.Site;
import codes.thischwa.bacoma.rest.model.pojo.site.Template;

/**
 * Static helper class to check the instance of the database pojos.
 */
public class InstanceUtil {

	public static boolean isPoormansObject(Object obj) {
		return (obj instanceof AbstractBacomaObject<?>);
	}

	public static boolean isSiteResource(Object obj) {
		return (obj instanceof AbstractSiteResource);
	}

	public static boolean isMacro(Object obj) {
		return (obj instanceof Macro);
	}

	public static boolean isTemplate(Object obj) {
		return (obj instanceof Template);
	}

	public static boolean isRenderable(Object obj) {
		return (obj instanceof IRenderable);
	}

	public static boolean isContent(Object obj) {
		return (obj instanceof Content);
	}

	public static boolean isPage(Object obj) {
		return (obj instanceof Page);
	}

	public static boolean isJustLevel(Object obj) {
		return (isLevel(obj) && !isSite(obj));
	}

	public static boolean isLevel(Object obj) {
		return (obj instanceof Level);
	}

	public static boolean isSite(Object obj) {
		return (obj instanceof Site);
	}

}
