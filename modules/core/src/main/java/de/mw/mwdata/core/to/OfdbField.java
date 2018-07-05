/**
 *
 */
package de.mw.mwdata.core.to;

import java.util.List;

import de.mw.mwdata.core.domain.DBTYPE;

/**
 * TO-class used in presentation-layer for all ofdb-relevant informations<br>
 * FIXME: ist OfdbField still necessary or can UiItemConfig primarily be built
 * up in UI from TabSpeig / AnsichtSpalte ?
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 *
 */
public class OfdbField {

	public final int DEFAULT_CURRENCY = 2;

	private boolean tabSpeigSystemWert;

	private boolean tabSpeigBearbErlaubt;
	private boolean ansichtSpalteBearbZugelassen;
	private boolean ansichtSpalteBearbHinzufuegenZugelassen;

	private String columnTitle;

	/**
	 * the maxlength of the field-content
	 */
	private String maxlength;

	/**
	 * the minlength of the field-content
	 */
	private String minlength;

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

	/* data needed for list of values */
	private List<Object> listOfValues;
	private String itemValue;
	private String itemLabel;
	private int resultIndex;

	/* common properties for view control */
	private boolean nullable;
	private boolean visible;
	private boolean filterable;

	/**
	 * itemKey is needed for unique Key-Identifier for storing in MapValue if e.g.
	 * there are two referenced tables in view with the same columnname
	 */
	private String itemKey;

	public OfdbField() {
	}

	public void setMaxlength(final String maxlength) {
		this.maxlength = maxlength;
	}

	public String getMaxlength() {
		return this.maxlength;
	}

	public void setMinlength(final String minlength) {
		this.minlength = minlength;
	}

	public String getMinlength() {
		return this.minlength;
	}

	public void setPropName(final String propName) {
		this.propName = propName;
	}

	public String getPropName() {
		return this.propName;
	}

	public void setPropOfdbName(final String propOfdbName) {
		this.propOfdbName = propOfdbName;
	}

	public String getPropOfdbName() {
		return this.propOfdbName;
	}

	public void setReihenfolge(final long reihenfolge) {
		this.reihenfolge = reihenfolge;
	}

	public long getReihenfolge() {
		return this.reihenfolge;
	}

	public boolean isMapped() {
		return (null != this.propName);
	}

	public void setListOfValues(final List<Object> listOfValues) {
		this.listOfValues = listOfValues;
	}

	public List<Object> getListOfValues() {
		return this.listOfValues;
	}

	public void setNullable(final boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isNullable() {
		return this.nullable;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("OfdbField [ propName = '");
		b.append(this.propName);
		// b.append("', propValue = '");
		// b.append(this.propValue);
		b.append("', propOfdbName = '");
		b.append(this.propOfdbName);
		b.append("' ]");
		return b.toString();
	}

	public boolean isEnum() {
		return this.dbtype.equals(DBTYPE.ENUM);
	}

	// public boolean isEditable() {
	// if (this.tabSpeigSystemWert || !this.tabSpeigBearbErlaubt) {
	// return false;
	// } else {
	// if (this.ansichtSpalteBearbZugelassen) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	//
	// }
	//
	// public boolean isInsertable() {
	// if (this.tabSpeigSystemWert || !this.tabSpeigBearbErlaubt) {
	// return false;
	// } else {
	// if (this.ansichtSpalteBearbZugelassen) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	// }

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

	public DBTYPE getDbtype() {
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

	public boolean isVerdeckenDurchSpalte() {
		return (this.getResultIndex() > 0);
	}

	public void setItemKey(final String itemKey) {
		this.itemKey = itemKey;
	}

	public String getItemKey() {
		return this.itemKey;
	}

	// following unused getters, setters, just needed for json conversion

	public int getDEFAULT_CURRENCY() {
		return DEFAULT_CURRENCY;
	}

	public void setTabSpeigBearbErlaubt(boolean tabSpeigBearbErlaubt) {
		this.tabSpeigBearbErlaubt = tabSpeigBearbErlaubt;
	}

	public boolean getTabSpeigBearbErlaubt() {
		return this.tabSpeigBearbErlaubt;
	}

	public void setTabSpeigSystemWert(boolean tabSpeigSytemWert) {
		this.tabSpeigSystemWert = tabSpeigSytemWert;
	}

	public boolean getTabSpeigSystemWert() {
		return this.tabSpeigSystemWert;
	}

	public void setAnsichtSpalteBearbZugelassen(boolean ansichtSpalteBearbZugelassen) {
		this.ansichtSpalteBearbZugelassen = ansichtSpalteBearbZugelassen;
	}

	public boolean getAnsichtSpalteBearbZugelassen() {
		return this.ansichtSpalteBearbZugelassen;
	}

	public void setAnsichtSpalteBearbHinzufuegenZugelassen(boolean ansichtSpalteBearbHinzufuegenZugelassen) {
		this.ansichtSpalteBearbHinzufuegenZugelassen = ansichtSpalteBearbHinzufuegenZugelassen;
	}

	public boolean getAnsichtSpalteBearbHinzufuegenZugelassen() {
		return this.ansichtSpalteBearbHinzufuegenZugelassen;
	}

}
