package de.mw.mwdata.ofdb.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import de.mw.mwdata.core.CRUD;
import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.LocalizedMessages;
import de.mw.mwdata.core.db.FxBooleanType;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.intercept.AbstractCrudChain;
import de.mw.mwdata.core.intercept.ICrudInterceptable;
import de.mw.mwdata.core.intercept.ICrudInterceptor;
import de.mw.mwdata.core.intercept.InvalidChainCheckException;
import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.query.QueryBuilder;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.query.QueryValue;
import de.mw.mwdata.core.query.SimpleQueryBuilder;
import de.mw.mwdata.core.query.ValueType;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.cache.ViewConfigValidationResultSet;
import de.mw.mwdata.ofdb.dao.IOfdbDao;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.impl.AnsichtTab;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.exception.OfdbFormatException;
import de.mw.mwdata.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.ofdb.exception.OfdbMissingObjectException;
import de.mw.mwdata.ofdb.exception.OfdbNullValueException;
import de.mw.mwdata.ofdb.exception.OfdbUniqueConstViolationException;
import de.mw.mwdata.ofdb.impl.ConfigOfdb;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;
import de.mw.mwdata.ofdb.impl.OfdbPropMapper;
import de.mw.mwdata.ofdb.query.IOfdbQueryModelService;

/**
 * Main Service for all ofdb-relevant operations. It caches the ofdb-data for
 * every registerd table.
 *
 * @author mwilbers
 * @version 1.0
 */
public class OfdbService extends AbstractCrudChain implements IOfdbService, ICrudInterceptor {

	// TODO: 1. build own logging-component
	// 2. Idee: MBeans anlegen für ausgabe der ofdb-informationen
	private static final Logger LOGGER = LoggerFactory.getLogger(OfdbService.class);

	private IOfdbDao ofdbDao;
	private OfdbCacheManager ofdbCacheManager;

	private ICrudService<IEntity> crudService;

	private IOfdbQueryModelService ofdbQueryModelService;

	public void setOfdbQueryModelService(IOfdbQueryModelService ofdbQueryModelService) {
		this.ofdbQueryModelService = ofdbQueryModelService;
	}

	protected IOfdbDao getOfdbDao() {
		return this.ofdbDao;
	}

	public void setOfdbDao(final IOfdbDao ofdbDao) {
		this.ofdbDao = ofdbDao;
	}

