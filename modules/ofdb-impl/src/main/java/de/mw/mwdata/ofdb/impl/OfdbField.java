/**
 *
 */
package de.mw.mwdata.ofdb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.ITabSpeig.DBTYPE;

/**
 * TO-class used in presentation-layer for all ofdb-relevant informations
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 *
 */
public class OfdbField {

	public final int DEFAULT_CURRENCY = 2;

	private StringBuffer diagnoseBuf;
	private Map<String, String> diagnoseMap = new HashMap<String, String>();

	private ITabSpeig tabSpeig;
	private IAnsichtSpalte ansichtSpalte;
	private String columnTitle;

	/**
	 * the maxlength of the field-content
	 */
	private int maxlength;

	/**
	 * the currency
	 */
	private int currency;

	/**
	 * the name of the domain-property
	 */
	private String propName;

	/**
	 * the ofdb-column-name of the domain-property
	 */
	private String propOfdbName = null;

	private DBTYPE dbtype;

	/**
	 * the reihenfolge-value of the ofdb-TabSpeig-item
	 */
	private long reihenfolge;

	/**
	 * the value of the ofdb-Property
	 */
	private Object propValue;

	/* data needed for list of values */
	private List<Object> listOfValues;
	private String itemValue;
	private String itemLabel;
	private int resultIndex;

	/* common properties for view control */

	private boolean nullable;

	// private boolean editable;

	private boolean visible;

	private boolean filterable;

	/**
	 * itemKey is needed for unique Key-Identifier for storing in MapValue if e.g.
	 * there are two referenced tables in view with the same columnname
	 */
	private String itemKey;

	public OfdbField(final ITabSpeig tabSpeig, final IAnsichtSpalte ansichtSpalte) {

		// this.diagnoseBuf = new StringBuffer();
		this.tabSpeig = tabSpeig;
		this.ansichtSpalte = ansichtSpalte;

		Integer nachkommastellen = ansichtSpalte.getAnzahlNachkommastellen();
		if (nachkommastellen == null) {
			this.setCurrency(this.DEFAULT_CURRENCY);
		} else {
			this.setCurrency(ansichtSpalte.getAnzahlNachkommastellen());
		}

		this.setFilterable(ansichtSpalte.getFilter());
		this.setVisible(ansichtSpalte.getInGridAnzeigen());
		this.setReihenfolge(tabSpeig.getReihenfolge());
		this.setPropOfdbName(tabSpeig.getSpalte());
		this.setNullable(!tabSpeig.getEingabeNotwendig() && !ansichtSpalte.getEingabeNotwendig());
		this.setDbtype(tabSpeig.getDbDatentyp());
		this.columnTitle = tabSpeig.getSpaltenkopf();

		// this.refreshEditMode(crud);

	}

	private void addDiagnose(final String name, final String value) {
		this.diagnoseMap.put(name, value);
	}

	public String getDiagnose() {
		// // FIXME: in tooltip prop setEditable ca 20 mal
		// return this.diagnoseBuf.toString();

		this.diagnoseBuf = new StringBuffer();
		for (Map.Entry<String, String> entry : this.diagnoseMap.entrySet()) {
			this.diagnoseBuf.append(entry.getKey() + ": " + entry.getValue() + "\n");
		}
		return this.diagnoseBuf.toString();
	}

	public void setMaxlength(final int maxlength) {
		this.maxlength = maxlength;
	}

	public int getMaxlength() {
		return this.maxlength;
	}

	public void setPropName(final String propName) {
		this.propName = propName;
		this.addDiagnose("propName", propName);
	}

	public String getPropName() {
		return this.propName;
	}

	public void setPropOfdbName(final String propOfdbName) {
		this.propOfdbName = propOfdbName;
		this.addDiagnose("propOfdbName", propOfdbName);
	}

	public String getPropOfdbName() {
		return this.propOfdbName;
	}

	public void setReihenfolge(final long reihenfolge) {
		this.reihenfolge = reihenfolge;
		this.addDiagnose("reihenfolge", Long.valueOf(reihenfolge).toString());
	}

	public long getReihenfolge() {
		return this.reihenfolge;
	}

	public boolean isMapped() {
		return (null != this.propName);
	}

	public void setPropValue(final Object propValue) {
		this.propValue = propValue;
	}

	public Object getPropValue() {
		return this.propValue;
	}

	public void setListOfValues(final List<Object> listOfValues) {
		this.listOfValues = listOfValues;
	}

	public List<Object> getListOfValues() {
		return this.listOfValues;
	}

	public void setNullable(final boolean nullable) {
		this.nullable = nullable;
		this.addDiagnose("nullable", Boolean.valueOf(nullable).toString());
	}

	public boolean isNullable() {
		return this.nullable;
	}

