package de.mw.mwdata.core.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.ObjectUtils;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
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
	public List<EntityTO> findMainMenus() {

		// für die RESTbased urls auf die entities:
		// select m.* , a.*
		// from FX_Menues_K m
		// left join FX_AnsichtDef_K a on (a.dsid = m.ansichtdefid)
		// where 1=1
		// and m.typ = 'ANSICHT'
		// and m.anzeigename = 'Ansichten'
		// ;

		// List<EntityTO> entityTOs = new ArrayList<>();
		OfdbQueryBuilder builder = new DefaultOfdbQueryBuilder();
		int layer = 0;
		String sql = builder.selectTable( ConfigOfdb.T_MENU, "aMenu" ).selectAlias( "aView", "urlPath" )
				.fromTable( ConfigOfdb.T_MENU, "aMenu" ).leftJoinTable( "aMenu", "ansichtDef", "aView" )
				.andWhereRestriction( "aMenu", "ebene", OperatorEnum.Eq, String.valueOf( layer ), ValueType.NUMBER )
				.orderBy( "aMenu", "anzeigeName", "asc" ).buildSQL();

		List<IEntity[]> entities = this.ofdbDao.executeQuery( sql );
		List<EntityTO> entityTOs = convertToEntityTO( entities, new String( "urlPath" ) );

		// for ( IEntity[] item : entities ) {
		// EntityTO entityTO = convertToEntityTO( item, new String( "urlPath" ) );
		// entityTOs.add( entityTO );
		// }

		return entityTOs;
	}

	private List<EntityTO> convertToEntityTO( final List<IEntity[]> entities, final String... joinedKeys ) {

		List<EntityTO> entityTOs = new ArrayList<>();

		for ( Object[] item : entities ) {
			if ( ObjectUtils.isEmpty( item[0] ) ) {
				throw new IllegalStateException( "Entity must not be empty in ofdb based result array IEntity[]." );
			}
			EntityTO entityTO = new EntityTO<AbstractMWEntity>( (AbstractMWEntity) item[0] );

			if ( !ArrayUtils.isEmpty( joinedKeys ) ) {
				if ( joinedKeys.length != item.length - 1 ) {
					throw new IllegalStateException(
							"Number of joined keys does not match number of retrieved values." );
				}
				for ( int i = 0; i < joinedKeys.length; i++ ) {
					entityTO.addJoinedValue( joinedKeys[i], (String) item[i + 1] );
				}
			}

			entityTOs.add( entityTO );
		}

		return entityTOs;
	}

	@Override
	public List<EntityTO> findChildMenus( final int parentMenuId ) {

		OfdbQueryBuilder builder = new DefaultOfdbQueryBuilder();
		String sql = builder.selectTable( ConfigOfdb.T_MENU, "aMenu" ).selectAlias( "aView", "urlPath" )
				.fromTable( ConfigOfdb.T_MENU, "aMenu" ).leftJoinTable( "aMenu", "ansichtDef", "aView" )
				.andWhereRestriction( "aMenu", "hauptMenueId", OperatorEnum.Eq, String.valueOf( parentMenuId ),
						ValueType.NUMBER )
				.orderBy( "aMenu", "anzeigeName", "asc" ).buildSQL();

		List<IEntity[]> entities = this.ofdbDao.executeQuery( sql );
		List<EntityTO> entityTOs = convertToEntityTO( entities, new String( "urlPath" ) );

		return entityTOs;
	}

}
