package codes.thischwa.bacoma.rest.render.context.object;

import codes.thischwa.bacoma.rest.model.BoInfo;
import codes.thischwa.bacoma.rest.model.InstanceUtil;
import codes.thischwa.bacoma.rest.model.pojo.site.AbstractBacomaObject;
import codes.thischwa.bacoma.rest.model.pojo.site.Level;

final class ContextObjectUtilities {

	private ContextObjectUtilities() {
	}
	
    /**
     * Generates a relative path from one {@link Level} to another.
     * 
     * @param startLevel
     * @param destLevel
     * @return Relative path.
     */
     static String getURLRelativePathToLevel(final Level startLevel, final Level destLevel) {
		if ((startLevel == null && destLevel == null))
			return "";
		if (destLevel == null)
			return getURLRelativePathToRoot(startLevel);
		if (destLevel.equals(startLevel))
			return "";
		StringBuilder link = new StringBuilder();
		link.append(getURLRelativePathToRoot(startLevel));
		for (AbstractBacomaObject<?> po : BoInfo.getBreadcrumbs(destLevel)) {
			if (InstanceUtil.isJustLevel(po)) {
				Level level = (Level) po;
				link.append(level.getName());
				link.append("/");
			}
		}
		return link.toString();
	}

     /**
      * Generates the relative path to the root of the site.
      * 
      * @param level
      * @return Relative path to the root, starting point is container.
      */
     static String getURLRelativePathToRoot(final Level level) {
     	if (level == null || InstanceUtil.isSite(level))
 			return "";
 		StringBuilder link = new StringBuilder();
 		for (int i = 0; i < level.getHierarchy(); i++) {
 			link.append("../");
 		}
 		return link.toString();
     }
     
}