	// @JsonIgnore
	// public long getPropId() {
	//
	// // FIXME: can propValue be of type IFxPersistable ?
	// return ((IEntity) this.propValue).getId();
	// }

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("OfdbField [ propName = '");
		b.append(this.propName);
		b.append("', propValue = '");
		b.append(this.propValue);
		b.append("', propOfdbName = '");
		b.append(this.propOfdbName);
		b.append("' ]");
		return b.toString();
	}

	public boolean isEnum() {
		return this.dbtype.equals(DBTYPE.ENUM);
		// return (this.propValue instanceof IFxEnum);
	}

	// FIXME: if crud = insert in ui there should be
	// this.ansichtSpalte.getBearbHinzufZugelassen() evaluated
	public boolean isEditable() {

		if (this.tabSpeig.getSystemWert() || !this.tabSpeig.getBearbErlaubt()) {
			return false;
		} else {

			// switch (crud) {
			// case INSERT: {
			// if (this.ansichtSpalte.getBearbHinzufZugelassen()) {
			// this.setEditable(true);
			// } else {
			// this.setEditable(false);
			// }
			// break;
			// }
			// case UPDATE: {
			if (this.ansichtSpalte.getBearbZugelassen()) {
				return true;
			} else {
				return false;
			}
			// break;
			// }
			// default: {
			// this.setEditable(true);
			// }
			// }

		}

	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setFilterable(final boolean filterable) {
		this.filterable = filterable;
	}

	public boolean isFilterable() {
		return this.filterable;
	}

	public void setCurrency(final int currency) {
		this.currency = currency;
	}

	public int getCurrency() {
		return this.currency;
	}

	public void setDbtype(final DBTYPE dbtype) {
		this.dbtype = dbtype;

	}

	public ITabSpeig.DBTYPE getDbtype() {
		return this.dbtype;
	}

	public void setItemValue(final String itemValue) {
		this.itemValue = itemValue;
	}

	public String getItemValue() {
		return this.itemValue;
	}

	public void setItemLabel(final String itemLabel) {
		this.itemLabel = itemLabel;

	}

	public String getItemLabel() {
		return this.itemLabel;
	}

	public void setResultIndex(final int resultIndex) {
		this.resultIndex = resultIndex;
	}

	/**
	 *
	 * @return 0 if field belongs to the maintable of the view, otherwise > 0, if
	 *         field is an additional column joined as alias on sql
	 */
	public int getResultIndex() {
		return this.resultIndex;
	}

	/**
	 *
	 * @return Spaltenkopf of FX_TabSpEig_K
	 */
	public String getColumnTitle() {
		return this.columnTitle;
	}

	public void setColumnTitle(final String columnTitle) {
		this.columnTitle = columnTitle;
	}

	// public void refreshEditMode(final CRUD crud) {
	//
	// if (this.tabSpeig.getSystemWert() || !this.tabSpeig.getBearbErlaubt()) {
	// this.setEditable(false);
	// } else {
	//
	// switch (crud) {
	// case INSERT: {
	// if (this.ansichtSpalte.getBearbHinzufZugelassen()) {
	// this.setEditable(true);
	// } else {
	// this.setEditable(false);
	// }
	// break;
	// }
	// case UPDATE: {
	// if (this.ansichtSpalte.getBearbZugelassen()) {
	// this.setEditable(true);
	// } else {
	// this.setEditable(false);
	// }
	// break;
	// }
	// default: {
	// this.setEditable(true);
	// }
	// }
	//
	// }
	//
	// }

	public boolean isVerdeckenDurchSpalte() {
		return (this.getResultIndex() > 0);
	}

	public ITabSpeig getTabSpeig() {
		return this.tabSpeig;
	}

	public IAnsichtSpalte getAnsichtSpalte() {
		return this.ansichtSpalte;
	}

	public void setItemKey(final String itemKey) {
		this.itemKey = itemKey;
	}

	public String getItemKey() {
		return this.itemKey;
	}

	// following unused getters, setters, just needed for json conversion

	public StringBuffer getDiagnoseBuf() {
		return diagnoseBuf;
	}

	public void setDiagnoseBuf(StringBuffer diagnoseBuf) {
		this.diagnoseBuf = diagnoseBuf;
	}

	public Map<String, String> getDiagnoseMap() {
		return diagnoseMap;
	}

	public void setDiagnoseMap(Map<String, String> diagnoseMap) {
		this.diagnoseMap = diagnoseMap;
	}

	public int getDEFAULT_CURRENCY() {
		return DEFAULT_CURRENCY;
	}

	public void setTabSpeig(ITabSpeig tabSpeig) {
		this.tabSpeig = tabSpeig;
	}

	public void setAnsichtSpalte(IAnsichtSpalte ansichtSpalte) {
		this.ansichtSpalte = ansichtSpalte;
	}

}
