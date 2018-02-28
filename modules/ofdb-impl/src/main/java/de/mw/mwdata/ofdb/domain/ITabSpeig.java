package de.mw.mwdata.ofdb.domain;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.IFxEnum;

public interface ITabSpeig extends IEntity {

	public enum DBTYPE implements IFxEnum {

		LONGINTEGER("LONGINTEGER"), STRING("STRING"), BOOLEAN("BOOLEAN"), DATE("DATE"), DATETIME("DATETIME"), DOUBLE(
				"DOUBLE"), BYTE("BYTE"), SINGLE("SINGLE"), ENUM("ENUM"), ENTITY("ENTITY"); // new: 23.04. Type for
		// enumerations

		// with bounded number of items

		private String	description;

		private DBTYPE() {

		}

		private DBTYPE(final String description) {
			this.setDescription( description );
		}

		public void setDescription( final String description ) {
			this.description = description;
		}

		public String getDescription() {
			return this.description;
		}

		@Override
		public String toString() {
			return getDescription();
		}

		// // @Override
		// public DBTYPE getValue( final String value ) {
		// return DBTYPE.valueOf( value );
		// }

		public boolean isEmpty() {
			return ("".equals( this.getDescription() ));
		}

		public Object getName() {
			return this.name();
		}

	}
	
	public  void setTabDef( ITabDef tabDef );

	public  ITabDef getTabDef();

	public  String getSpalte();

	public  void setSpalte( String spalte );

	public  Long getEindeutig();

	public  void setEindeutig( Long eindeutig );

	public  void setDbDatentyp( DBTYPE dbDatentyp );

	public  DBTYPE getDbDatentyp();

	/**
	 * 
	 * @return true if value of this column has to be unique
	 */
	public boolean isEindeutig();

	public void setTabDefId( Long tabDefId );

	public Long getTabDefId();

	/**
	 * 
	 * @return the position for ordering
	 */
	public Long getReihenfolge();

	/**
	 * 
	 * @return true if value must not be null
	 */
	public Boolean getEingabeNotwendig();

	/**
	 * 
	 * @return the caption for showing column in view
	 */
	public String getSpaltenkopf();

	/**
	 * 
	 * @return true if column is set by system only
	 */
	public Boolean getSystemWert();

	/**
	 * 
	 * @return true if column can be edited by user
	 */
	public Boolean getBearbErlaubt();

	/**
	 * 
	 * @return the maximum value. Can be numer or maybe string 
	 */
	public Object getMaximum();

	/**
	 * 
	 * @return true if the column has a special range of valid values
	 */
	public  boolean isEnum();

	/**
	 * 
	 * @return common object for a default value if no value is given by user
	 */
	public  Object getDefaultWert();

	/**
	 * 
	 * @return true if column holds primary key
	 */
	public Boolean getPrimSchluessel();

}