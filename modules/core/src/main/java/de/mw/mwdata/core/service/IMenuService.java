package de.mw.mwdata.core.service;

import java.util.List;
import de.mw.mwdata.core.domain.IEntity;

public interface IMenuService {

	public List<IEntity[]> findMainMenus();

	public List<IEntity[]> findChildMenus( final int parentMenuId );

}
