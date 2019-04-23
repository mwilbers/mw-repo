package de.mw.mwdata.ofdb.service;

import java.util.List;

import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.ofdb.domain.impl.UserView;
import de.mw.mwdata.ofdb.domain.impl.UserViewColumn;

public interface IViewService<IEntity> {

	public QueryResult executePaginatedViewQuery(final String viewName,
			final EntityTO<? extends AbstractMWEntity> entityTO, final PagingModel pagingModel,
			final List<SortKey>... sortKeys);

	public QueryResult executeViewQuery(final String viewName, final PagingModel pagingModel,
			final List<SortKey>... sortKeys);

	public UserView createUserView(final UserView userView, final List<UserViewColumn> userViewColumns);

	public List<UserView> listAllUserViews(final long userId);

}
