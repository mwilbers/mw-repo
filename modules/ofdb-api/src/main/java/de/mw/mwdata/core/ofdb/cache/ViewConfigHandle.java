package de.mw.mwdata.core.ofdb.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.core.ofdb.def.OfdbPropMapper;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.core.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ordb.query.OfdbQueryModel;

/**
 * Handler-class for managing logic of whole ViewConfiguration-object. Has
 * package-access to ViewConfigration-object.
 *
 * @author mwilbers
 *
 */
public class ViewConfigHandle {

	private ViewConfiguration viewConfig;

	ViewConfigHandle(final ViewConfiguration viewConf) {
		this.viewConfig = viewConf;
	}

	// package-visibility
	ViewConfiguration getViewConfiguration() {
		return this.viewConfig;
	}

	public List<ITabSpeig> getTableProps(final ITabDef tableDef) {
		return this.viewConfig.getTableProps(tableDef);
	}

	public Map<String, OfdbPropMapper> getPropertyMap() {
		return this.viewConfig.getPropertyMap();
	}

	private OfdbPropMapper findOfdbPropMapperByProperty(final String propertyName) {

		Map<String, OfdbPropMapper> propertyMap = getPropertyMap();
		for (Map.Entry<String, OfdbPropMapper> entry : propertyMap.entrySet()) {
			if (entry.getValue().getPropertyName().equals(propertyName)) {
				return entry.getValue();
			}
		}

		return null;
	}

	/**
	 * 
	 * @param tableDef
	 * @param propertyName
	 * @return table property found by searching property mapping
	 */
	public ITabSpeig findTablePropByProperty(final ITabDef tableDef, final String propertyName) {

		OfdbPropMapper propMapper = findOfdbPropMapperByProperty(propertyName);
		List<ITabSpeig> tableProps = getTableProps(tableDef);

		for (ITabSpeig tabProp : tableProps) {
			if (tabProp.getSpalte().toUpperCase().equals(propMapper.getColumnName().toUpperCase())) {
				return tabProp;
			}
		}

		return null;
	}

	/**
	 * Finds the {@link OfdbPropMapper } given by {@link ITabSpeig}}
	 * 
	 * @param tableProp
	 * @return can be null if ITabSpeig is not mapped
	 */
	public OfdbPropMapper findPropertyMapperByTabProp(final ITabSpeig tableProp) {

		for (Map.Entry<String, OfdbPropMapper> entry : this.getPropertyMap().entrySet()) {
			OfdbPropMapper mapper = entry.getValue();
			if (mapper.getColumnName().toUpperCase().equals(tableProp.getSpalte().toUpperCase())) {
				return mapper;
			}
		}

		return null;
	}

	public OfdbQueryModel getQueryModel() {
		return this.viewConfig.getQueryModel();
	}

	/**
	 * Returns the main underlying AnsichtTab-object of the configuration. That is
	 * the AnsichtTab with the table where to persist the underlying table-data
	 * (Jointype 'x').
	 *
	 * @param viewName
	 * @return
	 */
	public IAnsichtTab getMainAnsichtTab() {

		List<IAnsichtTab> ansichtTabList = this.viewConfig.getViewTabs(); // .ofdbCacheManager.getAnsichtTabList(
		// viewName );
		for (IAnsichtTab ansichtTab : ansichtTabList) {

			// FIXME: refactor expression x, do in ofdbvalidator
			if (ansichtTab.getJoinTyp().equalsIgnoreCase("x")) {
				return ansichtTab;
			}
		}

		return null;
	}

	public IAnsichtDef getViewDef() {
		return this.viewConfig.getViewDef();
	}

	public List<IAnsichtTab> getViewTabs() {
		return this.viewConfig.getViewTabs();
	}

	public ITabDef getTableByName(final String tableName) {
		for (ITabDef tabDef : this.viewConfig.getTableDefs()) {
			if (tabDef.getName().equals(tableName)) {
				return tabDef;
			}
		}

		return null;
	}

	public List<IAnsichtOrderBy> getViewOrders() {
		return this.viewConfig.getViewOrders();
	}

	public Map<String, IAnsichtSpalte> getViewColumns() {
		return this.viewConfig.getViewColumns();
	}

	public ITabSpeig findTabSpeigByAnsichtSpalte(final IAnsichtSpalte ansichtSpalte) {

		IAnsichtTab viewTab = findAnsichtTabByTabAKey(ansichtSpalte.getTabAKey());
		if (null == viewTab) {
			return null;
		}

		return this.findTabSpeigByTabAKeyAndSpalteAKey(viewTab.findTableName(), ansichtSpalte.getSpalteAKey());
	}

	public ITabSpeig findTabSpeigByTabAKeyAndSpalteAKey(final String tabAKey, final String spalteAKey) {

		IAnsichtTab viewTab = findAnsichtTabByTabAKey(tabAKey);
		List<ITabSpeig> tabSpeigs = this.viewConfig.getTableProps(viewTab.getTabDef());

		for (ITabSpeig tabSpeig : tabSpeigs) {
			if (tabSpeig.getSpalte().equals(spalteAKey)) {
				return tabSpeig;
			}
		}

		return null;
	}

	public IAnsichtTab findAnsichtTabByTabAKey(final String tabelleName) {

		for (IAnsichtTab ansichtTab : this.getViewTabs()) {
			if (ansichtTab.getTabAKey().equals(tabelleName)) {
				return ansichtTab;
			}
		}

		return null;

	}

	public List<OfdbField> getOfdbFieldList() {
		return this.viewConfig.getOfdbFieldList();
	}

	public ITabSpeig findTabSpeigByAnsichtOrderBy(final IAnsichtOrderBy ansichtOrderBy) {

		// findAnsichtTabByTabAKey( ansichtOrderBy.getTabAKey() );
		return findTabSpeigByTabAKeyAndSpalteAKey(ansichtOrderBy.getAnsichtTab().findTableName(),
				ansichtOrderBy.getSpalteAKey());

	}

	private class UniqueTabSpeigBucket {

		private List<ITabSpeig> tableProps;
		private Map<Long, List<ITabSpeig>> uniqueMap = new HashMap<Long, List<ITabSpeig>>();

		public UniqueTabSpeigBucket(final List<ITabSpeig> tableProps) {
			this.tableProps = tableProps;
			init();
		}

		private void init() {

			for (ITabSpeig tabSpeig : this.tableProps) {

				if (null != tabSpeig.getEindeutig()) {

					List<ITabSpeig> entries = this.uniqueMap.get(tabSpeig.getEindeutig());
					if (org.springframework.util.CollectionUtils.isEmpty(entries)) {
						entries = new ArrayList<ITabSpeig>();
						this.uniqueMap.put(tabSpeig.getEindeutig(), entries);
					}

					entries.add(tabSpeig);

				}

			}

		}

		private List<ITabSpeig> getUniqueTableProps(final Long uniqueIdent) {
			return this.uniqueMap.get(uniqueIdent);
		}

	}

	public List<ITabSpeig> getTabSpeigsByUniqueIdentifier(final Long uniqueIdent, ITabDef tabDef) {

		List<ITabSpeig> tableProps = getTableProps(tabDef);
		UniqueTabSpeigBucket uniqueMap = new UniqueTabSpeigBucket(tableProps);
		// this.ofdbCacheManager.findRegisteredTabSpeigs( tabDef.getName() ) );
		// List<ITabSpeig> uniqueTabSpeigs =
		// uniqueMap.getTabSpeigsByUniqueIdentifier(uniqueIdent);

		return uniqueMap.getUniqueTableProps(uniqueIdent);
	}

}
