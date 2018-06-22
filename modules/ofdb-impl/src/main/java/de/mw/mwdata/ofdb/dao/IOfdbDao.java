/**
 *
 */
package de.mw.mwdata.ofdb.dao;

import java.util.List;
import java.util.Map;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;
import de.mw.mwdata.ofdb.impl.OfdbPropMapper;

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
	public List<ITabSpeig> findTabSpeigByTable(final String table);

	/**
	 * Loads all view-based columns in a map with key = viewname and value = column
	 * given by the viewname
	 *
	 * @param ansichtName
	 * @return
	 */
	public Map<String, IAnsichtSpalte> findAnsichtSpaltenMapByAnsicht(final long ansichtId);

	/**
	 * Loads all AnsichtOrderBy-Domain-Objects for the given ansichtId
	 *
	 * @param ansichtId
	 * @return the list of AnsichtOrderBy or empty list
	 */
	public List<AnsichtOrderBy> findAnsichtOrderByAnsichtId(final long ansichtId);

	/**
	 * Loads all AnsichtTab-Objets for the given ansichtId
	 *
	 * @param ansichtId
	 * @return the list of ansichtTab-objects or empty list
	 */
	public List<IAnsichtTab> findAnsichtTabAnsichtId(final long ansichtId);

	/**
	 * Initializes the Persistence-Mapper from OFDB-TabSpeigs to propertynames.
	 *
	 * @param type
	 * @param tableName
	 * @return map containing key = db column name and value = {@link OfdbMapper}
	 *         object
	 */
	public OfdbEntityMapping initializeMapping(final Class<? extends AbstractMWEntity> type, final String tableName);

	public Object getEntityValue(final AbstractMWEntity entity, final int propPersistenceIndex);

	public Object setEntityValue(final AbstractMWEntity entity, final Object value, final ITabSpeig tabSpeig,
			final OfdbPropMapper propMapper);

	public List<AnsichtDef> loadViewsForRegistration(final String nameBenutzerBereich);

	public List<Object> getEnumValues(final Class<? extends AbstractMWEntity> entityClassType,
			final String propertyName);

	public abstract TabDef findTableDefByFullClassName(final String fullClassName);

}
