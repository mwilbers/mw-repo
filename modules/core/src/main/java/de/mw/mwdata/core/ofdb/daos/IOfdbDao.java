/**
 *
 */
package de.mw.mwdata.core.ofdb.daos;

import java.util.List;
import java.util.Map;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.def.OfdbPropMapper;
import de.mw.mwdata.core.ofdb.domain.AnsichtDef;
import de.mw.mwdata.core.ofdb.domain.AnsichtOrderBy;
import de.mw.mwdata.core.ofdb.domain.AnsichtTab;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.ofdb.domain.TabDef;
import de.mw.mwdata.core.ofdb.domain.TabSpeig;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;

/**
 * @author Wilbers, Markus
 * @version 1.0
 * @since Apr, 2011
 *
 */
public interface IOfdbDao {

	/**
	 * @param table
	 * @return a java-ArrayList of TabSpeigs for the given tableName
	 */
	public List<ITabSpeig> findTabSpeigByTable( final String table );

	/**
	 * @param tableName
	 * @return a java-HashMap of TabSpeigs for the given Tablename with key = TabSpeig.spalte and value = TabSpeig
	 */
	public Map<String, TabSpeig> findTabSpeigMapByTable( String tableName );

	/**
	 * Loads all view-based columns in a map with key = viewname and value = column given by the viewname
	 *
	 * @param ansichtName
	 * @return
	 */
	public Map<String, IAnsichtSpalte> findAnsichtSpaltenMapByAnsicht( final long ansichtId );

	/**
	 * Loads all AnsichtOrderBy-Domain-Objects for the given ansichtId
	 *
	 * @param ansichtId
	 * @return the list of AnsichtOrderBy or empty list
	 */
	public List<AnsichtOrderBy> findAnsichtOrderByAnsichtId( final long ansichtId );

	/**
	 * Loads all AnsichtTab-Objets for the given ansichtId
	 *
	 * @param ansichtId
	 * @return the list of ansichtTab-objects or empty list
	 */
	public List<AnsichtTab> findAnsichtTabAnsichtId( final long ansichtId );

	/**
	 * Loads the table-object given by the tablename
	 */
	public TabDef findTabDefByTableName( final String tableName );

	/**
	 * loads the ansicht-definition by ansichtname
	 */
	public IAnsichtDef findAnsichtDefByName( final String ansichtName );

	/**
	 * Initializes the Persistence-Mapper from OFDB-TabSpeigs to propertynames.
	 *
	 * @param type
	 * @param tableName
	 * @return map containing key = db column name and value = {@link OfdbMapper} object
	 */
	public Map<String, OfdbPropMapper> initializeMapper( final Class<? extends AbstractMWEntity> type,
			final String tableName );

	public Object getEntityValue( final AbstractMWEntity entity, final int propPersistenceIndex )
			throws OfdbMissingMappingException;

	public Object setEntityValue( final AbstractMWEntity entity, final Object value, final ITabSpeig tabSpeig,
			final OfdbPropMapper propMapper );

	public List<AnsichtDef> loadViewsForRegistration( final String applicationContextPath );

	public List<IEntity[]> executeQuery( final String sql );

	public List<IEntity[]> executeQueryPaginated( final String sql, final int pageIndex );

	public long executeCountQuery( final String sqlCount );

	public List<Object> getEnumValues( final Class<? extends AbstractMWEntity> entityClassType,
			final String propertyName );

	// public OfdbPropMapper mapTabSpeigToProperty( final TabSpeig tabSpeig );

	public abstract TabDef findTableDefByFullClassName( final String fullClassName );

}
