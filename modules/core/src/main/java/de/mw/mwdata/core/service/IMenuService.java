package de.mw.mwdata.core.service;

import java.util.List;
import de.mw.mwdata.core.domain.EntityTO;

public interface IMenuService {

	public List<EntityTO> findMainMenus();

	public List<EntityTO> findChildMenus( final int parentMenuId );

}
