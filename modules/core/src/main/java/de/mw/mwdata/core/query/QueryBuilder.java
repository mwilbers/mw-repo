package de.mw.mwdata.core.query;

// FIXME: rename to QueryBuilder (no deps to Ofdb)
public interface QueryBuilder {

	public QueryBuilder setCount(final boolean count);

	public String buildSQL() throws InvalidQueryConfigurationException;

	// public void reset();

	/**
	 * 
	 * @param entityName
	 *            the simple entity name
	 * @param tableAlias
	 *            the alias name from ofdb
	 * @return
	 */
	public QueryBuilder selectEntity(final String entityName, final String tableAlias);

	/**
	 * 
	 * @param tableAlias
	 *            the alias name of the ofdb table
	 * @param propertyName
	 *            the property name of the underlying entity
	 * @return
	 */
	public QueryBuilder selectAlias(final String tableAlias, final String propertyName);

	/**
	 * 
	 * @param entityName
	 *            the simple entity name
	 * @param tableAlias
	 *            the table alias from ofdb
	 * @return
	 */
	public QueryBuilder fromEntity(final String entityName, final String tableAlias);

	public QueryBuilder joinTable(final String tableName, final String tableAlias);

	public QueryBuilder leftJoinTable(final String leftTableAlias, final String association, final String assocAlias);

	/**
	 * Creates a join expression on an associated entity defined in domain class
	 *
	 * @param entityName
	 * @param entityAlias
	 * @return
	 */
	public QueryBuilder joinEntity(final String entityName, final String entityAlias);

	public QueryBuilder whereJoin(final String join1Table, final String join1Column, final String join2Table,
			final String join2Column);

	public QueryBuilder andWhereRestriction(final String tableAlias, final String columnAlias,
			final OperatorEnum operator, final Object value, final ValueType valueType);

	public QueryBuilder orderBy(final String tableAlias, final String columnAlias, final String orderDirection);

}
