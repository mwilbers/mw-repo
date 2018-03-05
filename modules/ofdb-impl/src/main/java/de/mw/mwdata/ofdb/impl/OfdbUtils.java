package de.mw.mwdata.ofdb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.impl.TabSpeig;

public class OfdbUtils {

	public static SortKey findSortKeyByColumnName(final List<SortKey> sortKeys, final String colName) {

		for (SortKey key : sortKeys) {
			if (key.getSortColumn().equals(colName)) {
				return key;
			}
		}

		return null;
	}

	/**
	 * Converts a List with TabSpeig-Elements to a HashMap with key =
	 * tabSpeig.spalte in uppercase.
	 * 
	 * @param items
	 * @return
	 */
	public static Map<String, TabSpeig> tabSpeigListToMap(final List<TabSpeig> items) {
		Map<String, TabSpeig> map = new HashMap<String, TabSpeig>();
		// Map<String,IFxPersistable> map = new
		// HashMap<String,IFxPersistable>();

		for (TabSpeig item : items) {
			// TODO: Test: name has to be unique here ->
			// hibernate-unique-constraint needed
			map.put(item.getSpalte().toUpperCase(), item);
		}

		return map;

	}

	public static IAnsichtTab getMainAnsichtTab(final List<IAnsichtTab> ansichtTabList) {

		for (IAnsichtTab ansichtTab : ansichtTabList) {

			// FIXME: refactor expression x, do in ofdbvalidator
			if (ansichtTab.getJoinTyp().equalsIgnoreCase("x")) {
				return ansichtTab;
			}
		}

		return null;

	}

}
