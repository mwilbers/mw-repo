package de.mw.mwdata.core.domain;

/**
 * Wrapper class for transporting array types of {@link IEntity} to UI<br>
 * FIXME: class really needed ? @see EntityTO
 *
 * @author wilbersm
 *
 */
@Deprecated
public class UiEntity {

	private Object[] entityArray;

	public UiEntity(final Object[] entityArray) {
		this.setEntityArray( entityArray );
	}

	public Object[] getEntityArray() {
		return this.entityArray;
	}

	public void setEntityArray( final Object[] entityArray ) {
		this.entityArray = entityArray;
	}

}
