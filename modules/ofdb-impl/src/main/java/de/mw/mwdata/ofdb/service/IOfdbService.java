package de.mw.mwdata.ofdb.service;

import java.util.List;
import java.util.Map;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.core.ofdb.exception.OfdbUniqueConstViolationException;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.domain.impl.TabSpeig;
import de.mw.mwdata.ofdb.impl.OfdbField;
import de.mw.mwdata.ofdb.impl.OfdbPropMapper;

/**
 * This interface should provide only readonly-methods from ofdb-tables.
 *
 * @author Wilbers, Markus
 * @version
 * @since
 *
 */
public interface IOfdbService {

	public IAnsichtDef findAnsichtByName(final String viewName);

	/**
	 * Loads the ansichtDef given by the id
	 */
	public IAnsichtDef findAnsichtById(final long ansichtId);

	/**
	 * Used for initialization and registration of ofdb-data for the given urlpath
	 * to the view
	 */
	public IAnsichtDef findAnsichtByUrlPath(final String urlPath);

	public List<OfdbField> initializeOfdbFields(final String viewName);

	public String buildFilteredSQL(final String viewName, final EntityTO<? extends AbstractMWEntity> filterEntityTO,
			final List<SortKey> sortKeys);

	public void presetDefaultValues(final AbstractMWEntity entity);

	public List<AnsichtDef> loadViewsForRegistration(final String applicationContextPath);

	public boolean isEmpty(final AbstractMWEntity entity) throws OfdbMissingMappingException;

	/**
	 * Loads all AnsichtTab-Objets for the given ansichtId
	 *
	 * @param ansichtId
	 * @return the list of ansichtTab-objects or empty list
	 */
	public List<IAnsichtTab> findAnsichtTabByAnsichtId(final long ansichtId);

	/**
	 * @param table
	 * @return a java-ArrayList of TabSpeigs for the given tableName
	 */
	public List<ITabSpeig> loadTablePropListByTableName(final String table);

	/**
	 * Finds a single {@link TabSpeig} by given tablename and columnname of TabSpeig
	 *
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public ITabSpeig loadTablePropByTableName(final String tableName, final String columnName);

	/**
	 * Initializes a property mapping between table columns (tabSpeig.spalte as
	 * map.key) in persistence layer and property names (as map.value) in entites
	 * from or-mapping.
	 *
	 * @param type
	 * @param tableName
	 * @param tabSpeigs
	 * @return
	 */
	public Map<String, OfdbPropMapper> initializeMapping(final Class<? extends AbstractMWEntity> type,
			final String tableName, final List<ITabSpeig> tabSpeigs);

	/**
	 * Loads all AnsichtOrderBy-Domain-Objects for the given ansichtId
	 *
	 * @param ansichtId
	 * @return the list of AnsichtOrderBy or empty list
	 */
	public List<AnsichtOrderBy> findAnsichtOrderByAnsichtId(final long ansichtId);

	/**
	 * Loads all view-based columns in a map with key = viewname and value = column
	 * given by the viewname
	 *
	 * @param ansichtName
	 * @return
	 */
	public Map<String, IAnsichtSpalte> findAnsichtSpaltenMapByAnsichtId(final long ansichtId);

	public List<Object> getListOfValues(final OfdbField ofField, final ITabSpeig tabSpeig,
			final List<IAnsichtOrderBy> ansichtOrderList, final Class<IEntity> type);

	/**
	 * Extracts the underlying table definition by the given entity and returns the
	 * relevant {@link TabDef}
	 *
	 * @param entity
	 * @return
	 */
	public TabDef findTableDefByEntity(final AbstractMWEntity entity);

	/**
	 *
	 * @param uniqueTabSpeig
	 * @param changedProps
	 * @throws OfdbUniqueConstViolationException
	 */
	public void checkUnique(final ITabSpeig uniqueTabSpeig, final AbstractMWEntity entity)
			throws OfdbUniqueConstViolationException;

	public String mapTabSpeig2Property(final ITabSpeig tabSpeig);

	public String buildSQL(final String viewName, final List<SortKey> sortKeys);

	public String buildSQLCount(final String viewName);

}
