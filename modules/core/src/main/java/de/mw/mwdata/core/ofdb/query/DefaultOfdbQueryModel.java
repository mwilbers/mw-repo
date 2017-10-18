package de.mw.mwdata.core.ofdb.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.mw.mwdata.core.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ordb.query.OfdbOrderSet;
import de.mw.mwdata.ordb.query.OfdbQueryModel;
import de.mw.mwdata.ordb.query.OfdbWhereRestriction;
import de.mw.mwdata.ordb.query.OperatorEnum;

public class DefaultOfdbQueryModel implements OfdbQueryModel {

	private ITabDef							tabDef;
	private List<IAnsichtTab>				joinAnsichtTabs		= new ArrayList<IAnsichtTab>();

	/**
	 * Contains a key-value-map with key = AnsichtTab.name and values = list of TabSpeigs
	 */
	private Map<String, List<ITabSpeig>>	aliasTabSpeigs		= new HashMap<String, List<ITabSpeig>>();

	private List<OfdbWhereRestriction>		whereRestrictions	= new ArrayList<OfdbWhereRestriction>();

	private List<OfdbOrderSet>				orderList			= new ArrayList<OfdbOrderSet>();

	public void setMainTable( final ITabDef tabDef ) {
		this.tabDef = tabDef;
	}

	public void addJoinTable( final IAnsichtTab ansichtTab ) {
		this.joinAnsichtTabs.add( ansichtTab );
	}

	public void addAlias( final IAnsichtTab ansichtTab, final ITabSpeig tabSpeig ) {

		List<ITabSpeig> list = this.aliasTabSpeigs.get( ansichtTab.getName() );

		if ( null == list ) {
			list = new ArrayList<ITabSpeig>();
			this.aliasTabSpeigs.put( ansichtTab.getName(), list );
		}

		list.add( tabSpeig );

	}

	public List<ITabSpeig> getAlias( final String viewName ) {

		List<ITabSpeig> list = this.aliasTabSpeigs.get( viewName );

		if ( null == list ) {
			return new ArrayList<ITabSpeig>();

		}
		return list;
	}

	// @Override
	public ITabDef getMainTable() {
		return this.tabDef;
	}

	public List<IAnsichtTab> getJoinedViews() {
		return this.joinAnsichtTabs;
	}

	/**
	 * Checks if the tabDef is already registered as maintable or as jointable
	 *
	 * @param tabDef
	 * @return true if registered, else false
	 */
	private boolean isTabDefRegistered( final ITabDef tabDef ) {

		if ( this.tabDef.equals( tabDef ) ) {
			return true;
		}

		for ( IAnsichtTab ansichtTab : this.joinAnsichtTabs ) {
			if ( ansichtTab.getTabDef().equals( tabDef ) ) {
				return true;
			}
		}

		return false;

	}

	public void addWhereRestriction( final ITabDef whereTabDef, final ITabSpeig whereTabSpeig,
			final OperatorEnum whereOperator, final Object whereValue ) {

		if ( !isTabDefRegistered( whereTabDef ) ) {
			throw new OfdbQueryBuilderException( "TabDef " + whereTabDef.getName()
					+ " not registered in OfdbQueryBuilder." );
		}

		OfdbWhereRestriction whereRes = new OfdbWhereRestriction( whereTabDef, whereTabSpeig, whereOperator, whereValue );

		this.whereRestrictions.add( whereRes );

	}

	// @Override
	public List<OfdbWhereRestriction> getWhereRestrictions() {
		return this.whereRestrictions;
	}

	// @Override
	public void resetWhereRestrictions() {
		this.whereRestrictions.clear();

	}

	public void addOrderSet( final ITabDef orderTabDef, final ITabSpeig orderTabSpeig, final String orderDirection ) {
		this.orderList.add( new OfdbOrderSet( orderTabDef, orderTabSpeig, orderDirection ) );
	}

	public List<OfdbOrderSet> getOrderSet() {
		return this.orderList;
	}

	// @Override
	public void resetOrderSet() {
		this.orderList.clear();
	}

}
