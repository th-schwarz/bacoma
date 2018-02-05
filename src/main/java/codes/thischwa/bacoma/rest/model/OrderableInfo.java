package codes.thischwa.bacoma.rest.model;

import java.util.List;

import codes.thischwa.bacoma.rest.model.pojo.site.Page;


/**
 * Helper to provide some static informations of an {@link IOrderable}.
 */
public class OrderableInfo {

	public static boolean isFirst(final IOrderable<?> orderable) {
		return (orderable != null && orderable.getFamily().indexOf(orderable) == 0) ? true : false;
	}

	public static boolean hasNext(final IOrderable<?> orderable) {
		if (orderable == null)
			return false;
		if (InstanceUtil.isPage(orderable) && BoInfo.isWelcomePage((Page)orderable))
			return false;
		
		int pos = orderable.getFamily().indexOf(orderable);
		return (pos < orderable.getFamily().size()-1);
	}

	public static <T extends IOrderable<?>> T getNext(final T orderable) {
		if (!hasNext(orderable))
			return null;

		@SuppressWarnings("unchecked")
		List<T> family = (List<T>) orderable.getFamily();
		int wantedPos = family.indexOf(orderable) + 1;
		return family.get(wantedPos);
	}

	public static boolean hasPrevious(final IOrderable<?> orderable) {
		if (orderable == null)
			return false;
		return (!isFirst(orderable));
	}

	public static  <T extends IOrderable<?>> T getPrevious(final T orderable) {
		if (!hasPrevious(orderable))
			return null;
		
		@SuppressWarnings("unchecked")
		List<T> family = (List<T>) orderable.getFamily();
		int wantedPos = family.indexOf(orderable) - 1;
		return family.get(wantedPos);
	}

}
