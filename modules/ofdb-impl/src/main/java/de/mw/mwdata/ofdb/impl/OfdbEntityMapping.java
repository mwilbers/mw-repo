package de.mw.mwdata.ofdb.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.ITabSpeig.DBTYPE;

/**
 * Class holds all column - propertyname mapping pairs for one table in OFDB
 * database.
 * 
 * @author WilbersM
 *
 */
public class OfdbEntityMapping {

	private String tableName;
	private Map<String, OfdbPropMapper> mapping = new HashMap<>();

	public OfdbEntityMapping(String tableName) {
		this.tableName = tableName;
	}

	public void addMapping(String columnNameDb, String propertyName, Integer propertyIndex, DBTYPE dbType) {

		OfdbPropMapper propMapper = new OfdbPropMapper(this.tableName, columnNameDb);
		propMapper.setPropertyName(propertyName);
		propMapper.setPropertyIndex(propertyIndex);

		// DBTYPE dbType = convertTypeToDbType(propertyTypes[i]);
		propMapper.setDbType(dbType);

		this.mapping.put(columnNameDb, propMapper);
	}

	public boolean hasMapping(final ITabSpeig tableProp) {
		OfdbPropMapper mapper = this.mapping.get(tableProp.getSpalte().toUpperCase());
		return (null != mapper);
	}

	public OfdbPropMapper getMapper(final ITabSpeig tableProp) {
		// FIXME: method necessary ? better OfdbPropMapper is package visible
		return this.mapping.get(tableProp.getSpalte().toUpperCase());
	}

	public Set<Map.Entry<String, OfdbPropMapper>> getMapperSet() {
		// FIXME method still needed ? only in tests ... ugly ...
		return this.mapping.entrySet();
	}

}
