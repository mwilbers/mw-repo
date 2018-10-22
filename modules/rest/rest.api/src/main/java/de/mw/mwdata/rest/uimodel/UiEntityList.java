package de.mw.mwdata.rest.uimodel;

import java.util.ArrayList;
import java.util.List;

import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.to.OfdbField;

/**
 * 
 * @author WilbersM
 *
 * @param <E>
 */
public class UiEntityList<E extends AbstractMWEntity> {

	private List<UiInputConfig> uiInputConfigs = new ArrayList<UiInputConfig>();
	private List<EntityTO<E>> entityTOs = new ArrayList<EntityTO<E>>();
	private PagingModel pagingModel;

	public UiEntityList(final List<IEntity[]> entities, final List<OfdbField> ofdbFieldList,
			final PagingModel pagingModel) {
		// this.uiConfigItems = new ArrayList<>();
		this.pagingModel = pagingModel;

		for (OfdbField field : ofdbFieldList) {
			UiInputConfig item = new UiInputConfig(field);
			this.uiInputConfigs.add(item);
		}

		for (Object[] entity : entities) {

			EntityTO en = new EntityTO((AbstractMWEntity) entity[0]);
			if (entity.length > 1) {
				for (int i = 1; i <= entity.length; i++) {
					en.addJoinedValue((String) entity[i]);
				}
			}

			this.entityTOs.add(en);
		}

	}

	public UiEntityList(final PagingModel pagingModel) {
		this.pagingModel = pagingModel;
		// this.uiConfigItems = new ArrayList<>();
	}

	public static UiEntityList createEmptyUiEntityList(PagingModel pagingModel) {
		return new UiEntityList(pagingModel);
	}

	public List<EntityTO<E>> getEntityTOs() {
		return this.entityTOs;
	}

	public PagingModel getPagingModel() {
		return this.pagingModel;
	}

}
