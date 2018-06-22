package de.mw.mwdata.core.domain;

public enum DBTYPE implements IFxEnum {

	LONGINTEGER("LONGINTEGER"), STRING("STRING"), BOOLEAN("BOOLEAN"), DATE("DATE"), DATETIME("DATETIME"), DOUBLE(
			"DOUBLE"), BYTE("BYTE"), SINGLE("SINGLE"), ENUM("ENUM"), ENTITY("ENTITY"); // new: 23.04. Type for
	// enumerations

	// with bounded number of items

	private String description;

	private DBTYPE() {

	}

	private DBTYPE(final String description) {
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

	// // @Override
	// public DBTYPE getValue( final String value ) {
	// return DBTYPE.valueOf( value );
	// }

	public boolean isEmpty() {
		return ("".equals(this.getDescription()));
	}

	public Object getName() {
		return this.name();
	}

}
