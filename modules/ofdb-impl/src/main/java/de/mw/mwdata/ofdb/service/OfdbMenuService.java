package de.mw.mwdata.ofdb.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.ObjectUtils;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.query.DefaultOfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.OfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.OperatorEnum;
import de.mw.mwdata.core.ofdb.query.ValueType;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.service.IMenuService;
import de.mw.mwdata.ofdb.dao.IOfdbDao;
import de.mw.mwdata.ofdb.impl.ConfigOfdb;

public class OfdbMenuService implements IMenuService {

	private IOfdbDao ofdbDao;

	private ICrudService<IEntity> crudService;

	public void setCrudService(ICrudService<IEntity> crudService) {
		this.crudService = crudService;
	}

	public void setOfdbDao(final IOfdbDao ofdbDao) {
		this.ofdbDao = ofdbDao;
	}

	@Override
	public List<EntityTO> findMainMenus() {

		OfdbQueryBuilder builder = new DefaultOfdbQueryBuilder();
		int layer = 0;
		String sql = builder.selectTable(ConfigOfdb.T_MENU, "aMenu").selectAlias("aView", "urlPath")
				.fromTable(ConfigOfdb.T_MENU, "aMenu").leftJoinTable("aMenu", "ansichtDef", "aView")
				.andWhereRestriction("aMenu", "ebene", OperatorEnum.Eq, String.valueOf(layer), ValueType.NUMBER)
				.orderBy("aMenu", "anzeigeName", "asc").buildSQL();

		List<IEntity[]> entities = this.crudService.executeSql(sql);
		List<EntityTO> entityTOs = convertToEntityTO(entities, new String("urlPath"));

		return entityTOs;
	}

	private List<EntityTO> convertToEntityTO(final List<IEntity[]> entities, final String... joinedKeys) {

		List<EntityTO> entityTOs = new ArrayList<>();

		for (Object[] item : entities) {
			if (ObjectUtils.isEmpty(item[0])) {
				throw new IllegalStateException("Entity must not be empty in ofdb based result array IEntity[].");
			}
			EntityTO entityTO = new EntityTO<AbstractMWEntity>((AbstractMWEntity) item[0]);

			if (!ArrayUtils.isEmpty(joinedKeys)) {
				if (joinedKeys.length != item.length - 1) {
					throw new IllegalStateException("Number of joined keys does not match number of retrieved values.");
				}
				for (int i = 0; i < joinedKeys.length; i++) {
					entityTO.addJoinedValue(joinedKeys[i], (String) item[i + 1]);
				}
			}

			entityTOs.add(entityTO);
		}

		return entityTOs;
	}

	@Override
	public List<EntityTO> findChildMenus(final int parentMenuId) {

		OfdbQueryBuilder builder = new DefaultOfdbQueryBuilder();
		String sql = builder.selectTable(ConfigOfdb.T_MENU, "aMenu").selectAlias("aView", "urlPath")
				.fromTable(ConfigOfdb.T_MENU, "aMenu").leftJoinTable("aMenu", "ansichtDef", "aView")
				.andWhereRestriction("aMenu", "hauptMenueId", OperatorEnum.Eq, String.valueOf(parentMenuId),
						ValueType.NUMBER)
				.orderBy("aMenu", "anzeigeName", "asc").buildSQL();

		List<IEntity[]> entities = this.crudService.executeSql(sql);
		List<EntityTO> entityTOs = convertToEntityTO(entities, new String("urlPath"));

		return entityTOs;
	}

}
