package de.mw.mwdata.core.service;

import java.util.List;
import de.mw.mwdata.core.ofdb.SortKey;

public interface IEntityService<IEntity> {

	/**
	 * Loads all entities given by the viewName. Loads them sorted if sortColumns are given.
	 *
	 * @param viewName
	 * @param sortColumns
	 * @return
	 */
	public List<IEntity[]> loadView( final String viewName, final List<SortKey>... sortKeys );

}
