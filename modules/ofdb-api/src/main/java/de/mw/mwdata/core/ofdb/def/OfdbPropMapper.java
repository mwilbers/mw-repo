package de.mw.mwdata.core.ofdb.def;

import java.io.Serializable;

import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig.DBTYPE;

public class OfdbPropMapper implements Serializable {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 715170130171288780L;

	private String				tableName;
	private String				columnName;
	private String				propertyName;
	private ITabSpeig.DBTYPE	dbType;

	/**
	 * Describes the index of the property in the hibernate persistence array of all properties
	 */
	private int					propertyIndex;

	public OfdbPropMapper(final String tableName, final String columnName) {
		this.tableName = tableName;
		this.columnName = columnName;
		// this.propertyName = propertyName;
	}

	public void setPropertyName( final String propertyName ) {
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

	public void setPropertyIndex( final int propertyIndex ) {
		this.propertyIndex = propertyIndex;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public DBTYPE getDbType() {
		return this.dbType;
	}

	public void setDbType( final DBTYPE dbType ) {
		this.dbType = dbType;
	}

	public boolean isAssociationType() {
		return this.dbType.equals( DBTYPE.ENTITY );
	}

}
