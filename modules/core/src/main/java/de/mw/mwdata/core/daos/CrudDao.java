package de.mw.mwdata.core.daos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.utils.PaginatedList;

public class CrudDao<T> extends HibernateDaoSupport implements ICrudDao<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrudDao.class);

	@Transactional(propagation = Propagation.REQUIRED)
	protected Session getCurrentSession() {
		return this.getHibernateTemplate().getSessionFactory().getCurrentSession();
	}

	@Override
	public T findById(final Class<T> clazz, final Long id) {
		return this.getHibernateTemplate().get(clazz, id);
	}

	@Override
	public T insert(final T entity) {
		LOGGER.debug("insert [" + entity + "]");
		this.getHibernateTemplate().save(entity);

		// FIXME: necessary for CRUDTest, but needed in general ?
		this.getHibernateTemplate().flush();
		return entity;
	}

	@Override
	public T update(final T entity) {
		LOGGER.debug("update [" + entity + "]");
		// this.getHibernateTemplate().flush();
		this.getCurrentSession().update(entity); // .save( entity );
		// this.getHibernateTemplate().saveOrUpdate( entity );
		// ... testen

		// FIXME: necessary for CRUDTest, but needed in general ?
		this.getHibernateTemplate().flush();
		return entity;
	}

	@Override
	public void delete(final T entity) {
		LOGGER.debug("delete [" + entity + "]");
		this.getHibernateTemplate().delete(entity);
		// FIXME: necessary for CRUDTest, but needed in general ?
		this.getHibernateTemplate().flush();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<T> findAll(final Class<T> clazz, final Map<String, String>... sortColumns) {
		Criteria crit = getCurrentSession().createCriteria(clazz);

		if (!ArrayUtils.isEmpty(sortColumns)) {
			addOrders(crit, sortColumns[0]);
		}

		List<T> result = crit.list();
		if (result == null) {
			result = Collections.emptyList();
		}
		return result;
	}

	@Override
	public T findByName(final Class<T> clazz, final String name) {
		Criteria crit = getCurrentSession().createCriteria(clazz);
		crit.add(Restrictions.eq("name", name));
		crit.addOrder(Order.asc("name"));
		List<T> result = crit.list();
		if (null != result && !result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * Method adds the given entries in the sortColumn-Map to the given
	 * Hibernate-criteria. Not null-safe.
	 *
	 * @param crit
	 * @param sortColumns
	 *            LinkedHashMap with key = column-name and value = asc/desc
	 */
	protected void addOrders(final Criteria crit, final Map<String, String> sortColumns) {

		for (Map.Entry<String, String> entry : sortColumns.entrySet()) {
			if ("asc".equals(entry.getValue())) {
				crit.addOrder(Order.asc(entry.getKey()));
			} else {
				crit.addOrder(Order.desc(entry.getKey()));
			}
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<IEntity[]> executeSql(final String sql) {

		Query query = this.getCurrentSession().createQuery(sql);
		List<IEntity[]> arrayResults = null;
		Type[] types = query.getReturnTypes();

		if (types.length == 1 && types[0] instanceof ManyToOneType) {
			// if only entity (and no other column) is queried, hibernate returns Object
			// instead of Object[]

			List<IEntity> result = query.list();
			arrayResults = new ArrayList<IEntity[]>(result.size());
			for (IEntity item : result) {
				IEntity[] arrayResult = new IEntity[1];
				arrayResult[0] = item;
				arrayResults.add(arrayResult);
			}

		} else {
			List<IEntity[]> result = query.list();
			arrayResults = new ArrayList<IEntity[]>(result);
		}

		return arrayResults;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<IEntity[]> executeSqlPaginated(final String sql, final int pageIndex) {

		Query query = this.getCurrentSession().createQuery(sql);

		int start = (pageIndex - 1) * PaginatedList.DEFAULT_STEPSIZE;
		query.setFirstResult(start);
		query.setMaxResults(PaginatedList.DEFAULT_STEPSIZE);

		return query.list();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public long executeCountSql(final String sqlCount) {
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sqlCount);
		List<Long> result = query.list();
		return result.get(0).longValue();
	}

}
