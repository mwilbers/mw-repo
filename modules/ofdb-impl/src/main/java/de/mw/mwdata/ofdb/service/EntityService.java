package de.mw.mwdata.ofdb.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.service.IEntityService;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.core.utils.SortKey.SORTDIRECTION;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.dao.IOfdbDao;
import de.mw.mwdata.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.ofdb.domain.ITabSpeig;

public class EntityService implements IEntityService<IEntity> {

	private OfdbCacheManager ofdbCacheManager;

	private IOfdbService ofdbService;

	private IOfdbDao ofdbDao;

	public void setOfdbService(final IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	protected IOfdbService getOfdbService() {
		return this.ofdbService;
	}

	public void setOfdbCacheManager(final OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	protected IOfdbDao getOfdbDao() {
		return this.ofdbDao;
	}

	public void setOfdbDao(final IOfdbDao ofdbDao) {
		this.ofdbDao = ofdbDao;
	}

	@Override
	public List<IEntity[]> loadView(final String viewName, final List<SortKey>... sortKeys) {

		List<SortKey> cols = prepareSortColumns(viewName, sortKeys);
		String sql = this.ofdbService.buildSQL(viewName, cols);
		List<IEntity[]> result = this.ofdbService.executeQuery(sql);

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

	// @Override
	protected long executeCountQuery(final String sqlCount) {
		return this.getOfdbDao().executeCountQuery(sqlCount);
	}

}
