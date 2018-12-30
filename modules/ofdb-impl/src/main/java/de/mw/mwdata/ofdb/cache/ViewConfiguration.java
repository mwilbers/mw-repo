package de.mw.mwdata.ofdb.cache;

import java.util.ArrayList;
import java.util.List;

import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;
import de.mw.mwdata.ofdb.query.OfdbQueryModel;

/**
 * Logical configuration object holding all ofdb relevant data for one given
 * view definition. Because of the private constructor there is no public access
 * to this class. Instead use {@link ViewConfigHandle} as smart handle object
 * managing logical lookup, loading, filtering an sorting operations on
 * ViewConfiguration object.
 * 
 * @author WilbersM
 *
 */
public class ViewConfiguration {

	private IAnsichtDef viewDef;
	private List<IAnsichtTab> viewTabs = new ArrayList<IAnsichtTab>();
	private List<IAnsichtSpalte> viewColumns = new ArrayList<IAnsichtSpalte>();
	private List<IAnsichtOrderBy> viewOrders = new ArrayList<IAnsichtOrderBy>();

	private class TableConfig {
		private ITabDef tableDef;
		private List<ITabSpeig> tableProps;
		private OfdbEntityMapping entityMapping;

		TableConfig(final ITabDef tableDef, final List<ITabSpeig> tableProps, final OfdbEntityMapping entityMapping) {
			this.tableDef = tableDef;
			this.tableProps = tableProps;
			this.entityMapping = entityMapping;
		}

	}

	private List<TableConfig> tableDefs = new ArrayList<TableConfig>();

	private OfdbQueryModel queryModel;

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

		public Builder addTableConfig(final ITabDef tableDef, final List<ITabSpeig> tableProps,
				final OfdbEntityMapping entityMapping) {

			TableConfig config = viewConfig.new TableConfig(tableDef, tableProps, entityMapping);
			this.viewConfig.tableDefs.add(config);
			return this;
		}

		public Builder addViewTab(final IAnsichtTab viewTab) {
			this.viewConfig.viewTabs.add(viewTab);
			return this;
		}

		public Builder addViewOrderBy(final IAnsichtOrderBy viewOrderBy) {
			this.viewConfig.viewOrders.add(viewOrderBy);
			return this;
		}

		/**
		 * key = ansichtSpalte.spalteAKey.toUpperCase()
		 *
		 * @param viewColumns
		 * @return
		 */
		public Builder setViewColumns(final List<IAnsichtSpalte> viewColumns) {
			this.viewConfig.viewColumns = viewColumns;
			return this;
		}

		public ViewConfigHandle buildHandle() {
			return new ViewConfigHandle(this.viewConfig);
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

	List<ITabDef> getTablesDefs() {
		List<ITabDef> tables = new ArrayList<ITabDef>();
		for (TableConfig tableConfig : this.tableDefs) {
			tables.add(tableConfig.tableDef);
		}

		return tables;
	}

	List<ITabSpeig> getTableProps(final ITabDef tableDef) {
		TableConfig tableConfig = findTableConfig(tableDef.getName());
		if (null == tableConfig) {
			return new ArrayList<ITabSpeig>();
		}
		return tableConfig.tableProps;
	}

	private TableConfig findTableConfig(final String tableName) {
		for (TableConfig config : this.tableDefs) {
			if (config.tableDef.getName().equals(tableName)) {
				return config;
			}
		}
		return null;
	}

	List<IAnsichtOrderBy> getViewOrders() {
		return this.viewOrders;
	}

	/**
	 * key = ansichtSpalte.spalteAKey.toUpperCase()
	 *
	 * @return
	 */
	List<IAnsichtSpalte> getViewColumns() {
		return this.viewColumns;
	}

	OfdbEntityMapping getEntityMapping(final String tableName) {
		TableConfig tableConfig = findTableConfig(tableName);
		if (null == tableConfig) {
			return null;
		}
		return tableConfig.entityMapping;
	}

	OfdbQueryModel getQueryModel() {
		return this.queryModel;
	}

	@Override
	public String toString() {
		return this.getViewDef().toString();
	}

}
