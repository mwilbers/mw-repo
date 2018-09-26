package de.mw.mwdata.ofdb.impl;

import java.io.Serializable;

import de.mw.mwdata.core.domain.DBTYPE;

public class OfdbPropMapper implements Serializable {

	private static final long serialVersionUID = 715170130171288780L;

	private String tableName;
	private String columnName;
	private String propertyName;
	private DBTYPE dbType;

	/**
	 * Holds the propertyname of the association list based property in Entity with
	 * same columnName. E.g. for TabDef: propName = 'bereichsID' =>
	 * associationPropName = 'bereich'
	 */
	private String associatedEntityName;

	/**
	 * Describes the index of the property in the hibernate persistence array of all
	 * properties
	 */
	private int propertyIndex;

	public OfdbPropMapper(final String tableName, final String columnName) {
		this.tableName = tableName;
		this.columnName = columnName;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("OfdbPropMapper (");
		b.append("table = ");
		b.append(this.tableName);
		b.append(", columnName = ");
		b.append(this.columnName);
		b.append(", propertyName = ");
		b.append(this.propertyName);
		b.append(" )");

		return b.toString();
	}

	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public int getPropertyIndex() {
		return this.propertyIndex;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setPropertyIndex(final int propertyIndex) {
		this.propertyIndex = propertyIndex;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public DBTYPE getDbType() {
		return this.dbType;
	}

	public void setDbType(final DBTYPE dbType) {
		this.dbType = dbType;
	}

	public boolean isAssociationType() {
		return this.dbType.equals(DBTYPE.ENTITY);
	}

	public String getAssociatedEntityName() {
		return associatedEntityName;
	}

	public void setAssociatedEntityName(String associatedEntityName) {
		this.associatedEntityName = associatedEntityName;
	}

}
