package de.mw.mwdata.core.service;

import java.util.List;

import de.mw.mwdata.core.domain.EntityTO;

public interface IMenuService<IEntity> {

	public List<EntityTO<?>> findMainMenus(final String userAreaName);

	public List<EntityTO<?>> findChildMenus(final long parentMenuId, final String userAreaName);

	public EntityTO<?> findMenuByUrlPath(final String urlPath);

	public EntityTO<?> findParentMenu(final long mainMenuId);

}
