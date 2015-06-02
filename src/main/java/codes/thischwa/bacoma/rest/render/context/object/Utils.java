package codes.thischwa.bacoma.rest.render.context.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import codes.thischwa.bacoma.rest.render.context.IContextObjectCommon;

/**
 * Context object to provide tool methods. A good place for various static helper methods.
 */
@Component("utils")
public class Utils implements IContextObjectCommon {

	/**
	 * Reverse the order of a {@link Collection}. It's null-save. If the desired {@link Collection} is null or empty, 
	 * an empty one will returned.
	 * @param <T>
	 * @param col The collection to reverse. Could be null or empty.
	 * 
	 * @return An empty collection if the desired one was empty or null, or the desired collection with the reversed order.
	 */
	public static <T> Collection<T> reverseCollection(Collection<T> col) {
		if (CollectionUtils.isEmpty(col))
			return new ArrayList<>();
		ArrayList<T> arrayList = new ArrayList<>(col);
		Collections.reverse(arrayList);
		return arrayList;
	}

	/**
	 * @return True, is string is empty, otherwise false.
	 */
	public static boolean isEmpty(final String string) {
		return StringUtils.isBlank(string);
	}

	/**
	 * @return Html escaped 'string', or '', if string is empty.
	 */
	public static String escape(final String string) {
		return StringUtils.defaultString(StringEscapeUtils.escapeHtml(string));
	}

	public static String skipBeginningString(final String string, final String stringToSkip) {
		return StringUtils.substring(string, stringToSkip.length());
	}
}
