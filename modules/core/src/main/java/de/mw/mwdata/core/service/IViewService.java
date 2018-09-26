package de.mw.mwdata.core.service;

import java.util.List;

import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.core.utils.SortKey;

public interface IViewService<IEntity> {

	// /**
	// * Loads all entities given by the viewName. Loads them sorted if sortColumns
	// * are given.
	// *
	// * @param viewName
	// * @param sortColumns
	// * @return
	// */
	// public List<IEntity[]> loadView(final String viewName, final List<SortKey>...
	// sortKeys);

	public PaginatedList<IEntity[]> executePaginatedViewQuery(final String viewName,
			final EntityTO<? extends AbstractMWEntity> entityTO, final PagingModel pagingModel,
			final List<SortKey>... sortKeys);

	public QueryResult executeViewQuery(final String viewName, final PagingModel pagingModel,
			final List<SortKey>... sortKeys);

}
