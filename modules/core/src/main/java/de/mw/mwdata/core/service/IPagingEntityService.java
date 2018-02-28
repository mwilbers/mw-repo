package de.mw.mwdata.core.service;

import java.util.List;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.core.utils.SortKey;

public interface IPagingEntityService extends IEntityService<IEntity> {

	public PaginatedList<IEntity[]> findByCriteriaPaginated(final String viewName,
			final EntityTO<? extends AbstractMWEntity> entityTO, final int pageIndex, final List<SortKey>... sortKeys);

	public PaginatedList<IEntity[]> loadViewPaginated(final String viewName, final int pageIndex,
			final List<SortKey>... sortKeys);

}
