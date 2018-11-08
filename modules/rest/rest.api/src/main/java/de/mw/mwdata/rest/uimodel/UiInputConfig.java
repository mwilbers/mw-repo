package de.mw.mwdata.rest.uimodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mw.mwdata.core.domain.DBTYPE;
import de.mw.mwdata.core.domain.JoinedPropertyTO;
import de.mw.mwdata.core.to.OfdbField;

/**
 * Ui specific object for describing configured behaviour of an output item in
 * UI. This output item can be a table column item, a simple textbox, an option
 * button or any other control.
 * 
 * @author WilbersM
 *
 */
public class UiInputConfig {

	public final int DEFAULT_CURRENCY = 2;

	private StringBuffer diagnoseBuf;
	private Map<String, String> diagnoseMap = new HashMap<String, String>();

	private boolean tabSpeigSytemWert;

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
	private int resultIndex;

	/* common properties for view control */
	private boolean nullable;
	private boolean visible;
	private boolean filterable;

	private JoinedPropertyTO joinedProperty;

	public UiInputConfig(final OfdbField ofdbField) {
		this.tabSpeigSytemWert = ofdbField.getTabSpeigSystemWert();
		this.tabSpeigBearbErlaubt = ofdbField.getTabSpeigBearbErlaubt();
		this.ansichtSpalteBearbZugelassen = ofdbField.getAnsichtSpalteBearbZugelassen();
		this.ansichtSpalteBearbHinzufuegenZugelassen = ofdbField.getAnsichtSpalteBearbHinzufuegenZugelassen();
		this.columnTitle = ofdbField.getColumnTitle();
		this.maxlength = ofdbField.getMaxlength();
		this.minlength = ofdbField.getMinlength();
		this.currency = ofdbField.getCurrency();
		this.propName = ofdbField.getPropName();
		this.propOfdbName = ofdbField.getPropOfdbName();
		this.dbtype = ofdbField.getDbtype();
		this.reihenfolge = ofdbField.getReihenfolge();
		this.listOfValues = ofdbField.getListOfValues();
		this.resultIndex = ofdbField.getResultIndex();

		this.nullable = ofdbField.isNullable();
		this.visible = ofdbField.isVisible();
		this.filterable = ofdbField.isFilterable();

		this.joinedProperty = ofdbField.getJoinedProperty();
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

	public boolean isEditable() {
		if (this.tabSpeigSytemWert || !this.tabSpeigBearbErlaubt) {
			return false;
		} else {
			if (this.ansichtSpalteBearbZugelassen) {
				return true;
			} else {
				return false;
			}
		}

	}

	public boolean isInsertable() {
		if (this.tabSpeigSytemWert || !this.tabSpeigBearbErlaubt) {
			return false;
		} else {
			if (this.ansichtSpalteBearbHinzufuegenZugelassen) {
				return true;
			} else {
				return false;
			}
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

	public DBTYPE getDbtype() {
		return this.dbtype;
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

	public void setTabSpeigBearbErlaubt(boolean tabSpeigBearbErlaubt) {
		this.tabSpeigBearbErlaubt = tabSpeigBearbErlaubt;
	}

	public void setTabSpeigSytemWert(boolean tabSpeigSytemWert) {
		this.tabSpeigSytemWert = tabSpeigSytemWert;
	}

	public void setAnsichtSpalteBearbZugelassen(boolean ansichtSpalteBearbZugelassen) {
		this.ansichtSpalteBearbZugelassen = ansichtSpalteBearbZugelassen;
	}

	public void setAnsichtSpalteBearbHinzufuegenZugelassen(boolean ansichtSpalteBearbHinzufuegenZugelassen) {
		this.ansichtSpalteBearbHinzufuegenZugelassen = ansichtSpalteBearbHinzufuegenZugelassen;
	}

	public boolean isSystem() {
		return this.tabSpeigSytemWert;
	}

	public JoinedPropertyTO getJoinedProperty() {
		return joinedProperty;
	}

}
