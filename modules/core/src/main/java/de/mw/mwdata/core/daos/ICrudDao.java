package de.mw.mwdata.core.daos;

import java.util.List;
import java.util.Map;

import de.mw.mwdata.core.domain.IEntity;

public interface ICrudDao<T> {

	/**
	 * Find an ofdb-entity given by its unique id
	 */
	public T findById(final Class<T> clazz, Long id);

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

	/**
	 * Executes the given sql without restrictions (on sizing, filtering)
	 * 
	 * @param sql
	 * @return
	 */
	public List<IEntity[]> executeSql(String sql);

	/**
	 * Executes the given sql for a certain range of result sets filtered by paging
	 * parameters (pageIndex, pageSize)
	 * 
	 * @param sql
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<IEntity[]> executeSqlPaginated(final String sql, final PagingModel pagingModel);

	/**
	 * Executes the given sql and returns a long parameter for interpreting the
	 * count of the result sets.
	 * 
	 * @param sqlCount
	 * @return
	 */
	public long executeCountSql(String sqlCount);

}
