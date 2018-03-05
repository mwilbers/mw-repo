package de.mw.mwdata.ofdb.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;
import de.mw.mwdata.ofdb.impl.OfdbField;
import de.mw.mwdata.ofdb.query.OfdbQueryModel;

public class ViewConfiguration {

	/*
	 * FIXME: Umbau geplant: a) viewColumns hier sollte
	 * Map<AnsichtDef,AnsichtSpalten> sein. Analog für andere Maps ... => also
	 * equals , hashCode methode in AnsichtDef erstellen b) ViewConfigHandle sollte
	 * keine add-methoden mehr haben, stattdessen nur noch schlaues ReadOnly machen
	 * c) add-Methoden in eine innere Klasse ViewConfiguration.Builder verschieben.
	 *
	 * ===>>> dann werden doppelte Add-Methoden entfernt und Code lesbarer
	 */

	private IAnsichtDef viewDef;
	private List<IAnsichtTab> viewTabs = new ArrayList<IAnsichtTab>();
	private Map<String, IAnsichtSpalte> viewColumns = new HashMap<String, IAnsichtSpalte>();
	private List<IAnsichtOrderBy> viewOrders = new ArrayList<IAnsichtOrderBy>();

	/**
	 * contains all underlying tables defined in viewTab. Same tables can be
	 * duplicated in more viewConfigs
	 */
	private List<ITabDef> tableDefs = new ArrayList<ITabDef>();

	/**
	 * contains the list of tableProperties for every involved table to this
	 * viewConfiguration
	 */
	private Map<ITabDef, List<ITabSpeig>> tableProps = new HashMap<ITabDef, List<ITabSpeig>>();

	private OfdbEntityMapping entityMapping;

	private OfdbQueryModel queryModel;

	private List<OfdbField> ofdbFields = new ArrayList<OfdbField>();

	/**
	 * private constructor. Use Builder for creation
	 *
	 * @param viewDef
	 */
	private ViewConfiguration(final IAnsichtDef viewDef) {
		this.viewDef = viewDef;
	}

	public static class Builder {

		ViewConfiguration viewConfig;

		public Builder(final IAnsichtDef viewDef) {
			this.viewConfig = new ViewConfiguration(viewDef);
		}

		public Builder addTableDef(final ITabDef tableDef) {
			this.viewConfig.tableDefs.add(tableDef);
			return this;
		}

		public Builder addTableProps(final ITabDef tableDef, final List<ITabSpeig> tabProps) {
			this.viewConfig.tableProps.put(tableDef, tabProps);
			return this;
		}

		public Builder addViewTab(final IAnsichtTab viewTab) {
			this.viewConfig.viewTabs.add(viewTab);
			return this;
		}

		public Builder setEntityMapping(final OfdbEntityMapping entityMapping) {
			this.viewConfig.entityMapping = entityMapping;
			return this;
		}

		public Builder addViewOrderBy(final IAnsichtOrderBy viewOrderBy) {
			this.viewConfig.viewOrders.add(viewOrderBy);
			return this;
		}

		public Builder setOfdbFields(final List<OfdbField> ofdbFields) {
			this.viewConfig.ofdbFields = ofdbFields;
			return this;
		}

		/**
		 * key = ansichtSpalte.spalteAKey.toUpperCase()
		 *
		 * @param viewColumns
		 * @return
		 */
		public Builder setViewColumns(final Map<String, IAnsichtSpalte> viewColumns) {
			this.viewConfig.viewColumns = viewColumns;
			return this;
		}

		public ViewConfigHandle buildHandle() {
			return new ViewConfigHandle(this.viewConfig);
		}

		public List<ITabSpeig> getTableProps(final ITabDef tableDef) {
			return this.viewConfig.tableProps.get(tableDef);
		}

		public Builder setQueryModel(final OfdbQueryModel queryModel) {
			this.viewConfig.queryModel = queryModel;
			return this;
		}

	}

	IAnsichtDef getViewDef() {
		return this.viewDef;
	}

	List<IAnsichtTab> getViewTabs() {
		return this.viewTabs;
	}

	List<ITabDef> getTableDefs() {
		return this.tableDefs;
	}

	List<ITabSpeig> getTableProps(final ITabDef tableDef) {
		return this.tableProps.get(tableDef);
	}

	List<IAnsichtOrderBy> getViewOrders() {
		return this.viewOrders;
	}

	/**
	 * key = ansichtSpalte.spalteAKey.toUpperCase()
	 *
	 * @return
	 */
	Map<String, IAnsichtSpalte> getViewColumns() {
		return this.viewColumns;
	}

	OfdbEntityMapping getEntityMapping() {
		return this.entityMapping;
	}

	OfdbQueryModel getQueryModel() {
		return this.queryModel;
	}

	List<OfdbField> getOfdbFieldList() {
		return this.ofdbFields;
	}

}
