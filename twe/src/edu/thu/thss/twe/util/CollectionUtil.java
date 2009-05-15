package edu.thu.thss.twe.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {

	/** Indicates whether collection elements should be actually checked. */
	private static final boolean DEBUG = true;

	private CollectionUtil() {
		// hide default constructor to prevent instantiation
	}

	/**
	 * Ensures that all elements of the given collection can be cast to a
	 * desired type.
	 * 
	 * @param collection
	 *            the collection to check
	 * @param type
	 *            the desired type
	 * @return a collection of the desired type
	 * @throws ClassCastException
	 *             if an element cannot be cast to the desired type
	 */
	@SuppressWarnings("unchecked")
	public static <E> Collection<E> checkCollection(Collection<?> collection,
			Class<E> type) {
		if (DEBUG) {
			for (Object element : collection) {
				type.cast(element);
			}
		}
		return (Collection<E>) collection;
	}

	/**
	 * Ensures that all elements of the given list can be cast to a desired
	 * type.
	 * 
	 * @param list
	 *            the list to check
	 * @param type
	 *            the desired type
	 * @return a list of the desired type
	 * @throws ClassCastException
	 *             if an element cannot be cast to the desired type
	 */
	@SuppressWarnings("unchecked")
	public static <E> List<E> checkList(List<?> list, Class<E> type) {
		if (DEBUG) {
			for (Object element : list) {
				type.cast(element);
			}
		}
		return (List<E>) list;
	}

	/**
	 * Ensures that all elements of the given set can be cast to a desired type.
	 * 
	 * @param list
	 *            the set to check
	 * @param type
	 *            the desired type
	 * @return a set of the desired type
	 * @throws ClassCastException
	 *             if an element cannot be cast to the desired type
	 */
	@SuppressWarnings("unchecked")
	public static <E> Set<E> checkSet(Set<?> set, Class<E> type) {
		if (DEBUG) {
			for (Object element : set) {
				type.cast(element);
			}
		}
		return (Set<E>) set;
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> checkMap(Map<?, ?> map, Class<K> keyType,
			Class<V> valueType) {
		if (DEBUG) {
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				keyType.cast(entry.getKey());
				valueType.cast(entry.getValue());
			}
		}
		return (Map<K, V>) map;
	}
}
