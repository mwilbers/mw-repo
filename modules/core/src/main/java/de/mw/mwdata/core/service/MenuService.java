package de.mw.mwdata.core.service;

import java.util.List;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.daos.IOfdbDao;
import de.mw.mwdata.core.ofdb.def.ConfigOfdb;
import de.mw.mwdata.core.ofdb.query.DefaultOfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.OfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.ValueType;
import de.mw.mwdata.ordb.query.OperatorEnum;

public class MenuService implements IMenuService {

	private IOfdbDao ofdbDao;

	public void setOfdbDao( final IOfdbDao ofdbDao ) {
		this.ofdbDao = ofdbDao;
	}

	@Override
	public List<IEntity[]> findMainMenus() {

		OfdbQueryBuilder builder = new DefaultOfdbQueryBuilder();
		int layer = 0;
		String sql = builder.selectTable( ConfigOfdb.T_MENU, "aMenu" ).fromTable( ConfigOfdb.T_MENU, "aMenu" )
				.andWhereRestriction( "aMenu", "ebene", OperatorEnum.Eq, String.valueOf( layer ), ValueType.NUMBER )
				.orderBy( "aMenu", "anzeigeName", "asc" ).buildSQL();
		return this.ofdbDao.executeQuery( sql );

	}

	@Override
	public List<IEntity[]> findChildMenus( final int parentMenuId ) {

		OfdbQueryBuilder builder = new DefaultOfdbQueryBuilder();
		String sql = builder.selectTable( ConfigOfdb.T_MENU, "aMenu" ).fromTable( ConfigOfdb.T_MENU, "aMenu" )
				.andWhereRestriction( "aMenu", "hauptMenueId", OperatorEnum.Eq, String.valueOf( parentMenuId ),
						ValueType.NUMBER )
				.orderBy( "aMenu", "anzeigeName", "asc" ).buildSQL();

		return this.ofdbDao.executeQuery( sql );
	}

}
