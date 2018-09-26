package de.mw.mwdata.ofdb.query;

import java.util.List;

import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;

/**
 * Interface for building ofdb-compatible HSQL-Queries
 *
 * @author mwilbers
 *
 */
public interface OfdbQueryModel {

	/**
	 * Sets the FROM-Table in the select-statement
	 *
	 * @param tabDef
	 */
	public void setMainTable(final ITabDef tabDef);

	/**
	 * Gets the FROM-Table in the select-statement
	 *
	 * @return
	 */
	public ITabDef getMainTable();

	/**
	 * Adds a table to join in the select-statement
	 *
	 * @param ansichtTab
	 */
	public void addJoinTable(final IAnsichtTab ansichtTab);

	/**
	 * Adds an additional select-column with alias-name
	 *
	 * @param ansichtTab
	 *            the AnsichtTab-Association for the joinTable the alias-column
	 *            belongs to
	 * @param tabSpeig
	 *            the TabSpeig the alias-column is based on
	 */
	public void addAlias(final IAnsichtTab ansichtTab, final ITabSpeig tabSpeig);

	public List<IAnsichtTab> getJoinedTables();

	/**
	 * Gets the list of aliased select-columns in the select-statement
	 *
	 * @param viewName
	 * @return
	 */
	public List<ITabSpeig> getAlias(final IAnsichtTab viewTab);

	public void addWhereRestriction(final ITabDef whereTabDef, final ITabSpeig whereTabSpeig,
			final OperatorEnum whereOperator, final Object whereValue);

	public List<OfdbWhereRestriction> getWhereRestrictions();

	public void resetWhereRestrictions();

	public void addOrderSet(final ITabDef orderTabDef, final ITabSpeig orderTabSpeig, final String orderDirection);

	public List<OfdbOrderSet> getOrderSet();

	public void resetOrderSet();

	public void addMetaData(final List<OfdbField> ofdbFields);

	public List<OfdbField> getMetaData();

}
