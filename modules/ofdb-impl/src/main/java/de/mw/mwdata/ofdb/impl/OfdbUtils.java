package de.mw.mwdata.ofdb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
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

	public static String generateItemKey(final ITabDef tabDef, final String propName) {
		return OfdbUtils.getSimpleName(tabDef) + "." + propName;
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

	/**
	 *
	 * @param tabDef
	 * @return the simple name of the entity given by the fullclassname of the
	 *         TabDef-column
	 *
	 */
	public static String getSimpleName(final ITabDef tabDef) {

		if (null == tabDef) {
			return StringUtils.EMPTY;
		}

		return ClassNameUtils.getSimpleClassName(tabDef.getFullClassName());
	}

	public static OfdbField createOfdbField(final ITabSpeig tabSpeig, final IAnsichtSpalte ansichtSpalte) {

		OfdbField ofdbField = new OfdbField();

		ofdbField.setTabSpeigBearbErlaubt(tabSpeig.getBearbErlaubt());
		ofdbField.setTabSpeigSytemWert(tabSpeig.getSystemWert());
		ofdbField.setAnsichtSpalteBearbZugelassen(ansichtSpalte.getBearbZugelassen());

		Integer nachkommastellen = ansichtSpalte.getAnzahlNachkommastellen();
		if (nachkommastellen == null) {
			ofdbField.setCurrency(ofdbField.DEFAULT_CURRENCY);
		} else {
			ofdbField.setCurrency(ansichtSpalte.getAnzahlNachkommastellen());
		}

		ofdbField.setFilterable(ansichtSpalte.getFilter());
		ofdbField.setVisible(ansichtSpalte.getInGridAnzeigen());
		ofdbField.setReihenfolge(tabSpeig.getReihenfolge());
		ofdbField.setPropOfdbName(tabSpeig.getSpalte());
		ofdbField.setNullable(!tabSpeig.getEingabeNotwendig() && !ansichtSpalte.getEingabeNotwendig());
		ofdbField.setDbtype(tabSpeig.getDbDatentyp());
		ofdbField.setColumnTitle(tabSpeig.getSpaltenkopf());

		// this.refreshEditMode(crud);
		return ofdbField;

	}

}
