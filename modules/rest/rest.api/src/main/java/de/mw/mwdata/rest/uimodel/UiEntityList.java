package de.mw.mwdata.rest.uimodel;

import java.util.ArrayList;
import java.util.List;

import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.to.OfdbField;

public class UiEntityList<E extends AbstractMWEntity> {

	/**
	 *
	 */
	private static final long serialVersionUID = -955049299999002273L;

	private List<UiInputConfig> uiConfigItems;
	private List<EntityTO<E>> entityTOs = new ArrayList<EntityTO<E>>();
	private PagingModel pagingModel;

	public UiEntityList(final List<IEntity[]> entities, final List<OfdbField> ofdbFieldList,
			final PagingModel pagingModel) {
		this.uiConfigItems = new ArrayList<>();
		this.pagingModel = pagingModel;

		for (OfdbField field : ofdbFieldList) {
			UiInputConfig item = new UiInputConfig(field);
			this.uiConfigItems.add(item);
		}

		for (Object[] entity : entities) {

			// FIXME:
			// ... add here additional values for index > 0 as key value pairs
			// maybe key is of type (tablekey|columnkey)
			// means: redesign in OfdbDao.executeQuery()
			// means: return type IEntity[] of ofdbDao is wrong: should be Object[] ->
			// change and test clientside
			// (json-conv)
			// vgl: OfdbField.itemKey, Value, Label felder und deren Verwendung

			addEntityTO(new EntityTO((AbstractMWEntity) entity[0]));
		}

	}

	private UiEntityList() {
		this.uiConfigItems = new ArrayList<>();
	}

	public static UiEntityList createEmptyUiEntityList() {
		return new UiEntityList<AbstractMWEntity>();
	}

	public List<UiInputConfig> getUiInputConfigs() {
		return this.uiConfigItems;
	}

	public void addEntityTO(final EntityTO<E> entityTO) {
		this.entityTOs.add(entityTO);
	}

	public List<EntityTO<E>> getEntityTOs() {
		return this.entityTOs;
	}

	public PagingModel getPagingModel() {
		return this.pagingModel;
	}

}
