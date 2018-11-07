package de.mw.mwdata.ofdb.query.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.LocalizedMessages;
import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.IFxEnum;
import de.mw.mwdata.core.domain.JoinedPropertyTO;
import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.query.QueryBuilder;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.query.QueryValue;
import de.mw.mwdata.core.query.SimpleQueryBuilder;
import de.mw.mwdata.core.query.ValueType;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.dao.IOfdbDao;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.ofdb.impl.ConfigOfdb;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;
import de.mw.mwdata.ofdb.impl.OfdbPropMapper;
import de.mw.mwdata.ofdb.impl.OfdbUtils;
import de.mw.mwdata.ofdb.query.IOfdbQueryModelService;
import de.mw.mwdata.ofdb.query.OfdbOrderSet;
import de.mw.mwdata.ofdb.query.OfdbQueryModel;
import de.mw.mwdata.ofdb.query.OfdbWhereRestriction;

public class OfdbQueryModelService implements IOfdbQueryModelService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OfdbQueryModelService.class);

	private OfdbCacheManager ofdbCacheManager;
	private ICrudService<IEntity> crudService;
	private IOfdbDao ofdbDao;

	public void setCrudService(final ICrudService<IEntity> crudService) {
		this.crudService = crudService;
	}

	public void setOfdbDao(final IOfdbDao ofdbDao) {
		this.ofdbDao = ofdbDao;
	}

	public void setOfdbCacheManager(final OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	@Override
	public String buildSQL(final OfdbQueryModel queryModel, final ViewConfigHandle viewHandle, final boolean setCount) {

		QueryBuilder queryBuilder = new SimpleQueryBuilder();
		ITabDef fromTabDef = queryModel.getMainTable();

		if (setCount) {
			queryBuilder.setCount(true);
		} else {
			queryBuilder.selectEntity(OfdbUtils.getSimpleName(fromTabDef), fromTabDef.getAlias());
		}

		// add select-alias-columns
		for (IAnsichtTab viewTab : queryModel.getJoinedTables())
			for (ITabSpeig aliasTabSpeig : queryModel.getAlias(viewTab)) {
				String aliasPropName = this.ofdbCacheManager.mapTabSpeig2Property(aliasTabSpeig);
				queryBuilder.selectAlias(aliasTabSpeig.getTabDef().getAlias(), aliasPropName);
			}

		// build from-part
		queryBuilder.fromEntity(OfdbUtils.getSimpleName(fromTabDef), fromTabDef.getAlias());

		for (IAnsichtTab ansichtTab : queryModel.getJoinedTables()) {
			ViewConfigHandle joinedViewHandle = this.ofdbCacheManager
					.getViewConfig(ansichtTab.getAnsichtDef().getName());

			if (ansichtTab.getJoinTyp().equalsIgnoreCase("x")) {
				continue;
			}

			// join tables
			ITabDef joinTabDef = ansichtTab.getTabDef();
			queryBuilder.joinTable(OfdbUtils.getSimpleName(joinTabDef), joinTabDef.getAlias());

			// FIXME: validation-check that ansichTab.join1spalteakey =
			// ansichtSpalte.spalteakey
			ITabSpeig tabSpeig1 = joinedViewHandle.findTabSpeigByTabAKeyAndSpalteAKey(joinTabDef.getName(),
					ansichtTab.getJoin1SpalteAKey());
			String join1propName = this.ofdbCacheManager.mapTabSpeig2Property(tabSpeig1);

			// FIXME: validation-check that ansichTab.join2spalteakey =
			// ansichtSpalte.spalteakey
			ITabSpeig tabSpeig2 = viewHandle.findTabSpeigByTabAKeyAndSpalteAKey(fromTabDef.getName(),
					ansichtTab.getJoin2SpalteAKey());
			String join2propName = this.ofdbCacheManager.mapTabSpeig2Property(tabSpeig2);

			// add where-restrictions
			queryBuilder.whereJoin(joinTabDef.getAlias(), join1propName, fromTabDef.getAlias(), join2propName);

		}

		// add where-restrictions to queryBuilder
		for (OfdbWhereRestriction whereRes : queryModel.getWhereRestrictions()) {

			ITabSpeig whereTabSpeig = whereRes.getWhereTabSpeig();
			String whereColumnPropName = this.ofdbCacheManager.mapTabSpeig2Property(whereTabSpeig);

			QueryValue wValue = convertValueToQueryValue(whereRes.getWhereValue(), whereTabSpeig);

			queryBuilder.andWhereRestriction(whereRes.getWhereTabDef().getAlias(), whereColumnPropName,
					whereRes.getWhereOperator(), wValue.getData(), wValue.getType());

		}

		// add order-items to queryBuilder
		for (OfdbOrderSet orderSet : queryModel.getOrderSet()) {

			String propName = this.ofdbCacheManager.mapTabSpeig2Property(orderSet.getOrderTabSpeig());
			queryBuilder.orderBy(orderSet.getOrderTabDef().getAlias(), propName, orderSet.getOrderDirection());
		}

		return queryBuilder.buildSQL();

	}

	@Override
	public QueryValue convertValueToQueryValue(final Object value, final ITabSpeig whereTabSpeig) {

		QueryValue wValue = null;

		switch (whereTabSpeig.getDbDatentyp()) {
		case STRING: {
			wValue = new QueryValue(value.toString(), ValueType.STRING);
			break;
		}
		case BOOLEAN: {
			if (value.equals(Boolean.TRUE)) {
				wValue = new QueryValue(Integer.valueOf(Constants.SYS_VAL_TRUE).toString(), ValueType.NUMBER);
				break;
			} else if (value.equals(Boolean.FALSE)) {
				wValue = new QueryValue(Integer.valueOf(Constants.SYS_VAL_FALSE).toString(), ValueType.NUMBER);
				break;
			}
		}
		case ENUM: {

			if (value instanceof IFxEnum) {
				// note: whereRes.getWhereValue() contains the enum-description-value here

				IFxEnum mwEnum = (IFxEnum) value;
				// ... 1. falsch: hier muss VONBIS von whereRes.getWhereValue() gezogen werden,
				// aber wie ?
				// 2. Prüfung in registerOfdb(): wenn DBTYPE ENUM dann prüfung, dass mapping auf
				// IMWEnum geht
				wValue = new QueryValue(mwEnum.getName().toString(), ValueType.STRING);
			} else {
				String msg = LocalizedMessages.getString(ConfigOfdb.BUNDLE_NAME_OFDB, "missingMWEnumMapping",
						whereTabSpeig.getTabDef().getName(), whereTabSpeig.getSpalte());
				throw new OfdbInvalidConfigurationException(msg);
			}
			break;
		}
		default: {
			wValue = new QueryValue(value.toString(), ValueType.NUMBER);
			break;
		}
		}

		return wValue;
	}

	@Override
	public QueryResult executeQueryModel(final OfdbQueryModel queryModel, final ViewConfigHandle viewHandle,
			List<SortKey> sortKeys, final PagingModel pagingModel) {

		String sqlCount = buildSQL(queryModel, viewHandle, true);
		long count = this.crudService.executeCountSql(sqlCount);

		extendQueryModelByOrderSets(queryModel, viewHandle, sortKeys);
		String sql = buildSQL(queryModel, viewHandle, false);

		// FIXME: here return IEntity[] array and introduce QueryService for wrapping
		// array to QueryResult
		QueryResult result = this.crudService.executeSqlPaginated(sql, queryModel.getMetaData(), pagingModel);
		result.setCountWithoutPaging(count);
		pagingModel.setCount(count);

		return result;
	}

	@Override
	public QueryResult executeFilteredQueryModel(final OfdbQueryModel queryModel, final ViewConfigHandle viewHandle,
			List<SortKey> sortKeys, final PagingModel pagingModel,
			final EntityTO<? extends AbstractMWEntity> entityTO) {

		extendQueryModelByFilters(queryModel, viewHandle, entityTO);
		// NOTE: queryModel was extended by filters

		String filteredSqlCount = buildSQL(queryModel, viewHandle, true);

		extendQueryModelByOrderSets(queryModel, viewHandle, sortKeys);
		String sqlFiltered = buildSQL(queryModel, viewHandle, false);

		long count = this.crudService.executeCountSql(filteredSqlCount);
		QueryResult result = this.crudService.executeSqlPaginated(sqlFiltered, queryModel.getMetaData(), pagingModel);
		result.setCountWithoutPaging(count);

		return result;
	}

	private void extendQueryModelByFilters(final OfdbQueryModel queryModel, final ViewConfigHandle viewHandle,
			final EntityTO<? extends AbstractMWEntity> filterEntityTO) {

		queryModel.resetWhereRestrictions();

		List<IAnsichtSpalte> viewColumns = viewHandle.getViewColumns();
		for (IAnsichtSpalte ansichtSpalte : viewColumns) {
			ITabSpeig tabSpeig = viewHandle.findTabSpeigByAnsichtSpalte(ansichtSpalte);

			if (!StringUtils.isEmpty(ansichtSpalte.getVerdeckenDurchTabAKey())
					&& !StringUtils.isEmpty(ansichtSpalte.getVerdeckenDurchSpalteAKey())) {

				ITabSpeig suchTabSpeig = viewHandle.findTabSpeigByTabAKeyAndSpalteAKey(
						ansichtSpalte.getVerdeckenDurchTabAKey(), ansichtSpalte.getVerdeckenDurchSpalteAKey());

				String mappedPropName = this.ofdbCacheManager.mapTabSpeig2Property(suchTabSpeig);

				// FIXME: cleanUp: remove itemKey and from ofdbfield ...
				// String itemKey = OfdbUtils.generateItemKey(suchTabSpeig.getTabDef(),
				// mappedPropName);

				OfdbEntityMapping entityMapping = this.ofdbCacheManager
						.getEntityMapping(tabSpeig.getTabDef().getName());
				OfdbPropMapper propMapping = entityMapping.findPropertyMapperByTabProp(tabSpeig);
				JoinedPropertyTO joinedProp = findJoinedProperty(queryModel, propMapping.getAssociatedEntityName(),
						mappedPropName);

				String whereValue = filterEntityTO.getJoinedValue(joinedProp.getResultArrayIndex());
				boolean toFilter = (!StringUtils.isBlank(whereValue));

				if (toFilter) {

					// FIXME: queryModel from viewHandle should not be changed !!!
					queryModel.addWhereRestriction(suchTabSpeig.getTabDef(), suchTabSpeig, OperatorEnum.Like,
							whereValue);
				}

			} else {

				boolean toFilter = false;

				Object entityValue = null;
				try {
					OfdbPropMapper propMapper = viewHandle.findPropertyMapperByTabProp(tabSpeig);

					if (null == propMapper) {
						// FIXME: should not be happen. Tritt auf bei Namensspalten, die langfristg
						// durch Id /
						// verdeckenDurch ersetzt werden sollten (z.B. TabDef.bereich)
						throw new OfdbMissingMappingException("Given TabSpeig not mapped by property.");
					}
					entityValue = this.ofdbDao.getEntityValue(filterEntityTO.getItem(), propMapper.getPropertyIndex());

					toFilter = (null != entityValue && !StringUtils.isBlank(entityValue.toString()));
				} catch (OfdbMissingMappingException e) {
					LOGGER.warn(
							"Property not mapped: Table " + tabSpeig.getName() + ", column " + tabSpeig.getSpalte());

				}
				if (toFilter) {
					queryModel.addWhereRestriction(tabSpeig.getTabDef(), tabSpeig, OperatorEnum.Like, entityValue);
				}

			}

		}

	}

	private JoinedPropertyTO findJoinedProperty(OfdbQueryModel queryModel, String associatedEntityName,
			String mappedPropName) {

		for (OfdbField field : queryModel.getMetaData()) {
			if (!field.hasJoinedProperty()) {
				continue;
			}

			if (field.getJoinedProperty().getEntityName().equals(associatedEntityName)
					&& field.getJoinedProperty().getPropName().equals(mappedPropName)) {
				return field.getJoinedProperty();
			}

		}

		return null;
	}

	private void extendQueryModelByOrderSets(final OfdbQueryModel queryModel, final ViewConfigHandle viewHandle,
			final List<SortKey> sortKeys) {

		queryModel.resetOrderSet();

		List<IAnsichtSpalte> viewColumns = viewHandle.getViewColumns();
		for (IAnsichtSpalte ansichtSpalte : viewColumns) {
			ITabSpeig tabSpeig = viewHandle.findTabSpeigByAnsichtSpalte(ansichtSpalte);

			OfdbEntityMapping entityMapping = ofdbCacheManager.getEntityMapping(tabSpeig.getTabDef().getName());
			if (!entityMapping.hasMapping(tabSpeig)) {
				continue;
			}

			if (!StringUtils.isEmpty(ansichtSpalte.getVerdeckenDurchTabAKey())
					&& !StringUtils.isEmpty(ansichtSpalte.getVerdeckenDurchSpalteAKey())) {

				String mappedPropertyName = this.ofdbCacheManager.mapTabSpeig2Property(tabSpeig);
				SortKey sortKey = OfdbUtils.findSortKeyByColumnName(sortKeys, mappedPropertyName);
				boolean toSort = (null != sortKey);

				if (toSort) {

					ITabSpeig suchTabSpeig = viewHandle.findTabSpeigByTabAKeyAndSpalteAKey(
							ansichtSpalte.getVerdeckenDurchTabAKey(), ansichtSpalte.getVerdeckenDurchSpalteAKey());
					queryModel.addOrderSet(suchTabSpeig.getTabDef(), suchTabSpeig,
							sortKey.getSortDirection().getName());

				}

			} else {

				String mappedPropertyName = this.ofdbCacheManager.mapTabSpeig2Property(tabSpeig);
				SortKey sortKey = OfdbUtils.findSortKeyByColumnName(sortKeys, mappedPropertyName);
				boolean toSort = (null != sortKey);
				if (toSort) {
					queryModel.addOrderSet(tabSpeig.getTabDef(), tabSpeig, sortKey.getSortDirection().getName());
				}

			}

		} // end for ofFields

	}

}
