package de.mw.mwdata.ofdb.query.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mw.mwdata.core.query.InvalidQueryConfigurationException;
import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.ofdb.query.OfdbOrderSet;
import de.mw.mwdata.ofdb.query.OfdbQueryModel;
import de.mw.mwdata.ofdb.query.OfdbWhereRestriction;

public class DefaultOfdbQueryModel implements OfdbQueryModel {

	private ITabDef tabDef;
	private List<IAnsichtTab> joinAnsichtTabs = new ArrayList<IAnsichtTab>();

	/**
	 * Contains a key-value-map with key = AnsichtTab.name and values = list of
	 * TabSpeigs<br>
	 * FIXME: error : key not unique. key should be tableName !
	 */
	private Map<String, List<ITabSpeig>> aliasTabSpeigs = new HashMap<String, List<ITabSpeig>>();

	private List<OfdbWhereRestriction> whereRestrictions = new ArrayList<OfdbWhereRestriction>();

	private List<OfdbOrderSet> orderList = new ArrayList<OfdbOrderSet>();

	private List<OfdbField> ofdbFields = new ArrayList<OfdbField>();

	public void setMainTable(final ITabDef tabDef) {
		this.tabDef = tabDef;
	}

	public void addJoinTable(final IAnsichtTab ansichtTab) {
		this.joinAnsichtTabs.add(ansichtTab);
	}

	public void addAlias(final IAnsichtTab ansichtTab, final ITabSpeig tabSpeig) {

		List<ITabSpeig> list = this.aliasTabSpeigs.get(ansichtTab.getTabAKey());

		if (null == list) {
			list = new ArrayList<ITabSpeig>();
			this.aliasTabSpeigs.put(ansichtTab.getTabAKey(), list);
		} else {
			// FIXME: should be fixed !
			throw new OfdbInvalidConfigurationException(
					"Invalid Query Model configuration. Duplicate key for ansichtTab.name " + ansichtTab.getName());
		}

		list.add(tabSpeig);

	}

	public List<ITabSpeig> getAlias(final IAnsichtTab viewTab) {

		List<ITabSpeig> list = this.aliasTabSpeigs.get(viewTab.getTabAKey());

		if (null == list) {
			return new ArrayList<ITabSpeig>();

		}
		return list;
	}

	// @Override
	public ITabDef getMainTable() {
		return this.tabDef;
	}

	public List<IAnsichtTab> getJoinedTables() {
		return this.joinAnsichtTabs;
	}

	/**
	 * Checks if the tabDef is already registered as maintable or as jointable
	 *
	 * @param tabDef
	 * @return true if registered, else false
	 */
	private boolean isTabDefRegistered(final ITabDef tabDef) {

		if (this.tabDef.equals(tabDef)) {
			return true;
		}

		for (IAnsichtTab ansichtTab : this.joinAnsichtTabs) {
			if (ansichtTab.getTabDef().equals(tabDef)) {
				return true;
			}
		}

		return false;

	}

	public void addWhereRestriction(final ITabDef whereTabDef, final ITabSpeig whereTabSpeig,
			final OperatorEnum whereOperator, final Object whereValue) {

		if (!isTabDefRegistered(whereTabDef)) {
			throw new InvalidQueryConfigurationException(
					"TabDef " + whereTabDef.getName() + " not registered in QueryBuilder.");
		}

		OfdbWhereRestriction whereRes = new OfdbWhereRestriction(whereTabDef, whereTabSpeig, whereOperator, whereValue);

		this.whereRestrictions.add(whereRes);

	}

	// @Override
	public List<OfdbWhereRestriction> getWhereRestrictions() {
		return this.whereRestrictions;
	}

	// @Override
	public void resetWhereRestrictions() {
		this.whereRestrictions.clear();

	}

	public void addOrderSet(final ITabDef orderTabDef, final ITabSpeig orderTabSpeig, final String orderDirection) {
		this.orderList.add(new OfdbOrderSet(orderTabDef, orderTabSpeig, orderDirection));
	}

	public List<OfdbOrderSet> getOrderSet() {
		return this.orderList;
	}

	// @Override
	public void resetOrderSet() {
		this.orderList.clear();
	}

	@Override
	public void addMetaData(List<OfdbField> ofdbFields) {
		this.ofdbFields.addAll(ofdbFields);
	}

	@Override
	public List<OfdbField> getMetaData() {
		return this.ofdbFields;
	}

}
