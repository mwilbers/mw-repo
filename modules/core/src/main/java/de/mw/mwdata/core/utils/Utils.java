/**
 * 
 */
package de.mw.mwdata.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import de.mw.mwdata.core.domain.IEntity;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Jun, 2010
 * 
 */
public class Utils {

	public static Map<String, IEntity> listToMap(final List<IEntity> items) {
		Map<String, IEntity> map = new HashMap<String, IEntity>();

		for (IEntity item : items) {
			// TODO: Test: name has to be unique here ->
			// hibernate-unique-constraint needed
			map.put(item.getName(), item);
		}

		return map;

	}

	public static List<IEntity[]> toObjectArray(final List<IEntity[]> items) {

		if (CollectionUtils.isEmpty(items)) {
			return new ArrayList<IEntity[]>();
		}

		// if already array
		if (items.get(0) instanceof Object[]) {
			return items;
		}

		List<IEntity[]> result = new ArrayList<IEntity[]>(items.size());

		for (int i = 0; i < items.size(); i++) {
			Object item = items.get(i);

			IEntity[] token = new IEntity[1];
			token[0] = (IEntity) item;
			result.add(token);
		}

		return result;
	}

	public static List<IEntity> mapToList(final Map<String, IEntity> map) {

		List<IEntity> results = new ArrayList<IEntity>();
		for (Map.Entry<String, IEntity> ansichtEntry : map.entrySet()) {
			results.add(ansichtEntry.getValue());
		}

		return results;
	}

}
