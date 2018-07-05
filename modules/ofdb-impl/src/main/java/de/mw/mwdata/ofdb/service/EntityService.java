package de.mw.mwdata.ofdb.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.service.IEntityService;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.core.utils.SortKey.SORTDIRECTION;
import de.mw.mwdata.core.utils.Utils;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.ITabSpeig;

public class EntityService implements IEntityService<IEntity> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityService.class);

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

	@Override
	public List<IEntity[]> loadView(final String viewName, final List<SortKey>... sortKeys) {

		List<SortKey> cols = prepareSortColumns(viewName, sortKeys);
		String sql = this.ofdbService.buildSQL(viewName, cols);
		List<IEntity[]> result = this.crudService.executeSql(sql);

		return result;

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
	public PaginatedList<IEntity[]> findByCriteriaPaginated(final String viewName,
			final EntityTO<? extends AbstractMWEntity> entityTO, final int pageIndex, final List<SortKey>... sortKeys) {

		List<SortKey> cols = prepareSortColumns(viewName, sortKeys);
		String sql = this.getOfdbService().buildFilteredSQL(viewName, entityTO, cols);

		int pageSize = loadPageSize();
		String sqlCount = getOfdbService().buildSQLCount(viewName);
		List<IEntity[]> resultList = getCrudService().executeSqlPaginated(sql, pageIndex, pageSize);
		List<IEntity[]> objectArray = Utils.toObjectArray(resultList);

		long count = this.getCrudService().executeCountSql(sqlCount);

		PaginatedList<IEntity[]> pagingList = new PaginatedList<IEntity[]>(objectArray, count, pageIndex);

		return pagingList;

	}

	private int loadPageSize() {

		String pageSizeString = this.applicationConfigService.getPropertyValue("app.hibernate.pageSizeForLoad");

		int pageSize = 0;
		try {
			pageSize = Integer.parseInt(pageSizeString);
		} catch (NumberFormatException e) {
			LOGGER.warn(
					"Configuration of application property 'app.hibernate.pageSizeForLoad' is not valid integer. Will use default page size.");
		}

		return pageSize;
	}

	@Override
	public PaginatedList<IEntity[]> loadViewPaginated(final String viewName, final int pageIndex,
			final List<SortKey>... sortKeys) {

		List<SortKey> cols = prepareSortColumns(viewName, sortKeys);

		String sqlCount = getOfdbService().buildSQLCount(viewName);
		long count = getCrudService().executeCountSql(sqlCount);

		// FIXME: hast to be adjusted for datasets greater than pageSize and paging ...

		int pageSize = loadPageSize();
		String sql = getOfdbService().buildSQL(viewName, cols);
		List<IEntity[]> resultList = getCrudService().executeSqlPaginated(sql, pageIndex, pageSize);
		List<IEntity[]> objectArray = Utils.toObjectArray(resultList);

		PaginatedList<IEntity[]> pagingList = new PaginatedList<IEntity[]>(objectArray, count, pageIndex);

		return pagingList;

	}

}
