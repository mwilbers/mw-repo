package de.mw.mwdata.core.ofdb.query;

import de.mw.mwdata.ordb.query.OperatorEnum;

public interface OfdbQueryBuilder {

	public OfdbQueryBuilder setCount( final boolean count );

	public String buildSQL() throws InvalidQueryConfigurationException;

	public void reset();

	public OfdbQueryBuilder selectTable( final String tableName, final String tableAlias );

	public OfdbQueryBuilder selectAlias( final String tableAlias, final String columnAlias );

	public OfdbQueryBuilder fromTable( final String tableName, final String tableAlias );

	public OfdbQueryBuilder joinTable( final String tableName, final String tableAlias );

	public OfdbQueryBuilder leftJoinTable( final String leftTableAlias, final String association,
			final String assocAlias );

	/**
	 * Creates a join expression on an associated entity defined in domain class
	 *
	 * @param entityName
	 * @param entityAlias
	 * @return
	 */
	public OfdbQueryBuilder joinEntity( final String entityName, final String entityAlias );

	public OfdbQueryBuilder whereJoin( final String join1Table, final String join1Column, final String join2Table,
			final String join2Column );

	public OfdbQueryBuilder andWhereRestriction( final String tableAlias, final String columnAlias,
			final OperatorEnum operator, final Object value, final ValueType valueType );

	public OfdbQueryBuilder orderBy( final String tableAlias, final String columnAlias, final String orderDirection );

}
