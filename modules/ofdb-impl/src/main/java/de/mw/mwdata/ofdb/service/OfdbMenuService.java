package de.mw.mwdata.ofdb.service;

import org.apache.commons.collections.ListUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mw.mwdata.core.query.OperatorEnum;
import de.mw.mwdata.core.query.QueryBuilder;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.query.SimpleQueryBuilder;
import de.mw.mwdata.core.query.ValueType;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.domain.IMenue;
import de.mw.mwdata.ofdb.domain.impl.Menue;
import de.mw.mwdata.ofdb.impl.ConfigOfdb;

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

		// ... fehler hier: MenuService holt nur Informationen zur Darstellugn des
		// Menues, aber nicht der View im Contentbereich,
		// daher darf MenuService nicht den OfdbMetaDataQueryBuilder und die viewHandle
		// verwenden

		// ViewConfigHandle viewHandle =
		// this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);
		// if (null == viewHandle) {
		// return QueryResult.createEmptyQueryResult();
		// }

		QueryBuilder builder = new SimpleQueryBuilder(); // new OfdbMetaDataQueryBuilder(viewHandle,
															// this.ofdbService);

		String sql = builder.selectEntity(ConfigOfdb.T_MENU, "aMenu").selectAlias("aView", "urlPath")
				.fromEntity(ConfigOfdb.T_MENU, "aMenu").leftJoinTable("aMenu", "ansichtDef", "aView")
				.joinEntity("bereich", "bBereich")
				.andWhereRestriction("aMenu", "ebene", OperatorEnum.Eq, String.valueOf(0), ValueType.NUMBER)
				.andWhereRestriction("bBereich", "name", OperatorEnum.Eq, userAreaName, ValueType.STRING)
				.orderBy("aMenu", "anzeigeName", "asc").buildSQL();

		return this.crudService.executeSql(sql, ListUtils.EMPTY_LIST);
	}

	@Override
	public IMenue findMenuById(long menuId) {
		return this.crudService.findById(Menue.class, menuId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public QueryResult findChildMenus(final IMenue menu, final String userAreaName) {

		// ViewConfigHandle viewHandle =
		// this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);
		QueryBuilder builder = new SimpleQueryBuilder();
		// new OfdbMetaDataQueryBuilder(viewHandle, this.ofdbService);

		Menue mainMenu = (Menue) menu; // this.crudService.findById(Menue.class, parentMenuId);
		String sql = builder.selectEntity(ConfigOfdb.T_MENU, "aMenu").selectAlias("aView", "urlPath")
				.fromEntity(ConfigOfdb.T_MENU, "aMenu").leftJoinTable("aMenu", "ansichtDef", "aView")
				.joinEntity("bereich", "bBereich")
				.andWhereRestriction("aMenu", "hauptMenueId", OperatorEnum.Eq, String.valueOf(mainMenu.getId()),
						ValueType.NUMBER)
				.andWhereRestriction("bBereich", "name", OperatorEnum.Eq, userAreaName, ValueType.STRING)
				.orderBy("aMenu", "anzeigeName", "asc").buildSQL();

		return this.crudService.executeSql(sql, ListUtils.EMPTY_LIST);
	}

	@Override
	public IMenue findMenuByUrlPath(String urlPath) {

		// ViewConfigHandle viewHandle =
		// this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);
		// if (null == viewHandle) {
		// return null;
		// }

		QueryBuilder builder = new SimpleQueryBuilder();
		// new OfdbMetaDataQueryBuilder(viewHandle, this.ofdbService);
		String sql = builder.selectEntity(ConfigOfdb.T_MENU, "aMenu").fromEntity(ConfigOfdb.T_MENU, "aMenu")
				.leftJoinTable("aMenu", "ansichtDef", "aView")
				.andWhereRestriction("aView", "urlPath", OperatorEnum.Eq, urlPath, ValueType.STRING).buildSQL();
		QueryResult result = this.crudService.executeSql(sql, ListUtils.EMPTY_LIST);

		if (result.isEmpty()) {
			return null;
		}

		return (IMenue) result.getEntityByRowIndex(0);
	}

}
