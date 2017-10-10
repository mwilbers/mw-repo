package de.mw.mwdata.core.ofdb.domain;

import de.mw.mwdata.core.domain.IFxEnum;
import de.mw.mwdata.core.domain.IEntity;

/**
 * Each entity is represented as dataset in one table. This can be a databased or filebased table. 
 * A table belongs to a database. The entity is identified by its full qualified classname.
 * @author mwilbers
 *
 */
public interface ITabDef extends IEntity {

	public enum DATENBANK implements IFxEnum {

		K("K"), M("M"), OF("OF"), X("X"), FW("FW"), WS("WS");

		private String	description;

		private DATENBANK() {

		}

		private DATENBANK(final String description) {
			this.setDescription( description );
		}

		// public boolean isNullValue() {
		// return false;
		// }

		public void setDescription( final String description ) {
			this.description = description;
		}

		// @Override
		public String getDescription() {
			return this.description;
		}

		// @Override
		@Override
		public String toString() {
			return getDescription();
		}

		// // @Override
		// public DATENBANK getValue( final String value ) {
		// return DATENBANK.valueOf( value );
		// }

		// // @Override
		// public IFxEnum[] getValues() {
		// return DATENBANK.values();
		// }

		public boolean isEmpty() {
			return ("".equals( this.getDescription() ));
		}

		public Object getName() {
			return this.name();
		}

	}
	
	public enum ZEITTYP implements IFxEnum {

		GUELTIGVON("gueltigVon"), VONBIS("vonBis"), DATUM("datum");

		private String	description;

		private ZEITTYP(final String description) {
			this.setDescription( description );
		}

		public void setDescription( final String description ) {
			this.description = description;
		}

		// @Override
		public String getDescription() {
			return this.description;
		}

		// // @Override
		// public IFxEnum getValue( final String value ) {
		// return ZEITTYP.valueOf( value );
		// }

		// @Override
		@Override
		public String toString() {
			return getDescription();
		}

		public boolean isEmpty() {
			return ("".equals( this.getDescription() ));
		}

		public Object getName() {
			return this.name();
		}

	}
	
	public void setZeittyp( final ZEITTYP zeittyp );
	
	public ZEITTYP getZeittyp();

	public void setDatenbank( final DATENBANK datenbank );

	public DATENBANK getDatenbank();
	
	public void setFullClassName( final String fullClassName );

	public String getFullClassName();

	/**
	 * 
	 * @return the alias of the table name
	 */
	public String getAlias();
	
}
