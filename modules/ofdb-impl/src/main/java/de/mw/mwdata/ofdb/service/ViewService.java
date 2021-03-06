package de.mw.mwdata.ofdb.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.query.QueryBuilder;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.query.SimpleQueryBuilder;
import de.mw.mwdata.core.query.ValueType;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.core.utils.SortKey.SORTDIRECTION;
import de.mw.mwdata.core.utils.Utils;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.UserView;
import de.mw.mwdata.ofdb.domain.impl.UserViewColumn;
import de.mw.mwdata.ofdb.query.IOfdbQueryModelService;
import de.mw.mwdata.ofdb.query.OfdbQueryModel;

public class ViewService implements IViewService<IEntity> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewService.class);

	private OfdbCacheManager ofdbCacheManager;
	private IOfdbService ofdbService;
	private ICrudService<IEntity> crudService;
	private IOfdbQueryModelService ofdbQueryModelService;

	public void setOfdbService(final IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	public void setCrudService(ICrudService<IEntity> crudService) {
		this.crudService = crudService;
	}

	public void setOfdbQueryModelService(IOfdbQueryModelService ofdbQueryModelService) {
		this.ofdbQueryModelService = ofdbQueryModelService;
	}

	protected IOfdbService getOfdbService() {
		return this.ofdbService;
	}

	protected ICrudService<IEntity> getCrudService() {
		return this.crudService;
	}

	public void setOfdbCacheManager(final OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	protected List<SortKey> prepareSortColumns(final String viewName, final List<SortKey>... sortKeys) {
		List<SortKey> cols = new ArrayList<SortKey>();

		if (ArrayUtils.isEmpty(sortKeys)) {

			ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(viewName);
			List<IAnsichtOrderBy> ansichtOrderList = viewHandle.getViewOrders();

			for (IAnsichtOrderBy orderBy : ansichtOrderList) {

				ITabSpeig tabSpeig = viewHandle.findTabSpeigByAnsichtOrderBy(orderBy);
				String propName = this.ofdbCacheManager.mapTabSpeig2Property(tabSpeig);

				SORTDIRECTION dir = (orderBy.getAufsteigend() ? SORTDIRECTION.ASC : SORTDIRECTION.DESC);
				cols.add(new SortKey(propName, dir.getName()));
			}

		} else {
			cols = sortKeys[0];
		}

		return cols;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public QueryResult executePaginatedViewQuery(final String viewName,
			final EntityTO<? extends AbstractMWEntity> entityTO, final PagingModel pagingModel,
			final List<SortKey>... sortKeys) {

		List<SortKey> cols = prepareSortColumns(viewName, sortKeys);

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(viewName);
		OfdbQueryModel queryModel = viewHandle.getQueryModel();
		QueryResult queryResult = this.ofdbQueryModelService.executeFilteredQueryModel(queryModel, viewHandle, cols,
				pagingModel, entityTO);

		return queryResult;
	}

	@Override
	public QueryResult executeViewQuery(final String viewName, final PagingModel pagingModel,
			final List<SortKey>... sortKeys) {

		List<SortKey> cols = prepareSortColumns(viewName, sortKeys);

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(viewName);
		OfdbQueryModel queryModel = viewHandle.getQueryModel();
		queryModel.resetWhereRestrictions();

		// FIXME: hast to be adjusted for datasets greater than pageSize and paging ...
		return this.ofdbQueryModelService.executeQueryModel(queryModel, viewHandle, cols, pagingModel);

		// #ViewLayout# here re-order layout-columns by user specific preferences: maybe
		// new userService.loadViewLayout(viewName, userId)

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public UserView createUserView(UserView userView, List<UserViewColumn> userViewColumns) {
		userView = (UserView) this.crudService.insert(userView);
		for (UserViewColumn col : userViewColumns) {
			this.crudService.insert(col);
		}

		return userView;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<UserView> listAllUserViews(final long userId) {

		QueryBuilder builder = new SimpleQueryBuilder();
		String sql = builder.selectEntity(UserView.class.getSimpleName(), "uView")
				.fromEntity(UserView.class.getSimpleName(), "uView")
				.andWhereRestriction("uView", "userId", OperatorEnum.Eq, Long.valueOf(userId), ValueType.NUMBER)
				.buildSQL();

		QueryResult result = this.crudService.executeSql(sql);

		List<IEntity[]> rows = result.getRows();
		List<UserView> results = (List<UserView>) Utils.entityArrayToEntity(rows);
		return results;
	}

}
