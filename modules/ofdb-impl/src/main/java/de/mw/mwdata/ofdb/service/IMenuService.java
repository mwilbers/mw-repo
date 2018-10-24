package de.mw.mwdata.ofdb.service;

import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.ofdb.domain.IMenue;

public interface IMenuService<IEntity> {

	public IMenue findMenuById(final long menuId);

	public QueryResult findMainMenus(final String userAreaName);

	public QueryResult findChildMenus(final IMenue menu, final String userAreaName);

	public IEntity findMenuByUrlPath(final String urlPath);

}
