package de.mw.mwdata.core.domain;

import java.util.ArrayList;
import java.util.List;
import de.mw.mwdata.core.ofdb.def.OfdbField;

public class UiEntityList<E extends AbstractMWEntity> {

	/**
	 *
	 */
	private static final long	serialVersionUID	= -955049299999002273L;

	private List<OfdbField>		ofdbFields;
	private List<UiEntity>		uiEntities			= new ArrayList<UiEntity>();
	private List<EntityTO<E>>	entityTOs			= new ArrayList<EntityTO<E>>();

	public UiEntityList(final List<IEntity[]> entities, final List<OfdbField> ofdbFieldList) {
		this.ofdbFields = new ArrayList<>( ofdbFieldList );
		for ( Object[] entity : entities ) {
			addUiEntity( new UiEntity( entity ) );

			// FIXME: add suchwert to EntityTO here
			addEntityTO( new EntityTO( (AbstractMWEntity) entity[0] ) );
		}
	}

	public List<OfdbField> getOfdbFields() {
		return this.ofdbFields;
	}

	public void setOfdbFields( final List<OfdbField> fields ) {
		this.ofdbFields = new ArrayList<>( fields );
	}

	public List<UiEntity> getUiEntities() {
		return this.uiEntities;
	}

	public void setUiEntities( final List<UiEntity> entities ) {
		this.uiEntities = entities;
	}

	public void addUiEntity( final UiEntity entity ) {
		this.uiEntities.add( entity );
	}

	public void setEntityTOs( final List<EntityTO<E>> entityTO ) {
		// FIXME: bad style: setting list from external
		this.entityTOs = entityTO;
	}

	public void addEntityTO( final EntityTO<E> entityTO ) {
		this.entityTOs.add( entityTO );
	}

	public List<EntityTO<E>> getEntityTOs() {
		return this.entityTOs;
	}

}
