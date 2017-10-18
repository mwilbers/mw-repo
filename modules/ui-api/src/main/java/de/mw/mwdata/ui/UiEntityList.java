package de.mw.mwdata.ui;

import java.util.ArrayList;
import java.util.List;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.def.OfdbField;

public class UiEntityList {

	/**
	 * 
	 */
	private static final long serialVersionUID = -955049299999002273L;

	private List<OfdbField> ofdbFields;
	private List<UiEntity> uiEntities = new ArrayList<UiEntity>();

	public UiEntityList(final List<IEntity[]> entities, final List<OfdbField> ofdbFieldList) {
		this.ofdbFields = new ArrayList<>(ofdbFieldList);
		for (Object[] entity : entities) {
			addUiEntity(new UiEntity(entity));
		}
	}

	// private Object[] entityArray;
	//
	// public Object[] getEntityArray() {
	// return entityArray;
	// }
	//
	// public void setEntityArray(Object[] entityArray) {
	// // FIXME: deep copy here ...
	// this.entityArray = entityArray;
	// }

	public List<OfdbField> getOfdbFields() {
		return this.ofdbFields;
	}

	public void setOfdbFields(final List<OfdbField> fields) {
		this.ofdbFields = new ArrayList<>(fields);
	}

	public List<UiEntity> getUiEntities() {
		return uiEntities;
	}

	public void setUiEntities(final List<UiEntity> entities) {
		this.uiEntities = entities;
	}

	public void addUiEntity(final UiEntity entity) {
		this.uiEntities.add(entity);
	}

}
