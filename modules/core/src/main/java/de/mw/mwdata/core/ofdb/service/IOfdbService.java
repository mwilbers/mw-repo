package de.mw.mwdata.core.ofdb.service;

import java.util.List;
import java.util.Map;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.SortKey;
import de.mw.mwdata.core.ofdb.def.CRUD;
import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.core.ofdb.def.OfdbPropMapper;
import de.mw.mwdata.core.ofdb.domain.AnsichtDef;
import de.mw.mwdata.core.ofdb.domain.AnsichtOrderBy;
import de.mw.mwdata.core.ofdb.domain.AnsichtTab;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.core.ofdb.domain.IMenue;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.ofdb.domain.TabDef;
import de.mw.mwdata.core.ofdb.domain.TabSpeig;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.core.ofdb.exception.OfdbUniqueConstViolationException;
import de.mw.mwdata.core.utils.ITree;

/**
 * This interface should provide only readonly-methods from ofdb-tables.
 *
 * @author Wilbers, Markus
 * @version
 * @since
 *
 */
public interface IOfdbService {

	/**
	 * Loads the registerd Menu-entries for the application-menu-bar
	 */
	public ITree findMenues();

	public IAnsichtDef findAnsichtByName( final String viewName );

	/**
	 * Loads the ansichtDef given by the id
	 */
	public IAnsichtDef findAnsichtById( final long ansichtId );

	/**
	 * @return the Menue-domain by the given menu-id
	 */
	public IMenue findMenuById( final long menueId );

	/**
	 * Used for initialization and registration of ofdb-data for the given urlpath to the view
	 */
	public IAnsichtDef findAnsichtByUrlPath( final String urlPath );

	public List<OfdbField> initializeOfdbFields( final String viewName, final CRUD crud );

	public String buildFilteredSQL( final String viewName, final EntityTO<? extends AbstractMWEntity> filterEntityTO,
			final List<SortKey> sortKeys );

	public void presetDefaultValues( final AbstractMWEntity entity );

	public List<AnsichtDef> loadViewsForRegistration( final String applicationContextPath );

	// /**
	// * Loads all entities given by the viewName. Loads them sorted if sortColumns are given.
	// *
	// * @param viewName
	// * @param sortColumns
	// * @return
	// */
	// public List<IEntity[]> loadView( final String viewName, final List<SortKey>... sortKeys );

	public List<IEntity[]> executeQuery( final String sql );

	// public long executeCountQuery( final String sqlCount );

	public boolean isEmpty( final AbstractMWEntity entity ) throws OfdbMissingMappingException;

	/**
	 * Loads all AnsichtTab-Objets for the given ansichtId
	 *
	 * @param ansichtId
	 * @return the list of ansichtTab-objects or empty list
	 */
	public List<AnsichtTab> findAnsichtTabByAnsichtId( final long ansichtId );

	/**
	 * @param table
	 * @return a java-ArrayList of TabSpeigs for the given tableName
	 */
	public List<ITabSpeig> loadTablePropListByTableName( final String table );

	/**
	 * Finds a single {@link TabSpeig} by given tablename and columnname of TabSpeig
	 *
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public ITabSpeig loadTablePropByTableName( final String tableName, final String columnName );

	/**
	 * Initializes a property mapping between table columns (tabSpeig.spalte as map.key) in persistence layer and
	 * property names (as map.value) in entites from or-mapping.
	 *
	 * @param type
	 * @param tableName
	 * @param tabSpeigs
	 * @return
	 */
	public Map<String, OfdbPropMapper> initializeMapping( final Class<? extends AbstractMWEntity> type,
			final String tableName, final List<ITabSpeig> tabSpeigs );

	/**
	 * Loads all AnsichtOrderBy-Domain-Objects for the given ansichtId
	 *
	 * @param ansichtId
	 * @return the list of AnsichtOrderBy or empty list
	 */
	public List<AnsichtOrderBy> findAnsichtOrderByAnsichtId( final long ansichtId );

	/**
	 * Loads all view-based columns in a map with key = viewname and value = column given by the viewname
	 *
	 * @param ansichtName
	 * @return
	 */
	public Map<String, IAnsichtSpalte> findAnsichtSpaltenMapByAnsichtId( final long ansichtId );

	public List<Object> getListOfValues( final OfdbField ofField, final ITabSpeig tabSpeig,
			final List<IAnsichtOrderBy> ansichtOrderList, final Class<? extends AbstractMWEntity> type );

	/**
	 * Extracts the underlying table definition by the given entity and returns the relevant {@link TabDef}
	 *
	 * @param entity
	 * @return
	 */
	public TabDef findTableDefByEntity( final AbstractMWEntity entity );

	/**
	 *
	 * @param uniqueTabSpeig
	 * @param changedProps
	 * @throws OfdbUniqueConstViolationException
	 */
	public void checkUnique( final ITabSpeig uniqueTabSpeig, final AbstractMWEntity entity )
			throws OfdbUniqueConstViolationException;

	public String mapTabSpeig2Property( final ITabSpeig tabSpeig );

	public String buildSQL( final String viewName, final List<SortKey> sortKeys );

	String buildSQLCount( final String viewName );

}