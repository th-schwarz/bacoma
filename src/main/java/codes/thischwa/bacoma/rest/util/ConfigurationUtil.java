package codes.thischwa.bacoma.rest.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Tool for trimming and filtering properties based on a {@link Map}.
 */
public class ConfigurationUtil {

	/**
	 * Construct a new property object, based on the assigned properties. 
	 *
	 * @param properties the base properties to work with.
	 * @param filter 1st part of a property key
	 * @param trim if true, 'filter' will be cut from the keys (Useful e.g. for velocity properties.)
	 * @param exactMatch if true, only one property (the exact one, if found) will be returned
	 * @return
	 */
	public static Map<String, String> getProperties(final Map<String, String> properties, final String filter, boolean trim, boolean exactMatch) {
		Map<String, String> newProps = new HashMap<>();

		for (Object keyObj : properties.keySet()) {
			String key = keyObj.toString();
			if ((!exactMatch && key.startsWith(filter)) || (exactMatch && key.equals(filter))) {
				String newKey = key;
				if (trim)
					newKey = key.substring(filter.length() + 1, key.length());
				newProps.put(newKey, properties.get(key));
				if (exactMatch && key.equals(filter))
					return newProps;
			}
		}
		return newProps;
	}

	/**
	 * Wrapper for {@link #getProperties(String, boolean, boolean)}.
	 */
	public static Map<String, String> getProperties(final Map<String, String> properties, final String filter, boolean trim) {
		return getProperties(properties, filter, trim, false);
	}

	/**
	 * Wrapper for {@link #getProperties(String, boolean, boolean)}.
	 */
	public static Map<String, String> getProperties(final Map<String, String> properties, final String filter) {
		return getProperties(properties, filter, false, false);
	}

}
