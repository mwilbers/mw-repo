package de.mw.mwdata.ofdb.service;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.query.MetaDataQueryBuilder;
import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.query.ValueType;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.service.IMenuService;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.impl.Menue;
import de.mw.mwdata.ofdb.impl.ConfigOfdb;
import de.mw.mwdata.ofdb.query.impl.OfdbMetaDataQueryBuilder;

// FIXME: not necessary named OfdbMenuService in ofdb-impl, better DefaultMenuService in core
public class OfdbMenuService implements IMenuService {

	private ICrudService<Menue> crudService;
	private OfdbCacheManager ofdbCacheManager;
	private IOfdbService ofdbService;

	public void setOfdbCacheManager(OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	public void setCrudService(ICrudService<Menue> crudService) {
		this.crudService = crudService;
	}

	public void setOfdbService(final IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	@Override
	public QueryResult findMainMenus(final String userAreaName) {

		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);
		MetaDataQueryBuilder builder = new OfdbMetaDataQueryBuilder(viewHandle, this.ofdbService);
		// int layer = 0;

		// FIXME: idea: instead of tabSpeigs here create ReadOnlyOfdbField inherited
		// from IOfdbField (or IFieldMetaData)
		String sql = builder.selectEntity(ConfigOfdb.T_MENU, "aMenu").selectAlias("aView", "urlPath")
				.fromEntity(ConfigOfdb.T_MENU, "aMenu").leftJoinTable("aMenu", "ansichtDef", "aView")
				.joinEntity("bereich", "bBereich")
				.andWhereRestriction("aMenu", "ebene", OperatorEnum.Eq, String.valueOf(0), ValueType.NUMBER)
				.andWhereRestriction("bBereich", "name", OperatorEnum.Eq, userAreaName, ValueType.STRING)
				.orderBy("aMenu", "anzeigeName", "asc").buildSQL();

		return this.crudService.executeSql(sql, builder.buildMetaData());
		// return new QueryResult(builder.buildMetaData(), entities);

		// FIXME: JoinedPropertyTO must be given by queryBuilder ...
		// List<EntityTO> entityTOs = convertToEntityTOList(result.getRows(),
		// new JoinedPropertyTO("ansichtDef", "urlPath", 1));
		//
		// return entityTOs;
	}

	// // FIXME: move following methods to utils ?
	// private List<EntityTO> convertToEntityTOList(final List<IEntity[]> entities,
	// final JoinedPropertyTO... joinedProperties) {
	//
	// List<EntityTO> entityTOs = new ArrayList<>();
	//
	// for (Object[] item : entities) {
	// if (ObjectUtils.isEmpty(item[0])) {
	// throw new IllegalStateException("Entity must not be empty in ofdb based
	// result array IEntity[].");
	// }
	// EntityTO entityTO = convertToEntityTO(item, joinedProperties);
	//
	// entityTOs.add(entityTO);
	// }
	//
	// return entityTOs;
	// }

	// private EntityTO convertToEntityTO(Object[] item, final JoinedPropertyTO...
	// joinedProperties) {
	// EntityTO entityTO = new EntityTO<AbstractMWEntity>((AbstractMWEntity)
	// item[0]);
	//
	// // FIXME: write comfort method for converting IEntity to EntityTO
	//
	// if (!ArrayUtils.isEmpty(joinedProperties)) {
	// if (joinedProperties.length != item.length - 1) {
	// throw new IllegalStateException(
	// "Number of joined properties does not match number of retrieved values.");
	// }
	// for (int i = 0; i < joinedProperties.length; i++) {
	// entityTO.addJoinedValue((String) item[i + 1]);
	// }
	// }
	// return entityTO;
	// }

	@Override
	public QueryResult findChildMenus(final long parentMenuId, final String userAreaName) {

		// FIXME: Regel: wenn ofdbFields vorhanden, kann erst entschieden werden, ob
		// Feld
		// editierbar oder nicht. Ansonsten readonly
		// QueryBuilder in core kann pragmatisch ohne ofdbFields benutzt werden, um
		// daten z.B. intern zu verarbeiten.
		// Falls ofdb-gesteuerte Anzeige stattfinden soll, muss man dem queryBilder die
		// fields manuell hinzufügen.
		// Dazu evt. methode createOfdbFields ändern nach createSingleOfdbField()
		// und OfdbMenueService muss von spring-abstract ofdbCacheAware mit
		// ofdbCacheManager ableiten
		// TODO: returntype not EntityTO but IEntity[] / QueryResult

		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);
		MetaDataQueryBuilder builder = new OfdbMetaDataQueryBuilder(viewHandle, this.ofdbService);
		// this.viewHandle););

		String sql = builder.selectEntity(ConfigOfdb.T_MENU, "aMenu").selectAlias("aView", "urlPath")
				.fromEntity(ConfigOfdb.T_MENU, "aMenu").leftJoinTable("aMenu", "ansichtDef", "aView")
				.joinEntity("bereich", "bBereich")
				.andWhereRestriction("aMenu", "hauptMenueId", OperatorEnum.Eq, String.valueOf(parentMenuId),
						ValueType.NUMBER)
				.andWhereRestriction("bBereich", "name", OperatorEnum.Eq, userAreaName, ValueType.STRING)
				.orderBy("aMenu", "anzeigeName", "asc").buildSQL();

		// FIXME: return QueryResult here and do not convert here to EntityTO, but in PL
		// controller
		return this.crudService.executeSql(sql, builder.buildMetaData());
		// return new QueryResult(builder.buildMetaData(), entities);
		// return this.crudService.executeSql(sql);
		// List<EntityTO> entityTOs = convertToEntityTOList(result.getRows(),
		// new JoinedPropertyTO("ansichtDef", "urlPath", 1));
		//
		// return entityTOs;
	}

	@Override
	public IEntity findMenuByUrlPath(String urlPath) {

		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);
		MetaDataQueryBuilder builder = new OfdbMetaDataQueryBuilder(viewHandle, this.ofdbService);

		String sql = builder.selectEntity(ConfigOfdb.T_MENU, "aMenu").fromEntity(ConfigOfdb.T_MENU, "aMenu")
				.leftJoinTable("aMenu", "ansichtDef", "aView")
				.andWhereRestriction("aView", "urlPath", OperatorEnum.Eq, urlPath, ValueType.STRING).buildSQL();

		QueryResult result = this.crudService.executeSql(sql, builder.buildMetaData());
		// return new QueryResult(builder.buildMetaData(), entities);

		// List<IEntity[]> entities = this.crudService.executeSql(sql);
		if (result.isEmpty()) {
			return null;
			// return EntityTO.createEmptyEntityTO();
		}

		return result.getEntityByRowIndex(0);
		// return result.getEntityByRowIndex(0);
		// return convertToEntityTO(result.getRows().get(0));
	}

	@Override
	public IEntity findParentMenu(long mainMenuId) {
		Menue menu = this.crudService.findById(Menue.class, mainMenuId);

		return menu;
		// return convertToEntityTO(new Object[] { menu });
	}

}
