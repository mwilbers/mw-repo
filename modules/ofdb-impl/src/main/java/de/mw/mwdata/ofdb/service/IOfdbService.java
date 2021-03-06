package de.mw.mwdata.ofdb.service;

import java.util.List;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.cache.ViewConfigValidationResultSet;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.ofdb.exception.OfdbUniqueConstViolationException;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;

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

	// public String buildFilteredSQL(final String viewName, final EntityTO<?
	// extends AbstractMWEntity> filterEntityTO,
	// final List<SortKey> sortKeys);

	// public void presetDefaultValues(final AbstractMWEntity entity);

	public List<AnsichtDef> loadViewsForRegistration(final String nameBenutzerBereich);

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
	 * Initializes a property mapping between table columns (tabSpeig.spalte as
	 * map.key) in persistence layer and property names (as map.value) in entites
	 * from or-mapping.
	 *
	 * @param type
	 * @param tableName
	 * @param tabSpeigs
	 * @return
	 */
	public OfdbEntityMapping initializeMapping(final Class<? extends AbstractMWEntity> type, final String tableName,
			final List<ITabSpeig> tabSpeigs);

	/**
	 * Loads all AnsichtOrderBy-Domain-Objects for the given ansichtId
	 *
	 * @param ansichtId
	 * @return the list of AnsichtOrderBy or empty list
	 */
	public List<AnsichtOrderBy> findAnsichtOrderByAnsichtId(final long ansichtId);

	/**
	 * Loads all view-based columns in a list given by view id
	 *
	 * @param ansichtName
	 * @return
	 */
	public List<IAnsichtSpalte> findAnsichtSpaltenByAnsichtId(final long ansichtId);

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

	/**
	 * Checks if all properties of the given AnsichtDef-object are valid for the
	 * OFDB-Configuration
	 *
	 * @param ansichtDef
	 * @param set
	 */
	public ViewConfigValidationResultSet isAnsichtValid(final IAnsichtDef ansichtDef);

	/**
	 * Checks if the given tableDef is valid for the OFDB-Configuration
	 *
	 * @param tabDef
	 * @param tabSpeigs
	 *            all table properties belonging to the given tabDef
	 * @param set
	 */
	public ViewConfigValidationResultSet isTableValid(final ITabDef tabDef, List<ITabSpeig> tabSpeigs);

	/**
	 * Checks if all ansichtTab-objects of the given ansichtDef are valid for the
	 * OFDB-configuration
	 *
	 * @param ansichtDef
	 * @param ansichtTabList
	 * @param set
	 */
	public ViewConfigValidationResultSet isAnsichtTabListValid(final IAnsichtDef ansichtDef,
			final List<IAnsichtTab> ansichtTabList);

	/**
	 * Checks if the AnsichtSpalte is valid for the OFDB-configuration
	 *
	 * @param spalte
	 * @param viewHandle
	 */
	public ViewConfigValidationResultSet isAnsichtSpalteValid(final IAnsichtSpalte spalte,
			final ViewConfigHandle viewHandle);

	public ViewConfigValidationResultSet isViewOrderByValid(final IAnsichtOrderBy ansichtOrderBy,
			final ViewConfigHandle viewHandle);

}
