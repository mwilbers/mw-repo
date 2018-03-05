package de.mw.mwdata.ofdb.impl;

import java.util.ArrayList;
import java.util.List;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;

public class UiEntityList<E extends AbstractMWEntity> {

	/**
	 *
	 */
	private static final long serialVersionUID = -955049299999002273L;

	private List<OfdbField> ofdbFields;
	private List<EntityTO<E>> entityTOs = new ArrayList<EntityTO<E>>();

	public UiEntityList(final List<IEntity[]> entities, final List<OfdbField> ofdbFieldList) {
		this.ofdbFields = new ArrayList<>(ofdbFieldList);

		// if ( entities.get( 0 ).getClass().isArray() ) {

		for (Object[] entity : entities) {

			// FIXME:
			// ... add here additional values for index > 0 as key value pairs
			// maybe key is of type (tablekey|columnkey)
			// means: redesign in OfdbDao.executeQuery()
			// means: return type IEntity[] of ofdbDao is wrong: should be Object[] ->
			// change and test clientside
			// (json-conv)
			// vgl: OfdbField.itemKey, Value, Label felder und deren Verwendung

			// FIXME: add suchwert to EntityTO here
			addEntityTO(new EntityTO((AbstractMWEntity) entity[0]));
		}

		// } else {
		//
		// List<IEntity> singleEntites = entities;
		// for ( IEntity[] entity : entities ) {
		//
		// // FIXME: add suchwert to EntityTO here
		// addEntityTO( new EntityTO( (AbstractMWEntity) entity[0] ) );
		// }
		//
		// }

	}

	public List<OfdbField> getOfdbFields() {
		return this.ofdbFields;
	}

	public void setOfdbFields(final List<OfdbField> fields) {
		this.ofdbFields = new ArrayList<>(fields);
	}

	public void addEntityTO(final EntityTO<E> entityTO) {
		this.entityTOs.add(entityTO);
	}

	public List<EntityTO<E>> getEntityTOs() {
		return this.entityTOs;
	}

}
