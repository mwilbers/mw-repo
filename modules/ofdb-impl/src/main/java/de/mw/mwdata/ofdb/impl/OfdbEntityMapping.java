package de.mw.mwdata.ofdb.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import de.mw.mwdata.core.domain.DBTYPE;
import de.mw.mwdata.ofdb.domain.ITabSpeig;

/**
 * Class holds mapping informations for all columns of one table in OFDB
 * database.
 * 
 * @author WilbersM
 *
 */
public class OfdbEntityMapping {

	private String tableName;

	/**
	 * key = columnNameDb
	 */
	private Map<String, OfdbPropMapper> mapping = new HashMap<>();

	public OfdbEntityMapping(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("table = '");
		b.append(this.tableName);
		b.append("', mappings = [");
		if (!CollectionUtils.isEmpty(this.mapping.entrySet())) {
			for (Map.Entry<String, OfdbPropMapper> entry : this.mapping.entrySet()) {
				b.append(entry.getValue().toString());
				b.append(" ");
			}
		}
		b.append("]");
		return b.toString();
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
		return this.mapping.get(tableProp.getSpalte().toUpperCase());
	}

	public Collection<OfdbPropMapper> getMappings() {
		return this.mapping.values();
	}

	public OfdbPropMapper findPropertyMapperByTabProp(final ITabSpeig tableProp) {
		for (OfdbPropMapper mapper : this.getMappings()) {
			if (mapper.getColumnName().toUpperCase().equals(tableProp.getSpalte().toUpperCase())) {
				return mapper;
			}
		}

		return null;
	}

}
