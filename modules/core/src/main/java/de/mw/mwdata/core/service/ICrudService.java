package de.mw.mwdata.core.service;

import java.util.List;
import java.util.Map;

import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.to.OfdbField;

public interface ICrudService<T> {

	/**
	 * Find an ofdb-entity given by its unique id
	 */
	public T findById(final Class<T> clazz, final Long id);

	/**
	 * Inserts the given ofdb-entity
	 *
	 * @param entity
	 * @return
	 */
	public T insert(T entity);

	/**
	 * Updates the ofdb-entity if already persisted
	 *
	 * @param entity
	 * @return
	 */
	public T update(T entity);

	/**
	 * Deletes the ofdb-entity from persistence-layer
	 *
	 * @param entity
	 */
	public void delete(T entity);

	/**
	 * Returns a list of all entities sorted by the given sortColumns (key =
	 * columnName, value = direction(asc/desc))
	 *
	 * @param sortColumns
	 * @return
	 */
	public List<T> findAll(final Class<T> clazz, final Map<String, String>... sortColumns);

	/**
	 * Returns the entity given the the classtype and the unique natural name-key
	 *
	 * @param clazz
	 * @param name
	 * @return
	 */
	public T findByName(final Class<T> clazz, final String name);

	public QueryResult executeSql(String sql);

	public QueryResult executeSql(String sql, final List<OfdbField> list);

	public QueryResult executeSqlPaginated(final String sql, final List<OfdbField> list, final PagingModel pagingModel);

	public long executeCountSql(final String sqlCount);

}