	public void setOfdbCacheManager(final OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	public void setCrudService(final ICrudService<IEntity> crudService) {
		this.crudService = crudService;
		if (this.crudService instanceof ICrudInterceptable) {
			ICrudInterceptable interceptor = (ICrudInterceptable) this.crudService;
			interceptor.registerCrudInterceptor(this, 0);
		}
	}

	@Override
	public IAnsichtDef findAnsichtById(final long ansichtId) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public IAnsichtDef findAnsichtByUrlPath(final String urlPath) {

		List<ViewConfigHandle> viewConfigs = this.ofdbCacheManager.getRegisteredViewConfigs();
		for (ViewConfigHandle handle : viewConfigs) {
			String path = handle.getViewDef().getUrlPath();
			if (path.equals(urlPath)) {
				return handle.getViewDef();
			}
		}

		QueryBuilder b = new SimpleQueryBuilder();
		String sql = b.selectEntity(ConfigOfdb.T_VIEWDEF, "v").fromEntity(ConfigOfdb.T_VIEWDEF, "v")
				.andWhereRestriction("v", "urlPath", OperatorEnum.Eq, urlPath, ValueType.STRING).buildSQL();
		QueryResult result = this.crudService.executeSql(sql);

		if (result.isEmpty()) {
			String message = MessageFormat.format("No View configurations registered in ofdb cache for urlPath {0}",
					urlPath);
			LOGGER.warn(message);
			return null;

		}

		return (IAnsichtDef) result.getEntityByRowIndex(0);
	}

	@Override
	public IAnsichtDef findAnsichtByName(final String viewName) {

		if (this.ofdbCacheManager.isViewRegistered(viewName)) {
			ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(viewName);
			return viewHandle.getViewDef();
		}

		QueryBuilder b = new SimpleQueryBuilder();
		String sql = b.selectEntity(ConfigOfdb.T_VIEWDEF, "v").fromEntity(ConfigOfdb.T_VIEWDEF, "v")
				.andWhereRestriction("v", "name", OperatorEnum.Eq, viewName, ValueType.STRING).buildSQL();

		QueryResult result = this.crudService.executeSql(sql);
		if (result.isEmpty()) {
			String message = MessageFormat.format("ViewDef {0} is not found. ", viewName);
			throw new OfdbMissingObjectException(message);
		}

		return (IAnsichtDef) result.getEntityByRowIndex(0);
	}

	private Object getOfdbDefault(final ITabSpeig tabSpeig) throws OfdbNullValueException {

		Object result = null;
		switch (tabSpeig.getDbDatentyp()) {
		case BOOLEAN:
			if (null != tabSpeig.getDefaultWert()) {
				result = FxBooleanType.defBoolean((String) tabSpeig.getDefaultWert());
			} else {
				if (tabSpeig.getEingabeNotwendig()) {

					// TODO: replace with logical check in TabDefService:
					// if BOOLEAN and eingabeNotwendig == null : -> error
					String msg = LocalizedMessages.getString(ConfigOfdb.BUNDLE_NAME_OFDB, "illegalOfdbNullValue",
							tabSpeig.getTabDef().getName(), tabSpeig.getSpalte());
					throw new OfdbNullValueException(msg);
				}
			}
			break;
		case STRING:
			if (null != tabSpeig.getDefaultWert()) {
				if (Constants.MWDATADEFAULT.USERID.getName().equals(tabSpeig.getDefaultWert())) {
					result = Constants.SYS_USER_DEFAULT; // FIXME: has to be replaced by real userid
				} else {
					result = tabSpeig.getDefaultWert();
				}
			} else {
				if (tabSpeig.getEingabeNotwendig()) {

					// TODO: replace with logical check in TabDefService:
					// if BOOLEAN and eingabeNotwendig == null : -> error
					String msg = LocalizedMessages.getString(ConfigOfdb.BUNDLE_NAME_OFDB, "illegalOfdbNullValue",
							tabSpeig.getTabDef().getName(), tabSpeig.getSpalte());
					throw new OfdbNullValueException(msg);
				}
			}
			break;
		case LONGINTEGER:
			if (null != tabSpeig.getDefaultWert()) {
				try {
					result = Long.parseLong((String) tabSpeig.getDefaultWert());
				} catch (NumberFormatException ne) {
					String msg = LocalizedMessages.getString(ConfigOfdb.BUNDLE_NAME_OFDB, "invalidDefaultValue",
							tabSpeig.getTabDef().getName(), tabSpeig.getSpalte());
					throw new OfdbFormatException(msg, ne);
				}
			} else {
				if (tabSpeig.getEingabeNotwendig()) {

					// TODO: replace with logical check in TabDefService:
					// if BOOLEAN and eingabeNotwendig == null : -> error
					String msg = LocalizedMessages.getString(ConfigOfdb.BUNDLE_NAME_OFDB, "illegalOfdbNullValue",
							tabSpeig.getTabDef().getName(), tabSpeig.getSpalte());
					throw new OfdbNullValueException(msg);
				}
			}
			break;
		case ENUM: {
			if (null != tabSpeig.getDefaultWert()) {
				result = tabSpeig.getDefaultWert();

			} else {
				if (tabSpeig.getEingabeNotwendig()) {

					// TODO: replace with logical check in TabDefService:
					// if BOOLEAN and eingabeNotwendig == null : -> error
					String msg = LocalizedMessages.getString(ConfigOfdb.BUNDLE_NAME_OFDB, "illegalOfdbNullValue",
							tabSpeig.getTabDef().getName(), tabSpeig.getSpalte());
					throw new OfdbNullValueException(msg);
				}
			}
			break;
		}
		case DATE: {
			if (Constants.MWDATADEFAULT.NOW.getName().equals(tabSpeig.getDefaultWert())) {
				result = new Date();
			}
			break;
		}
		default:

			break;
		}

		return result;
	}

	@Override
	public void presetDefaultValues(final AbstractMWEntity entity) {

		String urlPath = ClassNameUtils.convertClassNameToUrlPath(entity);
		IAnsichtDef viewDef = this.findAnsichtByUrlPath(urlPath);
		if (null == viewDef) {
			return;
		}

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(viewDef.getName());
		IAnsichtTab viewToTable = viewHandle.getMainAnsichtTab();

		List<ITabSpeig> tabSpeigs = viewHandle.getTableProps(viewToTable.getTabDef());
		for (ITabSpeig tabSpeig : tabSpeigs) {

			Object value = null;

			OfdbPropMapper propMapper = viewHandle.findPropertyMapperByTabProp(tabSpeig);

			if (null == propMapper) {
				continue;
			}

			value = this.getOfdbDao().getEntityValue(entity, propMapper.getPropertyIndex());
			if (null == value) {
				value = getOfdbDefault(tabSpeig);
				if (null != value) {
					this.getOfdbDao().setEntityValue(entity, value, tabSpeig, propMapper);
					LOGGER.info("Default Value set for Table " + viewToTable.getTabDef().getName() + ", TabSpeig "
							+ tabSpeig.getSpalte() + ", defaultvalue: " + value);
				}

			}

		}

	}

	@Override
	public List<OfdbField> initializeOfdbFields(final String viewName) {
		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(viewName);
		return viewHandle.getQueryModel().getMetaData();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AnsichtDef> loadViewsForRegistration(final String nameBenutzerBereich) {
		return this.getOfdbDao().loadViewsForRegistration(nameBenutzerBereich);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean isEmpty(final AbstractMWEntity entity) throws OfdbMissingMappingException {

		TabDef tabDef = findTableDefByEntity(entity);

		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(tabDef.getName());
		List<ITabSpeig> tabSpeigs = viewHandle.getTableProps(tabDef);

		boolean isEmpty = true;
		for (ITabSpeig tabSpeig : tabSpeigs) {
			OfdbPropMapper propMapper = viewHandle.findPropertyMapperByTabProp(tabSpeig);

			if (null == propMapper) {
				// FIXME: should not happen. happens e.g. for TabDef.bereich but should be
				// removed in future because of
				// mapping bereichId / BenutzerBereich.name
				LOGGER.warn("TabSpeig not mapped: " + tabSpeig.getTabDef().getName() + "." + tabSpeig.getSpalte());
				continue;

			}

			Object entityValue = this.getOfdbDao().getEntityValue(entity, propMapper.getPropertyIndex());

			if (entityValue instanceof String) {
				if (!StringUtils.isBlank((String) entityValue)) {
					return false;
				}
			} else {
				if (null != entityValue) {
					return false;
				}
			}

		}

		return isEmpty;
	}

	@Override
	public List<IAnsichtTab> findAnsichtTabByAnsichtId(final long ansichtId) {
		return this.getOfdbDao().findAnsichtTabAnsichtId(ansichtId);
	}

	@Override
	public List<ITabSpeig> loadTablePropListByTableName(final String table) {
		return this.getOfdbDao().findTabSpeigByTable(table);
	}

	@Override
	public OfdbEntityMapping initializeMapping(final Class<? extends AbstractMWEntity> type, final String tableName,
			final List<ITabSpeig> tabSpeigs) {

		OfdbEntityMapping entityMapping = this.getOfdbDao().initializeMapping(type, tableName);

		for (ITabSpeig tabSpeig : tabSpeigs) {
			if (tabSpeig.getPrimSchluessel()) {
				if (!entityMapping.hasMapping(tabSpeig)) {
					throw new OfdbInvalidConfigurationException("Invalid Property Mapping on primary key column "
							+ tabSpeig.getSpalte().toUpperCase() + ". No property found on entity "
							+ type.getClass().getName() + " with this columnname.");
				}

			}
		}

		return entityMapping;

	}

	@Override
	public List<AnsichtOrderBy> findAnsichtOrderByAnsichtId(final long ansichtId) {
		return this.getOfdbDao().findAnsichtOrderByAnsichtId(ansichtId);
	}

	@Override
	public List<IAnsichtSpalte> findAnsichtSpaltenByAnsichtId(final long ansichtId) {
		return this.getOfdbDao().findAnsichtSpaltenByAnsicht(ansichtId);
	}

	@Override
	public List<Object> getListOfValues(final OfdbField ofField, final ITabSpeig tabSpeig,
			final List<IAnsichtOrderBy> ansichtOrderList, final Class<IEntity> type) {

		// if no hibernate-specific entity-property found
		if (!ofField.isMapped()) {
			return Collections.emptyList();
		}

		if (tabSpeig.isEnum()) {

			String fullClassName = tabSpeig.getTabDef().getFullClassName();
			Class<? extends AbstractMWEntity> entityClassType = null;

			try {
				entityClassType = ClassNameUtils.getClassType(fullClassName);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return this.getOfdbDao().getEnumValues(entityClassType, ofField.getPropName());
		} else if (null != type) {
			Map<String, String> sortColumns = convertAnsichtOrderListToSortColumns(ansichtOrderList);
			List<IEntity> lovs = this.crudService.findAll(type, sortColumns);

			return ListUtils.unmodifiableList(lovs);
		}

		return Collections.emptyList();

	}

	/**
	 * Converts the list of AnsichtOrderBy-elements to a map with key = spalte and
	 * value = asc / desc
	 *
	 * @param viewOrderList
	 * @return
	 */
	private Map<String, String> convertAnsichtOrderListToSortColumns(final List<IAnsichtOrderBy> viewOrderList) {

		Map<String, String> sortColumns = new HashMap<String, String>();
		if (null != viewOrderList && viewOrderList.size() > 0) {
			for (IAnsichtOrderBy ansichtOrderBy : viewOrderList) {

				ViewConfigHandle viewHandle = this.ofdbCacheManager
						.getViewConfig(ansichtOrderBy.getAnsichtTab().getAnsichtDef().getName());
				ITabSpeig tabProp = viewHandle.findTabSpeigByAnsichtOrderBy(ansichtOrderBy);
				OfdbPropMapper propMapper = viewHandle.findPropertyMapperByTabProp(tabProp);
				String asc = (ansichtOrderBy.getAufsteigend() ? "asc" : "desc");

				sortColumns.put(propMapper.getPropertyName(), asc);
			}

		}

		return sortColumns;

	}

	@Override
	public void doChainActionsBeforeCheck(final AbstractMWEntity entity, final CRUD crud) {

		updateAssociationPropertyValues(entity);

		presetDefaultValues(entity);

		if (null != this.nextChainItem) {
			this.nextChainItem.doChainActionsBeforeCheck(entity, crud);
		}
	}

	private void updateAssociationPropertyValues(AbstractMWEntity entity) {

		String urlPath = ClassNameUtils.convertClassNameToUrlPath(entity);
		IAnsichtDef viewDef = findAnsichtByUrlPath(urlPath);
		if (null == viewDef) {
			return;
		}

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(viewDef.getName());
		IAnsichtTab viewToTable = viewHandle.getMainAnsichtTab();

		List<ITabSpeig> tabSpeigs = viewHandle.getTableProps(viewToTable.getTabDef());
		for (ITabSpeig tabSpeig : tabSpeigs) {

			OfdbPropMapper propMapper = viewHandle.findPropertyMapperByTabProp(tabSpeig);

			if (null == propMapper) {
				continue;
			}

			if (propMapper.hasAssociatedEntity()) {
				Long id = (Long) this.getOfdbDao().getEntityValue(entity, propMapper.getPropertyIndex());
				ITabSpeig entityProp = viewHandle.findTablePropByProperty(tabSpeig.getTabDef(),
						propMapper.getAssociatedEntityName(), true);

				OfdbPropMapper entityPropMapper = viewHandle.findPropertyMapperByTabProp(entityProp);
				IEntity associatedEntity = (IEntity) this.getOfdbDao().getEntityValue(entity,
						entityPropMapper.getAssociatedPropertyIndex());

				if (id != associatedEntity.getId()) {
					associatedEntity = this.crudService.findById((Class<IEntity>) associatedEntity.getClass(), id);
					this.getOfdbDao().setEntityValue(entity, associatedEntity, entityProp,
							entityPropMapper.getAssociatedPropertyMapper());
				}

			}

		}

	}

	@Override
	public TabDef findTableDefByEntity(final AbstractMWEntity entity) {
		String fullClassName = entity.getClass().getName();
		return this.getOfdbDao().findTableDefByFullClassName(fullClassName); // (TabDef) o;
	}

	public void checkAllUniques(final AbstractMWEntity entity, final CRUD crud)
			throws OfdbUniqueConstViolationException {

		TabDef tabDef = findTableDefByEntity(entity);
		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(tabDef.getName());
		OfdbEntityMapping entityMapping = viewHandle.getEntityMapping(tabDef.getName());

		for (OfdbPropMapper mapper : entityMapping.getMappings()) {
			if (mapper.isAssociationType()) {
				continue;
			}

			ITabSpeig tableProp = viewHandle.findTablePropByProperty(tabDef, mapper.getPropertyName(), false);
			if (null == tableProp) {
				continue; // e.g. TabSpeig.getName() runs here ...
			}

			checkUnique(tableProp, entity);

		}

	}

	@Override
	public void checkUnique(final ITabSpeig tableProp, final AbstractMWEntity entity)
			throws OfdbUniqueConstViolationException {

		ITabDef tabDef = tableProp.getTabDef();
		if (tableProp.isEindeutig()) {

			ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(tabDef.getName());
			List<ITabSpeig> uniqueTabSpeigs = viewHandle.getTabSpeigsByUniqueIdentifier(tableProp.getEindeutig(),
					tabDef);

			OfdbEntityMapping entityMapping = this.ofdbCacheManager.getEntityMapping(tabDef.getName());

			QueryBuilder builder = new SimpleQueryBuilder();
			builder.selectEntity(entity.getClass().getSimpleName(), "tAlias")
					.fromEntity(entity.getClass().getSimpleName(), "tAlias");

			boolean uniquePropsToCheck = false;
			for (ITabSpeig tabSpeig : uniqueTabSpeigs) {
				// FIXME: if more than one unique prop, than this check is only needed for one
				// of all

				Object entityValue = null;
				OfdbPropMapper propMapper = entityMapping.getMapper(tabSpeig);
				String uniquePropNameItem = propMapper.getPropertyName();

				entityValue = this.ofdbDao.getEntityValue(entity, propMapper.getPropertyIndex());

				if (ObjectUtils.isEmpty(entityValue)) {
					if (tabSpeig.getEingabeNotwendig()) {
						String msg = MessageFormat.format("Column {0} of table {1} must not be null.",
								tabSpeig.getSpalte(), tabSpeig.getTabDef().getName());
						throw new OfdbNullValueException(msg);
					} else {
						continue;
					}
				}

				QueryValue queryValue = this.ofdbQueryModelService.convertValueToQueryValue(entityValue, tabSpeig);
				builder = builder.andWhereRestriction("tAlias", uniquePropNameItem, OperatorEnum.Eq,
						queryValue.getData(), ValueType.STRING);
				uniquePropsToCheck = true;

			}

			if (uniquePropsToCheck) {

				builder = builder.andWhereRestriction("tAlias", Constants.SYS_PROP_ID, OperatorEnum.NotEq,
						entity.getId(), ValueType.NUMBER);

				String sql = builder.buildSQL();
				QueryResult result = this.crudService.executeSql(sql);
				if (!result.isEmpty()) {
					String message = "Unique violation. TableDef: " + tabDef.getName() + ", tableProperty: "
							+ tableProp.getSpalte();
					throw new OfdbUniqueConstViolationException(message);
				}
			}

		}

	}

	@Override
	public void doChainCheck(final AbstractMWEntity entity, final CRUD crud) throws InvalidChainCheckException {

		try {
			checkAllUniques(entity, crud);

		} catch (OfdbUniqueConstViolationException e) {
			throw new InvalidChainCheckException("Unique check violation: " + e.getLocalizedMessage(), e);
		}

		if (null != this.nextChainItem) {
			this.nextChainItem.doChainCheck(entity, crud);
		}

	}

	public ViewConfigValidationResultSet isAnsichtValid(final IAnsichtDef ansichtDef) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();
		if (StringUtils.isEmpty(ansichtDef.getUrlPath())) {
			set.addValidationResult("illegalOfdbNullValue", ansichtDef.getName(), "urlPath");
		}

		return set;
	}

	private void isViewTabUnique(final List<IAnsichtTab> ansichtTabList, final ViewConfigValidationResultSet set) {

		Map<String, List<IAnsichtTab>> map = new HashMap<String, List<IAnsichtTab>>();

		for (IAnsichtTab ansichtTab : ansichtTabList) {

			String tableName = ansichtTab.getTabDef().getName();
			List<IAnsichtTab> aTabs = null;
			if (map.containsKey(tableName)) {

				aTabs = map.get(tableName);
				for (IAnsichtTab aTab : aTabs) {

					// if duplicate tabAKey per refrenced table
					if (aTab.getTabAKey().equalsIgnoreCase(ansichtTab.getTabAKey())) {
						set.addValidationResult("invalidOfdbConfig.FX_AnsichtTab.TabAKey", tableName,
								ansichtTab.getAnsichtDef().getName());
					}

				}

			} else {
				aTabs = new ArrayList<IAnsichtTab>();
				map.put(tableName, aTabs);
			}

			AnsichtTab viewTab = (AnsichtTab) ansichtTab;
			if (ansichtTab.getTabAKey().equalsIgnoreCase(tableName)) {

				if (!StringUtils.isBlank(viewTab.getTabelle())) {
					set.addValidationResult("invalidOfdbConfig.FX_AnsichtTab.TabelleNull", tableName,
							ansichtTab.getAnsichtDef().getName());
				}

			} else {
				if (StringUtils.isBlank(viewTab.getTabelle())) {

					set.addValidationResult("invalidOfdbConfig.FX_AnsichtTab.TabelleNotNull", tableName,
							ansichtTab.getAnsichtDef().getName());

				}
			}
			aTabs = map.get(tableName);
			aTabs.add(ansichtTab);

		}
	}

	public ViewConfigValidationResultSet isAnsichtTabListValid(final IAnsichtDef ansichtDef,
			final List<IAnsichtTab> ansichtTabList) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();

		List<String> foundTables = new ArrayList<String>();
		for (IAnsichtTab viewTab : ansichtTabList) {
			if (viewTab.getJoinTyp().equals("x")) {
				foundTables.add(viewTab.getTabDef().getName());
			}

			if (foundTables.size() > 1) {
				set.addValidationResult("invalidOfdbConfig.FX_AnsichtTab.duplicatedMainViewTab", ansichtDef.getName(),
						foundTables.toString());
			}

		}

		isViewTabUnique(ansichtTabList, set);

		return set;

	}

	public ViewConfigValidationResultSet isTableValid(final ITabDef tabDef, final List<ITabSpeig> tableProps) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();

		// check fullClassName
		checkFullClassNameWellDefined(tabDef, set);

		// check Primary key definitions
		checkOnePrimaryKeyProperty(tabDef, tableProps, set);

		return set;
	}

	/**
	 * Method checks that table property 'fullClassName' is well defined
	 * 
	 * @param tabDef
	 * @param set
	 */
	private void checkFullClassNameWellDefined(final ITabDef tabDef, ViewConfigValidationResultSet set) {
		if (StringUtils.isEmpty(tabDef.getFullClassName())) {
			set.addValidationResult("illegalOfdbNullValue", tabDef.getName(), "fullClassName");

		} else {
			// check if fullClassName is valid
			try {
				ClassNameUtils.getClassType(tabDef.getFullClassName());
			} catch (ClassNotFoundException e) {
				set.addValidationResult("invalidOfdbValue", tabDef.getName(), "fullClassName",
						tabDef.getFullClassName());
			}
		}
	}

	/**
	 * Method checks that there is only one table property that defines the primary
	 * key column
	 * 
	 * @param tabDef
	 * @param tableProps
	 * @param set
	 */
	private void checkOnePrimaryKeyProperty(final ITabDef tabDef, final List<ITabSpeig> tableProps,
			ViewConfigValidationResultSet set) {

		List<ITabSpeig> tempPrimKeys = new ArrayList<ITabSpeig>();
		for (ITabSpeig tabSpeig : tableProps) {
			if (tabSpeig.getPrimSchluessel()) {
				tempPrimKeys.add(tabSpeig);
			}
		}

		if (tempPrimKeys.size() > 1) {
			set.addValidationResult("invalidOfdbConfig.FX_TabSpeig.combinedPrimKeyColumns", tabDef.getName());
		}
	}

	public ViewConfigValidationResultSet isAnsichtSpalteValid(final IAnsichtSpalte spalte,
			final ViewConfigHandle viewHandle) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();

		if (!StringUtils.isEmpty(spalte.getAnsichtSuchen())) {

			if (StringUtils.isEmpty(spalte.getSuchwertAusTabAKey())
					|| StringUtils.isEmpty(spalte.getSuchwertAusSpalteAKey())) {
				set.addValidationResult("invalidOfdbConfig.FX_AnsichtSpalten.AnsichtSuchen", spalte.getSpalteAKey(),
						spalte.getName());
			}

			ITabSpeig suchTabSpeig = viewHandle.findTabSpeigByTabAKeyAndSpalteAKey(spalte.getVerdeckenDurchTabAKey(),
					spalte.getVerdeckenDurchSpalteAKey());
			ViewConfigHandle viewHandleSuchen = this.ofdbCacheManager.getViewConfig(spalte.getAnsichtSuchen());
			if (spalte.getSuchwertAusTabAKey().equals(spalte.getTabAKey())) {
				viewHandleSuchen = viewHandle;
			}

			if (!viewHandleSuchen.getEntityMapping(suchTabSpeig.getTabDef().getName()).hasMapping(suchTabSpeig)) {
				set.addValidationResult(
						"invalidOfdbConfig.FX_AnsichtTab.noPropMapping_AnsichtSpalte_SuchWertAusTabAKey",
						spalte.getVerdeckenDurchTabAKey(), spalte.getVerdeckenDurchSpalteAKey(),
						viewHandle.getViewDef().getName());
			}

			IAnsichtDef ansichtDef = viewHandleSuchen.getViewDef();

			// FIXME: null-check no more needed when we use db-foreign-keys on ansichtDefId
			if (null == ansichtDef) {
				// String msg =
				// "Fehlende AnsichtDef für Verknüpfung im Feld
				// FX_AnsichtSpalten_K.AnsichtSuchen für Ansicht: "
				// + spalte.getName() + ", Spalte: " + spalte.getSpalteAKey();
				// result.setErrorMessage( msg );
				set.addValidationResult("invalidOfdbConfig.FX_AnsichtSpalten.AnsichtSuchen_missingEntry",
						spalte.getName(), spalte.getSpalteAKey());
			}

			ITabDef suchTabDef = this.ofdbCacheManager.findRegisteredTableDef(spalte.getSuchwertAusTabAKey());
			if (spalte.getSuchwertAusTabAKey().equals(spalte.getTabAKey())) {
				viewHandleSuchen = viewHandle;
				suchTabDef = viewHandle.getMainAnsichtTab().getTabDef();
			} else if (null == suchTabDef) {
				set.addValidationResult(
						"invalidOfdbConfig.FX_AnsichtTab.missingTabDef_AnsichtSpalte_SuchWertAusTabAKey",
						spalte.getName(), spalte.getSpalteAKey()); // -> siehe aufruf createOfdbFields()
			}

			IAnsichtTab ansichtTabSuchen = viewHandleSuchen.findAnsichtTabByTabAKey(spalte.getSuchwertAusTabAKey());
			if (null == ansichtTabSuchen) {
				// String message =
				// "Fehlende Tabelle für Verknüpfung in FX_AnsichtTab_K zum Feld
				// FX_AnsichtSpalten_K.SuchWertAusTabAKey für Ansicht: "
				// + spalte.getName() + ", Spalte: " + spalte.getSpalteAKey();
				set.addValidationResult(
						"invalidOfdbConfig.FX_AnsichtTab.missingMapping_AnsichtSpalte_SuchWertAusTabAKey",
						spalte.getName(), spalte.getSpalteAKey()); // -> siehe aufruf createOfdbFields()
			}

			if (!StringUtils.isEmpty(spalte.getVerdeckenDurchTabAKey())
					&& !StringUtils.isEmpty(spalte.getVerdeckenDurchSpalteAKey())) {

				if (!spalte.getVerdeckenDurchTabAKey().equals(spalte.getSuchwertAusTabAKey())) {
					// String message = LocalizedMessages.getString( Config.BUNDLE_NAME,
					// "invalidOfdbConfig.FX_AnsichtSpalten.VerdeckenDurchTabAKey",
					// spalte.getVerdeckenDurchTabAKey() );
					// result.setErrorMessage( message );
					set.addValidationResult("invalidOfdbConfig.FX_AnsichtSpalten.VerdeckenDurchTabAKey",
							spalte.getVerdeckenDurchTabAKey());
				}

				ITabDef verdeckenTabDef = this.ofdbCacheManager
						.findRegisteredTableDef(spalte.getVerdeckenDurchTabAKey());
				if (spalte.getSuchwertAusTabAKey().equals(spalte.getTabAKey())) {
					verdeckenTabDef = viewHandle.getMainAnsichtTab().getTabDef();
				}
				if (null == verdeckenTabDef) {
					set.addValidationResult(
							"invalidOfdbConfig.FX_AnsichtTab.missingTabDef_AnsichtSpalte_VerdeckenDurchTabAKey",
							spalte.getName(), spalte.getSpalteAKey()); // -> siehe aufruf createOfdbFields()
				}

			}

		}

		return set;

	}

	public ViewConfigValidationResultSet isViewOrderByValid(final IAnsichtOrderBy ansichtOrderBy,
			final ViewConfigHandle viewHandle) {

		return new ViewConfigValidationResultSet();
	}

}
