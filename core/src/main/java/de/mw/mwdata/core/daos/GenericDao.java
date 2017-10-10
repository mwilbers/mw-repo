/*
 * Copyright MWData Markus Wilbers
 */
package de.mw.mwdata.core.daos;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic base-DAO-class for hibernate-specific CRUD-operations with database
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Jan, 2010
 *
 * @param <T>
 */
@Deprecated
public class GenericDao<T> extends HibernateDaoSupport implements IGenericDao<T> {

	private static final Logger	LOGGER	= LoggerFactory.getLogger( GenericDao.class );

	private Class<T>			type;

	// @Autowired
	protected SessionFactory	sessionFactory;

	public GenericDao(final Class<T> type) {
		this.type = type;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected Session getCurrentSession() {
		return this.getHibernateTemplate().getSessionFactory().getCurrentSession();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete( final T entity ) {
		LOGGER.debug( "delete [" + entity + "]" );
		this.getHibernateTemplate().delete( entity );
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public T findById( final Long id ) {
		return this.getHibernateTemplate().get( this.type, id );
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T insert( final T entity ) {
		LOGGER.debug( "insert [" + entity + "]" );
		this.getHibernateTemplate().save( entity );
		this.getHibernateTemplate().flush();
		return entity;
	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T update( final T entity ) {

		LOGGER.debug( "update [" + entity + "]" );
		this.getHibernateTemplate().save( entity );
		this.getHibernateTemplate().flush();
		return entity;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<T> findAll( final Map<String, String>... sortColumns ) {

		Criteria crit = getCurrentSession().createCriteria( this.type );

		if ( !ArrayUtils.isEmpty( sortColumns ) ) {
			addOrders( crit, sortColumns[0] );
		}

		List<T> result = crit.list();
		if ( result == null ) {
			result = Collections.emptyList();
		}
		return result;
	}

	/**
	 * Method adds the given entries in the sortColumn-Map to the given Hibernate-criteria. Not null-safe.
	 *
	 * @param crit
	 * @param sortColumns
	 *            LinkedHashMap with key = column-name and value = asc/desc
	 */
	protected void addOrders( final Criteria crit, final Map<String, String> sortColumns ) {

		for ( Map.Entry<String, String> entry : sortColumns.entrySet() ) {
			if ( "asc".equals( entry.getValue() ) ) {
				crit.addOrder( Order.asc( entry.getKey() ) );
			} else {
				crit.addOrder( Order.desc( entry.getKey() ) );
			}
		}

	}

	// @Override
	@Override
	public T findByName( final Class<T> clazz, final String name ) {

		Criteria crit = getCurrentSession().createCriteria( clazz );
		crit.add( Restrictions.eq( "name", name ) );
		crit.addOrder( Order.asc( "name" ) );
		List<T> result = crit.list();
		if ( null != result && !result.isEmpty() ) {
			return result.get( 0 );
		}
		return null;

	}

}
