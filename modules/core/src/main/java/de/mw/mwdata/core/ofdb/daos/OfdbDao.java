/**
 *
 */
package de.mw.mwdata.core.ofdb.daos;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import de.mw.mwdata.core.daos.ICrudDao;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.def.ConfigOfdb;
import de.mw.mwdata.core.ofdb.def.OfdbPropMapper;
import de.mw.mwdata.core.ofdb.domain.AnsichtDef;
import de.mw.mwdata.core.ofdb.domain.AnsichtOrderBy;
import de.mw.mwdata.core.ofdb.domain.AnsichtTab;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig.DBTYPE;
import de.mw.mwdata.core.ofdb.domain.TabDef;
import de.mw.mwdata.core.ofdb.domain.TabSpeig;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingObjectException;
import de.mw.mwdata.core.ofdb.query.DefaultOfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.OfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.ValueType;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.ordb.query.OperatorEnum;

/**
 * Ofdb-Dao-Layer that encapsulates all ofdb-relevant Daos and DB-operations.
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 *
 */
public class OfdbDao extends HibernateDaoSupport implements IOfdbDao {

	private static final Logger	LOGGER	= LoggerFactory.getLogger( OfdbDao.class );

	// FIXME: remove all ofdbDaos here, use then direclty in ofdbService per injection. use ofdbDao only as
	// hibernate-persister

	@Autowired
	private ICrudDao			crudDao;

	@Autowired
	private OfdbMapper			ofdbMapper;

	/** FIXME: holding map as state in OfdbDao ? */
	// private Map<String, OfdbMapper> tableMap = new HashMap<String, OfdbMapper>();

	// @Override
	@Override
	public Map<String, OfdbPropMapper> initializeMapper( final Class<? extends AbstractMWEntity> type,
			final String tableName ) {

		LOGGER.debug(
				"Loading Cache for Dao : " + tableName + " ....................................." + type.toString() );

		// OfdbMapper mapper = this.tableMap.get( tableName );

		if ( this.getHibernateTemplate().getSessionFactory() != null ) {
			return this.ofdbMapper.init( type, tableName );

			// mapper = new OfdbMapper( type );
			// this.tableMap.put( tableName, mapper );
		} else {
			// FIXME: can this case happen ?
			throw new RuntimeException( "Missing SessionFactory in OfdbDao.initializeMapper()" );
			// return null;
		}

	}

