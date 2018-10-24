package de.mw.mwdata.ofdb.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.query.MetaDataQueryBuilder;
import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.query.ValueType;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IMenue;
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
	@Transactional(propagation = Propagation.REQUIRED)
	public QueryResult findMainMenus(final String userAreaName) {

		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);
		if (null == viewHandle) {
			return QueryResult.createEmptyQueryResult();
		}

		MetaDataQueryBuilder builder = new OfdbMetaDataQueryBuilder(viewHandle, this.ofdbService);

		// FIXME: idea: instead of tabSpeigs here create ReadOnlyOfdbField inherited
		// from IOfdbField (or IFieldMetaData)
		String sql = builder.selectEntity(ConfigOfdb.T_MENU, "aMenu").selectAlias("aView", "urlPath")
				.fromEntity(ConfigOfdb.T_MENU, "aMenu").leftJoinTable("aMenu", "ansichtDef", "aView")
				.joinEntity("bereich", "bBereich")
				.andWhereRestriction("aMenu", "ebene", OperatorEnum.Eq, String.valueOf(0), ValueType.NUMBER)
				.andWhereRestriction("bBereich", "name", OperatorEnum.Eq, userAreaName, ValueType.STRING)
				.orderBy("aMenu", "anzeigeName", "asc").buildSQL();

		return this.crudService.executeSql(sql, builder.buildMetaData());
	}

	@Override
	public IMenue findMenuById(long menuId) {
		return this.crudService.findById(Menue.class, menuId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public QueryResult findChildMenus(final IMenue menu, final String userAreaName) {

		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);
		MetaDataQueryBuilder builder = new OfdbMetaDataQueryBuilder(viewHandle, this.ofdbService);

		Menue mainMenu = (Menue) menu; // this.crudService.findById(Menue.class, parentMenuId);
		// List<Menue> subMenus = mainMenu.getUnterMenues();
		//
		// List<IEntity[]> rows = new ArrayList<IEntity[]>(subMenus.size());
		//
		// for (int i = 0; i < subMenus.size(); i++) {
		// IEntity[] e = (IEntity[]) new Object[] { subMenus.get(i),
		// subMenus.get(i).getAnsichtDef().getUrlPath() };
		// rows.add(e);
		// }
		// return new QueryResult(builder.buildMetaData(), rows);

		// String sql = builder.selectEntity(ConfigOfdb.T_MENU,
		// "aMenu").selectAlias("aView", "urlPath")
		// .fromEntity(ConfigOfdb.T_MENU, "aMenu").andWhereRestriction("aMenu",
		// "hauptMenueId", OperatorEnum.Eq,
		// String.valueOf(mainMenu.getId()), ValueType.NUMBER)
		// .orderBy("aMenu", "anzeigeName", "asc").buildSQL();

		String sql = builder.selectEntity(ConfigOfdb.T_MENU, "aMenu").selectAlias("aView", "urlPath")
				.fromEntity(ConfigOfdb.T_MENU, "aMenu").leftJoinTable("aMenu", "ansichtDef", "aView")
				.joinEntity("bereich", "bBereich")
				.andWhereRestriction("aMenu", "hauptMenueId", OperatorEnum.Eq, String.valueOf(mainMenu.getId()),
						ValueType.NUMBER)
				.andWhereRestriction("bBereich", "name", OperatorEnum.Eq, userAreaName, ValueType.STRING)
				.orderBy("aMenu", "anzeigeName", "asc").buildSQL();

		return this.crudService.executeSql(sql, builder.buildMetaData());
	}

	@Override
	public IEntity findMenuByUrlPath(String urlPath) {

		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);
		if (null == viewHandle) {
			return null;
		}

		MetaDataQueryBuilder builder = new OfdbMetaDataQueryBuilder(viewHandle, this.ofdbService);

		String sql = builder.selectEntity(ConfigOfdb.T_MENU, "aMenu").fromEntity(ConfigOfdb.T_MENU, "aMenu")
				.leftJoinTable("aMenu", "ansichtDef", "aView")
				.andWhereRestriction("aView", "urlPath", OperatorEnum.Eq, urlPath, ValueType.STRING).buildSQL();

		QueryResult result = this.crudService.executeSql(sql, builder.buildMetaData());

		if (result.isEmpty()) {
			return null;
		}

		return result.getEntityByRowIndex(0);
	}

}
