package de.mw.mwdata.ofdb.domain;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.IFxEnum;

/**
 * Each entity is represented as dataset in one table. This can be a databased
 * or filebased table. A table belongs to a database. The entity is identified
 * by its full qualified classname.
 * 
 * @author mwilbers
 *
 */
public interface ITabDef extends IEntity {

	public enum DATENBANK implements IFxEnum {

		K("K"), M("M"), OF("OF"), X("X"), FW("FW"), WS("WS");

		private String description;

		private DATENBANK() {

		}

		private DATENBANK(final String description) {
			this.setDescription(description);
		}

		// public boolean isNullValue() {
		// return false;
		// }

		public void setDescription(final String description) {
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

		public boolean isEmpty() {
			return ("".equals(this.getDescription()));
		}

		public Object getName() {
			return this.name();
		}

	}

	public enum ZEITTYP implements IFxEnum {

		GUELTIGVON("gueltigVon"), VONBIS("vonBis"), DATUM("datum");

		private String description;

		private ZEITTYP(final String description) {
			this.setDescription(description);
		}

		public void setDescription(final String description) {
			this.description = description;
		}

		public String getDescription() {
			return this.description;
		}

		@Override
		public String toString() {
			return getDescription();
		}

		public boolean isEmpty() {
			return ("".equals(this.getDescription()));
		}

		public Object getName() {
			return this.name();
		}

	}

	public void setZeittyp(final ZEITTYP zeittyp);

	public ZEITTYP getZeittyp();

	public void setDatenbank(final DATENBANK datenbank);

	public DATENBANK getDatenbank();

	public void setFullClassName(final String fullClassName);

	public String getFullClassName();

	/**
	 * 
	 * @return the alias of the table name
	 */
	public String getAlias();

}
