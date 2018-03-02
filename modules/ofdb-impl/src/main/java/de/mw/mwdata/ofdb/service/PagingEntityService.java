package de.mw.mwdata.ofdb.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.service.IPagingEntityService;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.core.utils.Utils;

public class PagingEntityService extends EntityService implements IPagingEntityService {

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public PaginatedList<IEntity[]> findByCriteriaPaginated(final String viewName,
			final EntityTO<? extends AbstractMWEntity> entityTO, final int pageIndex, final List<SortKey>... sortKeys) {

		List<SortKey> cols = prepareSortColumns(viewName, sortKeys);
		String sql = this.getOfdbService().buildFilteredSQL(viewName, entityTO, cols);

		String sqlCount = getOfdbService().buildSQLCount(viewName);
		List<IEntity[]> resultList = getCrudService().executeSqlPaginated(sql, pageIndex);
		List<IEntity[]> objectArray = Utils.toObjectArray(resultList);

		long count = this.getCrudService().executeCountSql(sqlCount);

		PaginatedList<IEntity[]> pagingList = new PaginatedList<IEntity[]>(objectArray, count, pageIndex);

		return pagingList;

	}

	@Override
	public PaginatedList<IEntity[]> loadViewPaginated(final String viewName, final int pageIndex,
			final List<SortKey>... sortKeys) {

		List<SortKey> cols = prepareSortColumns(viewName, sortKeys);
		String sql = getOfdbService().buildSQL(viewName, cols);

		String sqlCount = getOfdbService().buildSQLCount(viewName);
		List<IEntity[]> resultList = getCrudService().executeSqlPaginated(sql, pageIndex);
		List<IEntity[]> objectArray = Utils.toObjectArray(resultList);

		long count = getCrudService().executeCountSql(sqlCount);

		PaginatedList<IEntity[]> pagingList = new PaginatedList<IEntity[]>(objectArray, count, pageIndex);

		return pagingList;

	}

}
