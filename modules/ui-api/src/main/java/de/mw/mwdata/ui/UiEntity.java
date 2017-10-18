package de.mw.mwdata.ui;

import de.mw.mwdata.core.domain.IEntity;

/**
 * Wrapper class for transporting array types of {@link IEntity} to UI<br>
 * FIXME: class really needed ? @see EntityTO
 * 
 * @author wilbersm
 *
 */
// @Deprecated
public class UiEntity {

	private Object[] entityArray;

	public UiEntity(Object[] entityArray) {
		this.setEntityArray(entityArray);
	}

	public Object[] getEntityArray() {
		return entityArray;
	}

	public void setEntityArray(Object[] entityArray) {
		this.entityArray = entityArray;
	}

}
