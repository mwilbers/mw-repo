package de.mw.mwdata.core.service;

import de.mw.mwdata.core.query.QueryResult;

public interface IMenuService<IEntity> {

	public QueryResult findMainMenus(final String userAreaName);

	public QueryResult findChildMenus(final long parentMenuId, final String userAreaName);

	public IEntity findMenuByUrlPath(final String urlPath);

	public IEntity findParentMenu(final long mainMenuId);

}
