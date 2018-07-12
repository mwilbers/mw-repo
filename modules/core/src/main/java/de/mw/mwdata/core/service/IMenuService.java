package de.mw.mwdata.core.service;

import java.util.List;

import de.mw.mwdata.core.domain.EntityTO;

public interface IMenuService {

	public List<EntityTO> findMainMenus(final String userAreaName);

	public List<EntityTO> findChildMenus(final int parentMenuId, final String userAreaName);

}