	/**
	 * Method returns the field-value of the entity from the given TabSpeig
	 *
	 * @return the value as object, returns null, if there is no value,
	 *
	 *
	 */
	// @Override
	@Override
	public Object getEntityValue( final AbstractMWEntity entity, final int propPersistenceIndex )
			throws OfdbMissingMappingException {

		return this.ofdbMapper.getPropertyValue( entity, propPersistenceIndex );
	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, TabSpeig> findTabSpeigMapByTable( final String tableName ) {

		OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
		String sql = b.selectTable( ConfigOfdb.T_TABPROPS, "tSpeig" ).fromTable( ConfigOfdb.T_TABPROPS, "tSpeig" )
				.joinTable( ConfigOfdb.T_TABDEF, "tDef" ).whereJoin( "tSpeig", "tabDefId", "tDef", "id" )
				.andWhereRestriction( "tDef", "name", OperatorEnum.Eq, tableName, ValueType.STRING )
				.orderBy( "tSpeig", "reihenfolge", "ASC" ).buildSQL();
		List<IEntity[]> tabProps = this.executeQuery( sql );

		if ( null == tabProps ) {
			String message = MessageFormat.format( "No table properties found by table name {0}. ", tableName );
			throw new OfdbMissingObjectException( message );
		}

		Map<String, TabSpeig> map = new HashMap<String, TabSpeig>();
		for ( int i = 0; i < tabProps.size(); i++ ) {
			Object o = tabProps.get( i );
			TabSpeig tabSpeig = (TabSpeig) o;
			map.put( tabSpeig.getSpalte(), tabSpeig ); // FIXME: remove toUpperCase()
		}
		return map;

	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ITabSpeig> findTabSpeigByTable( final String table ) {

		OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
		String sql = b.selectTable( ConfigOfdb.T_TABPROPS, "tSpeig" ).fromTable( ConfigOfdb.T_TABPROPS, "tSpeig" )
				.joinTable( ConfigOfdb.T_TABDEF, "tDef" ).whereJoin( "tSpeig", "tabDefId", "tDef", "id" )
				.andWhereRestriction( "tDef", "name", OperatorEnum.Eq, table, ValueType.STRING )
				.orderBy( "tSpeig", "reihenfolge", "ASC" ).buildSQL();
		List<IEntity[]> tabProps = executeQuery( sql );

		List<ITabSpeig> props = new ArrayList<ITabSpeig>();
		for ( int i = 0; i < tabProps.size(); i++ ) {
			// Object o = tabProp;
			IEntity[] entityArray = tabProps.get( i );
			props.add( (TabSpeig) entityArray[0] );
		}

		return props;
	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, IAnsichtSpalte> findAnsichtSpaltenMapByAnsicht( final long ansichtId ) {

		OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
		String sql = b.selectTable( ConfigOfdb.T_VIEWPROPS, "aSpalten" ).fromTable( ConfigOfdb.T_VIEWPROPS, "aSpalten" )
				.andWhereRestriction( "aSpalten", "ansichtDefId", OperatorEnum.Eq, new Long( ansichtId ).toString(),
						ValueType.NUMBER )
				.orderBy( "aSpalten", "indexGrid", "asc" ).buildSQL();
		List<IEntity[]> results = executeQuery( sql );

		Map<String, IAnsichtSpalte> map = new HashMap<String, IAnsichtSpalte>( results.size() );
		for ( int i = 0; i < results.size(); i++ ) {
			IEntity[] entityArray = results.get( i );
			IAnsichtSpalte aSpalte = (IAnsichtSpalte) entityArray[0]; // ansichtSpalten.get( i );
			map.put( aSpalte.getSpalteAKey().toUpperCase(), aSpalte );
		}
		return map;

	}

	// @Override
	@Override
	public TabDef findTableDefByFullClassName( final String fullClassName ) {

		OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
		String sql = b.selectTable( ConfigOfdb.T_TABDEF, "t" ).fromTable( ConfigOfdb.T_TABDEF, "t" )
				.andWhereRestriction( "t", "fullClassName", OperatorEnum.Eq, fullClassName, ValueType.STRING )
				.buildSQL();
		List<IEntity[]> result = executeQuery( sql );
		if ( null == result ) {
			String message = MessageFormat.format( "TabDef by fullClassName {0} is not found. ", fullClassName );
			throw new OfdbMissingObjectException( message );
		}

		IEntity[] entityArray = result.get( 0 );
		return (TabDef) entityArray[0];
	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TabDef findTabDefByTableName( final String tableName ) {
		// FIXME: should be replaced by genericDao.findByName
		return (TabDef) this.crudDao.findByName( TabDef.class, tableName );

	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public IAnsichtDef findAnsichtDefByName( final String ansichtName ) {
		// FIXME: should be replaced by genericDao.findByName
		return (IAnsichtDef) this.crudDao.findByName( AnsichtDef.class, ansichtName );
	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AnsichtOrderBy> findAnsichtOrderByAnsichtId( final long ansichtId ) {

		OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
		String sql = b.selectTable( ConfigOfdb.T_VIEWORDERBY, "aOrder" ).fromTable( ConfigOfdb.T_VIEWORDERBY, "aOrder" )
				.joinTable( ConfigOfdb.T_VIEWTAB, "aTab" ).whereJoin( "aOrder", "ansichtTabId", "aTab", "id" )
				.andWhereRestriction( "aTab", "ansichtDefId", OperatorEnum.Eq, new Long( ansichtId ).toString(),
						ValueType.NUMBER )
				.orderBy( "aOrder", "reihenfolge", "asc" ).buildSQL();
		List<IEntity[]> results = this.executeQuery( sql );

		List<AnsichtOrderBy> orders = new ArrayList<AnsichtOrderBy>();

		for ( int i = 0; i < results.size(); i++ ) {
			IEntity[] entityArray = results.get( i );
			orders.add( (AnsichtOrderBy) entityArray[0] );
		}

		return orders;
	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AnsichtTab> findAnsichtTabAnsichtId( final long ansichtId ) {

		OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
		String sql = b
				.selectTable( "AnsichtTab", "aTab" ).fromTable( "AnsichtTab", "aTab" ).andWhereRestriction( "aTab",
						"ansichtDefId", OperatorEnum.Eq, new Long( ansichtId ).toString(), ValueType.NUMBER )
				.orderBy( "aTab", "reihenfolge", "asc" ).buildSQL();
		List<IEntity[]> results = executeQuery( sql );

		List<AnsichtTab> aTabs = new ArrayList<AnsichtTab>();
		for ( int i = 0; i < results.size(); i++ ) {
			IEntity[] entityArray = results.get( i );
			aTabs.add( (AnsichtTab) entityArray[0] );
		}

		return aTabs;

	}

	// @Override
	@Override
	public Object setEntityValue( final AbstractMWEntity entity, final Object value, final ITabSpeig tabSpeig,
			final OfdbPropMapper propMapper ) {

		Object result = null;
		// if enum, than switch from string to enum-value
		Class<? extends AbstractMWEntity> entityClassType = null;
		@SuppressWarnings("rawtypes")
		String fullClassName = tabSpeig.getTabDef().getFullClassName();

		try {
			entityClassType = ClassNameUtils.getClassType( fullClassName );
		} catch ( ClassNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if ( tabSpeig.getDbDatentyp() == DBTYPE.ENUM ) {

			// FIXME: is tabSpeig.getSpalte() correct as propertyName ?
			Class<Enum> c = this.ofdbMapper.getEnumType( propMapper.getPropertyName(), entityClassType );
			result = Enum.valueOf( c, value.toString() ); // FIXME: toString replaced by getDescription() ?
		} else {
			result = value;
		}

		this.ofdbMapper.setPropertyValue( entity, propMapper.getPropertyIndex(), result, entityClassType );
		return result;

	}

	// @Override
	@Override
	public List<AnsichtDef> loadViewsForRegistration( final String applicationContextPath ) {

		OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
		String sql = b.selectTable( ConfigOfdb.T_VIEWDEF, "viewDef" ).fromTable( ConfigOfdb.T_VIEWDEF, "viewDef" )
				.andWhereRestriction( "viewDef", "appContextPath", OperatorEnum.Eq, applicationContextPath,
						ValueType.STRING )
				.andWhereRestriction( "viewDef", "urlPath", OperatorEnum.IsNotNull, "null", ValueType.STRING )
				.orderBy( "viewDef", "reihenfolge", "asc" ).buildSQL();

		List<IEntity[]> results = this.executeQuery( sql );

		List<AnsichtDef> views = new ArrayList<AnsichtDef>();
		if ( CollectionUtils.isEmpty( results ) ) {
			return views;
		}

		for ( int i = 0; i < results.size(); i++ ) {
			// Object o = view;
			IEntity[] entityArray = results.get( i );
			views.add( (AnsichtDef) entityArray[0] );
			// views.add( item );
		}

		return views;
	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<IEntity[]> executeQuery( final String sql ) {

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery( sql );
		// query.setFlushMode( FlushMode.MANUAL );

		List<IEntity[]> arrayResults = null;
		Type[] types = query.getReturnTypes();
		if ( types.length == 1 && types[0] instanceof ManyToOneType ) { // if only entity (and no other column) is
																		// queried, hibernate returns Object instead of
																		// Object[]

			List<IEntity> result = query.list();
			arrayResults = new ArrayList<IEntity[]>( result.size() );

			for ( IEntity item : result ) {
				IEntity[] arrayResult = new IEntity[1];
				arrayResult[0] = item;
				arrayResults.add( arrayResult );
			}

		} else {
			List<IEntity[]> result = query.list();
			arrayResults = new ArrayList<IEntity[]>( result );
		}

		return arrayResults;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<IEntity[]> executeQueryPaginated( final String sql, final int pageIndex ) {

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery( sql );

		int start = (pageIndex - 1) * PaginatedList.DEFAULT_STEPSIZE;
		query.setFirstResult( start );
		query.setMaxResults( PaginatedList.DEFAULT_STEPSIZE );

		@SuppressWarnings("unchecked")
		List<IEntity[]> result = query.list();

		return result;

	}

	// @Override
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public long executeCountQuery( final String sqlCount ) {
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery( sqlCount );

		List<Long> result = query.list();
		return result.get( 0 ).longValue();
	}

	@Override
	public List<Object> getEnumValues( final Class<? extends AbstractMWEntity> entityClassType,
			final String propertyName ) {
		// OfdbMapper ofdbMapper = this.tableMap.get( tableName );

		return this.ofdbMapper.getEnumValues( entityClassType, propertyName );
	}
}
