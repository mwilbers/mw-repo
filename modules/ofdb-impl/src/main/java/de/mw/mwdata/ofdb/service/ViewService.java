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
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.service.IViewService;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.core.utils.SortKey.SORTDIRECTION;
import de.mw.mwdata.core.utils.Utils;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.ITabSpeig;

public class ViewService implements IViewService<IEntity> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewService.class);

	private OfdbCacheManager ofdbCacheManager;

	private IOfdbService ofdbService;

	private ICrudService<IEntity> crudService;

	private ApplicationConfigService applicationConfigService;

	public void setOfdbService(final IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	public void setCrudService(ICrudService<IEntity> crudService) {
		this.crudService = crudService;
	}

	public void setApplicationConfigService(ApplicationConfigService applicationConfigService) {
		this.applicationConfigService = applicationConfigService;
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
				String propName = this.ofdbService.mapTabSpeig2Property(tabSpeig);

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
	public PaginatedList<IEntity[]> executePaginatedViewQuery(final String viewName,
			final EntityTO<? extends AbstractMWEntity> entityTO, final PagingModel pagingModel,
			final List<SortKey>... sortKeys) {

		List<SortKey> cols = prepareSortColumns(viewName, sortKeys);

		QueryResult queryResult = this.getOfdbService().executeFilteredQueryModel(viewName, cols, pagingModel,
				entityTO);
		List<IEntity[]> objectArray = Utils.toObjectArray(queryResult.getRows());
		PaginatedList<IEntity[]> pagingList = new PaginatedList<IEntity[]>(objectArray,
				queryResult.getCountWithoutPaging(), pagingModel);

		// FIXME: better return QueryResult instead of PaginatedList<IEntity[]>
		return pagingList;

	}

	@Override
	public QueryResult executeViewQuery(final String viewName, final PagingModel pagingModel,
			final List<SortKey>... sortKeys) {

		List<SortKey> cols = prepareSortColumns(viewName, sortKeys);
		// FIXME: hast to be adjusted for datasets greater than pageSize and paging ...
		QueryResult queryResult = getOfdbService().executeQueryModel(viewName, cols, pagingModel);

		return queryResult;

	}

}
