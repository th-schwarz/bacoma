package codes.thischwa.bacoma.rest.util;

public class EnumUtil {

	public static <E extends Enum<E>> E valueOfIgnoreCase(Class<E> cls, String value) throws IllegalArgumentException {
		for (E e : cls.getEnumConstants()) {
			if (e.toString().equalsIgnoreCase(value))
				return e;
		}
		throw new IllegalArgumentException(String.format("Unknown enum.value: ", value));
	}
}
