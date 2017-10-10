package de.mw.mwdata.core.ofdb.service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.LocalizedMessages;
import de.mw.mwdata.core.db.FxBooleanType;
import de.mw.mwdata.core.db.UniqueTabSpeigBucket;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.IFxEnum;
import de.mw.mwdata.core.ofdb.MapValue;
import de.mw.mwdata.core.ofdb.OfdbUtils;
import de.mw.mwdata.core.ofdb.SortKey;
import de.mw.mwdata.core.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.core.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.core.ofdb.daos.IMenueDao;
import de.mw.mwdata.core.ofdb.daos.IOfdbDao;
import de.mw.mwdata.core.ofdb.def.CRUD;
import de.mw.mwdata.core.ofdb.def.ConfigOfdb;
import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.core.ofdb.def.OfdbPropMapper;
import de.mw.mwdata.core.ofdb.domain.AnsichtDef;
import de.mw.mwdata.core.ofdb.domain.AnsichtOrderBy;
import de.mw.mwdata.core.ofdb.domain.AnsichtTab;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.core.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.core.ofdb.domain.IMenue;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.ofdb.domain.TabDef;
import de.mw.mwdata.core.ofdb.exception.OfdbFormatException;
import de.mw.mwdata.core.ofdb.exception.OfdbInvalidCheckException;
import de.mw.mwdata.core.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingObjectException;
import de.mw.mwdata.core.ofdb.exception.OfdbNullValueException;
import de.mw.mwdata.core.ofdb.exception.OfdbUniqueConstViolationException;
import de.mw.mwdata.core.ofdb.intercept.AbstractCrudChain;
import de.mw.mwdata.core.ofdb.query.DefaultOfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.OfdbQueryBuilder;
import de.mw.mwdata.core.ofdb.query.QueryValue;
import de.mw.mwdata.core.ofdb.query.ValueType;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.core.utils.ITree;
import de.mw.mwdata.ordb.query.OfdbOrderSet;
import de.mw.mwdata.ordb.query.OfdbQueryModel;
import de.mw.mwdata.ordb.query.OfdbWhereRestriction;
import de.mw.mwdata.ordb.query.OperatorEnum;

/**
 * Main Service for all ofdb-relevant operations. It caches the ofdb-data for every registerd table.
 *
 * @author mwilbers
 * @version 1.0
 */
public class OfdbService extends AbstractCrudChain implements IOfdbService {

	// TODO: 1. build own logging-component
	// 2. Idee: MBeans anlegen für ausgabe der ofdb-informationen
	private static final Logger	LOGGER	= LoggerFactory.getLogger( OfdbService.class );

	private IOfdbDao			ofdbDao;
	private OfdbCacheManager	ofdbCacheManager;
	private IMenueDao			menueDao;
	private ICrudService		crudService;

	protected IOfdbDao getOfdbDao() {
		return this.ofdbDao;
	}

	public void setOfdbDao( final IOfdbDao ofdbDao ) {
		this.ofdbDao = ofdbDao;
	}

	public void setOfdbCacheManager( final OfdbCacheManager ofdbCacheManager ) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	public void setMenueDao( final IMenueDao menueDao ) {
		this.menueDao = menueDao;
	}

	public void setCrudService( final ICrudService crudService ) {
		this.crudService = crudService;
	}

	@Override
	public ITree findMenues() {
		return this.menueDao.findMenues();
		// return this.getOfdbDao().findMenues();
	}

	@Override
	public IAnsichtDef findAnsichtById( final long ansichtId ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMenue findMenuById( final long menueId ) {
		return this.menueDao.findById( menueId );
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public IAnsichtDef findAnsichtByUrlPath( final String urlPath ) {

		List<ViewConfigHandle> viewConfigs = this.ofdbCacheManager.getRegisteredViewConfigs();
		for ( ViewConfigHandle handle : viewConfigs ) {
			String path = handle.getViewDef().getUrlPath();
			if ( path.equals( urlPath ) ) {
				return handle.getViewDef();
			}
		}

		OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
		String sql = b.selectTable( ConfigOfdb.T_VIEWDEF, "v" ).fromTable( ConfigOfdb.T_VIEWDEF, "v" )
				.andWhereRestriction( "v", "urlPath", OperatorEnum.Eq, urlPath, ValueType.STRING ).buildSQL();
		List<IEntity[]> result = this.executeQuery( sql );

		if ( CollectionUtils.isEmpty( result ) ) {
			String message = MessageFormat.format( "ViewDef by urlpath {0} is not found. ", urlPath );
			throw new OfdbMissingObjectException( message );
		}

		Object o = result.get( 0 );
		return (AnsichtDef) o;
	}

	@Override
	public IAnsichtDef findAnsichtByName( final String viewName ) {

		if ( this.ofdbCacheManager.isViewRegistered( viewName ) ) {
			ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig( viewName );
			return viewHandle.getViewDef();
		}

		// ... 2 Varianten des QueryBuilders.
		// 1. SimpleQueryBuilder (siehe hier unten, kein ViewHandle, keine Ofdb-Objekte daher whereREstriction mit '')
		// 2. OfdbQueryBuilder ( siehe buildFilteredSql() mit Aufbau über das QueryModel )

		OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
		String sql = b.selectTable( ConfigOfdb.T_VIEWDEF, "v" ).fromTable( ConfigOfdb.T_VIEWDEF, "v" )
				.andWhereRestriction( "v", "name", OperatorEnum.Eq, viewName, ValueType.STRING ).buildSQL();

		List<IEntity[]> result = this.executeQuery( sql );

		if ( null == result ) {
			String message = MessageFormat.format( "ViewDef {0} is not found. ", viewName );
			throw new OfdbMissingObjectException( message );
		}

		Object o = result.get( 0 );
		return (AnsichtDef) o;

	}

	@Override
	public String buildSQL( final String viewName, final List<SortKey> sortKeys ) {

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig( viewName );
		OfdbQueryModel queryModel = viewHandle.getQueryModel();

		addOrderToQueryModel( queryModel, viewName, sortKeys );
		return buildSQLInternal( queryModel, viewName, false );
	}

	private String buildSQLInternal( final OfdbQueryModel queryModel, final String viewName, final boolean setCount ) {

		// 1.
		// ... hier refactoring, dass man QueryModel als argument übergeben kann,
		// alternativ methode buildDefaultSql oder buildCachedSql, das den ofdbCacheManger
		// mit querymodel verwendet
		//
		// 2. in FXOfdbInterceptor: eine methode convertDatatypeToHsql in ofdbService extrahieren
		// und dort verwenden

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig( viewName );
		OfdbQueryBuilder queryBuilder = new DefaultOfdbQueryBuilder();
		ITabDef fromTabDef = queryModel.getMainTable();

		if ( setCount ) {
			queryBuilder.setCount( true );
		} else {
			queryBuilder.selectTable( getSimpleName( fromTabDef ), fromTabDef.getAlias() );
		}

		// add select-alias-columns
		for ( ITabSpeig aliasTabSpeig : queryModel.getAlias( viewName ) ) {
			// ... hier funktioniert das select ..., BenB.name noch nicht
			String aliasPropName = mapTabSpeig2Property( aliasTabSpeig );
			queryBuilder.selectAlias( aliasTabSpeig.getTabDef().getAlias(), aliasPropName );
		}

		// build from-part
		queryBuilder.fromTable( getSimpleName( fromTabDef ), fromTabDef.getAlias() );

		for ( IAnsichtTab ansichtTab : queryModel.getJoinedViews() ) {
			ViewConfigHandle joinedViewHandle = this.ofdbCacheManager
					.getViewConfig( ansichtTab.getAnsichtDef().getName() );

			// AnsichtTab ansichtTab = ansichtTabEntry.getValue();
			if ( ansichtTab.getJoinTyp().equalsIgnoreCase( "x" ) ) {
				continue;
			}

			// join tables
			ITabDef joinTabDef = ansichtTab.getTabDef();
			queryBuilder.joinTable( getSimpleName( joinTabDef ), joinTabDef.getAlias() );

			// FIXME: validation-check that ansichTab.join1spalteakey = ansichtSpalte.spalteakey
			ITabSpeig tabSpeig1 = joinedViewHandle.findTabSpeigByTabAKeyAndSpalteAKey( joinTabDef.getName(),
					ansichtTab.getJoin1SpalteAKey() );
			String join1propName = mapTabSpeig2Property( tabSpeig1 );

			// FIXME: validation-check that ansichTab.join2spalteakey = ansichtSpalte.spalteakey
			ITabSpeig tabSpeig2 = viewHandle.findTabSpeigByTabAKeyAndSpalteAKey( fromTabDef.getName(),
					ansichtTab.getJoin2SpalteAKey() );
			String join2propName = mapTabSpeig2Property( tabSpeig2 );

			// add where-restrictions
			queryBuilder.whereJoin( joinTabDef.getAlias(), join1propName, fromTabDef.getAlias(), join2propName );

		}

		// add where-restrictions to queryBuilder
		for ( OfdbWhereRestriction whereRes : queryModel.getWhereRestrictions() ) {

			ITabSpeig whereTabSpeig = whereRes.getWhereTabSpeig();
			String whereColumnPropName = mapTabSpeig2Property( whereTabSpeig );

			QueryValue wValue = convertValueToQueryValue( whereRes.getWhereValue(), whereTabSpeig );

			queryBuilder.andWhereRestriction( whereRes.getWhereTabDef().getAlias(), whereColumnPropName,
					whereRes.getWhereOperator(), wValue.getData(), wValue.getType() );

		}

		// add order-items to queryBuilder
		for ( OfdbOrderSet orderSet : queryModel.getOrderSet() ) {

			String propName = mapTabSpeig2Property( orderSet.getOrderTabSpeig() );
			queryBuilder.orderBy( orderSet.getOrderTabDef().getAlias(), propName, orderSet.getOrderDirection() );
		}

		return queryBuilder.buildSQL();

	}

	private QueryValue convertValueToQueryValue( final Object value, final ITabSpeig whereTabSpeig ) {

		QueryValue wValue = null;

		switch ( whereTabSpeig.getDbDatentyp() ) {
			case STRING: {
				wValue = new QueryValue( value.toString(), ValueType.STRING );
				break;
			}
			case BOOLEAN: {
				if ( value.equals( Boolean.TRUE ) ) {
					wValue = new QueryValue( Integer.valueOf( Constants.SYS_VAL_TRUE ).toString(), ValueType.NUMBER );
					break;
				} else if ( value.equals( Boolean.FALSE ) ) {
					wValue = new QueryValue( Integer.valueOf( Constants.SYS_VAL_FALSE ).toString(), ValueType.NUMBER );
					break;
				}
			}
			case ENUM: {

				if ( value instanceof IFxEnum ) {
					// note: whereRes.getWhereValue() contains the enum-description-value here

					IFxEnum mwEnum = (IFxEnum) value;
					// ... 1. falsch: hier muss VONBIS von whereRes.getWhereValue() gezogen werden, aber wie ?
					// 2. Prüfung in registerOfdb(): wenn DBTYPE ENUM dann prüfung, dass mapping auf IMWEnum geht
					wValue = new QueryValue( mwEnum.getName().toString(), ValueType.STRING );
				} else {
					String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, "missingMWEnumMapping",
							whereTabSpeig.getTabDef().getName(), whereTabSpeig.getSpalte() );
					throw new OfdbInvalidConfigurationException( msg );
				}
				break;
			}
			default: {
				wValue = new QueryValue( value.toString(), ValueType.NUMBER );
				break;
			}
		}

		return wValue;
	}

	@Override
	public String buildFilteredSQL( final String viewName, final EntityTO<? extends AbstractMWEntity> filterEntityTO,
			final List<SortKey> sortKeys ) {

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig( viewName );
		OfdbQueryModel queryModel = viewHandle.getQueryModel();
		queryModel.resetWhereRestrictions();

		List<OfdbField> ofFields = viewHandle.getOfdbFieldList();
		for ( OfdbField ofField : ofFields ) {

			ITabSpeig tabSpeig = ofField.getTabSpeig();

			if ( ofField.isVerdeckenDurchSpalte() ) {

				MapValue whereMapValue = filterEntityTO.getMap().get( ofField.getItemKey() );
				boolean toFilter = (!StringUtils.isBlank( whereMapValue.getValue() ));

				if ( toFilter ) {

					IAnsichtSpalte ansichtSpalte = ofField.getAnsichtSpalte();
					ITabSpeig suchTabSpeig = viewHandle.findTabSpeigByTabAKeyAndSpalteAKey(
							ansichtSpalte.getVerdeckenDurchTabAKey(), ansichtSpalte.getVerdeckenDurchSpalteAKey() );

					queryModel.addWhereRestriction( suchTabSpeig.getTabDef(), suchTabSpeig, OperatorEnum.Eq,
							whereMapValue.getValue() );

				}

			} else {

				boolean toFilter = false;

				Object entityValue = null;
				try {
					OfdbPropMapper propMapper = this.ofdbCacheManager.findPropertyMapperByTabSpeig( tabSpeig );
					if ( null == propMapper ) {
						// FIXME: should not be happen. Tritt auf bei Namensspalten, die langfristg durch Id /
						// verdeckenDurch ersetzt werden sollten (z.B. TabDef.bereich)
						throw new OfdbMissingMappingException( "Given TabSpeig not mapped by property." );
					}
					entityValue = this.getOfdbDao().getEntityValue( filterEntityTO.getItem(),
							propMapper.getPropertyIndex() );

					toFilter = (null != entityValue && !StringUtils.isBlank( entityValue.toString() ));
				} catch ( OfdbMissingMappingException e ) {
					LOGGER.warn(
							"Property not mapped: Table " + tabSpeig.getName() + ", column " + tabSpeig.getSpalte() );

				}
				if ( toFilter ) {
					queryModel.addWhereRestriction( tabSpeig.getTabDef(), tabSpeig, OperatorEnum.Eq, entityValue );
				}

			}

		}

		addOrderToQueryModel( queryModel, viewName, sortKeys );
		String sqlFiltered = buildSQLInternal( queryModel, viewName, false );

		return sqlFiltered;

	}

	@Override
	public String buildSQLCount( final String viewName ) {

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig( viewName );
		OfdbQueryModel queryModel = viewHandle.getQueryModel();
		return buildSQLInternal( queryModel, viewName, true );
	}

	// FIXME: viewName argument necessary here ? and viewHandle ?
	private void addOrderToQueryModel( final OfdbQueryModel queryModel, final String viewName,
			final List<SortKey> sortKeys ) {

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig( viewName );

		queryModel.resetOrderSet();

		List<OfdbField> ofFields = viewHandle.getOfdbFieldList();
		for ( OfdbField ofField : ofFields ) {

			ITabSpeig tabSpeig = ofField.getTabSpeig();

			if ( ofField.isVerdeckenDurchSpalte() ) {

				SortKey sortKey = OfdbUtils.findSortKeyByColumnName( sortKeys, ofField.getPropName() );
				boolean toSort = (null != sortKey);

				if ( toSort ) {

					IAnsichtSpalte ansichtSpalte = ofField.getAnsichtSpalte();
					ITabSpeig suchTabSpeig = viewHandle.findTabSpeigByTabAKeyAndSpalteAKey(
							ansichtSpalte.getVerdeckenDurchTabAKey(), ansichtSpalte.getVerdeckenDurchSpalteAKey() );

					// SortKey key = OfdbUtils.findSortKeyByColumnName( sortKeys, ofField.getPropName() );
					queryModel.addOrderSet( suchTabSpeig.getTabDef(), suchTabSpeig,
							sortKey.getSortDirection().getName() );

				}

			} else {

				SortKey sortKey = OfdbUtils.findSortKeyByColumnName( sortKeys, ofField.getPropName() );
				boolean toSort = (null != sortKey);
				if ( toSort ) {
					queryModel.addOrderSet( tabSpeig.getTabDef(), tabSpeig, sortKey.getSortDirection().getName() );
				}

			}

		} // end for ofFields

	}

	@Override
	public String mapTabSpeig2Property( final ITabSpeig tabSpeig ) {
		Map<String, OfdbPropMapper> propertyMap = this.ofdbCacheManager
				.getPropertyMap( tabSpeig.getTabDef().getName() );
		return propertyMap.get( tabSpeig.getSpalte().toUpperCase() ).getPropertyName();
	}

	/**
	 *
	 * @param tabDef
	 * @return the simple name of the entity given by the fullclassname of the TabDef-column
	 *
	 *         FIXME: duplicated method in ViewConfigFactory -> typical method for util-class
	 *
	 */
	private String getSimpleName( final ITabDef tabDef ) {

		if ( null == tabDef ) {
			return StringUtils.EMPTY;
		}

		String simpleName = ClassNameUtils.getSimpleClassName( tabDef.getFullClassName() );
		if ( null == simpleName ) {
			// FIXME: throw exception: better: do validation in registration-method
		}
		return simpleName;

	}

	private Object getOfdbDefault( final ITabSpeig tabSpeig ) throws OfdbNullValueException {

		Object result = null;
		switch ( tabSpeig.getDbDatentyp() ) {
			case BOOLEAN:
				if ( null != tabSpeig.getDefaultWert() ) {
					result = FxBooleanType.defBoolean( (String) tabSpeig.getDefaultWert() );
				} else {
					if ( tabSpeig.getEingabeNotwendig() ) {

						// TODO: replace with logical check in TabDefService:
						// if BOOLEAN and eingabeNotwendig == null : -> error
						String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, "illegalOfdbNullValue",
								tabSpeig.getTabDef().getName(), tabSpeig.getSpalte() );
						throw new OfdbNullValueException( msg );
					}
				}
				break;
			case STRING:
				if ( null != tabSpeig.getDefaultWert() ) {
					if ( Constants.MWDATADEFAULT.USERID.getName().equals( tabSpeig.getDefaultWert() ) ) {
						result = Constants.SYS_USER_DEFAULT;
					} else {
						result = tabSpeig.getDefaultWert();
					}
				} else {
					if ( tabSpeig.getEingabeNotwendig() ) {

						// TODO: replace with logical check in TabDefService:
						// if BOOLEAN and eingabeNotwendig == null : -> error
						String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, "illegalOfdbNullValue",
								tabSpeig.getTabDef().getName(), tabSpeig.getSpalte() );
						throw new OfdbNullValueException( msg );
					}
				}
				break;
			case LONGINTEGER:
				if ( null != tabSpeig.getDefaultWert() ) {
					try {
						result = Long.parseLong( (String) tabSpeig.getDefaultWert() );
					} catch ( NumberFormatException ne ) {
						String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, "invalidDefaultValue",
								tabSpeig.getTabDef().getName(), tabSpeig.getSpalte() );
						throw new OfdbFormatException( msg, ne );
					}
				} else {
					if ( tabSpeig.getEingabeNotwendig() ) {

						// TODO: replace with logical check in TabDefService:
						// if BOOLEAN and eingabeNotwendig == null : -> error
						String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, "illegalOfdbNullValue",
								tabSpeig.getTabDef().getName(), tabSpeig.getSpalte() );
						throw new OfdbNullValueException( msg );
					}
				}
				break;
			case ENUM: {
				if ( null != tabSpeig.getDefaultWert() ) {
					result = tabSpeig.getDefaultWert();

				} else {
					if ( tabSpeig.getEingabeNotwendig() ) {

						// TODO: replace with logical check in TabDefService:
						// if BOOLEAN and eingabeNotwendig == null : -> error
						String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, "illegalOfdbNullValue",
								tabSpeig.getTabDef().getName(), tabSpeig.getSpalte() );
						throw new OfdbNullValueException( msg );
					}
				}
				break;
			}
			case DATE: {
				if ( Constants.MWDATADEFAULT.NOW.getName().equals( tabSpeig.getDefaultWert() ) ) {
					result = new Date();
				}
				break;
			}
			default:

				break;
		}

		return result;
	}

	@Override
	public void presetDefaultValues( final AbstractMWEntity entity ) {

		// AbstractMWEntity entity = changedEntity.getEntity();
		String urlPath = ClassNameUtils.convertClassNameToUrlPath( entity.getClass().getName() );
		IAnsichtDef viewDef = this.findAnsichtByUrlPath( urlPath );
		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig( viewDef.getName() );
		IAnsichtTab viewToTable = viewHandle.getMainAnsichtTab();
		String tableName = viewToTable.getTabDef().getName();

		List<ITabSpeig> tabSpeigs = viewHandle.getTableProps( viewToTable.getTabDef() );
		for ( ITabSpeig tabSpeig : tabSpeigs ) {

			Object value = null;
			try {

				OfdbPropMapper propMapper = this.ofdbCacheManager.findPropertyMapperByTabSpeig( tabSpeig );
				value = this.getOfdbDao().getEntityValue( entity, propMapper.getPropertyIndex() );
				if ( null == value ) {
					value = getOfdbDefault( tabSpeig );
					if ( null != value ) {
						Object result = this.getOfdbDao().setEntityValue( entity, value, tabSpeig, propMapper );
						// changedEntity.setNewValue( propMapper.getPropertyName(), result );
						// ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig( viewName );
						// String mainTableName = viewHandle.getMainAnsichtTab().getTabDef().getName();
						// this.getMainAnsichtTab( viewName ).getTabDef().getName();
						LOGGER.info( "Default Value set for Table " + tableName + ", TabSpeig " + tabSpeig.getSpalte()
								+ ", defaultvalue: " + value );
					}

				}

			} catch ( OfdbMissingMappingException e ) {
				LOGGER.error( e.getLocalizedMessage() );
			}

		}

	}

	@Override
	public List<OfdbField> initializeOfdbFields( final String viewName, final CRUD crud ) {

		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig( viewName );
		List<OfdbField> ofFields = viewHandle.getOfdbFieldList();

		for ( OfdbField ofField : ofFields ) {
			ofField.refreshEditMode( crud );
		}

		return ofFields;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AnsichtDef> loadViewsForRegistration( final String applicationContextPath ) {
		return this.getOfdbDao().loadViewsForRegistration( applicationContextPath );

		// folgendes statement tut nicht:
		// select ansichtdef0_.DSID as DSID7_, ansichtdef0_.angelegtAm as angelegtAm7_, ansichtdef0_.angelegtVon as
		// angelegt3_7_, ansichtdef0_.ofdb as ofdb7_,
		// ansichtdef0_.system as system7_, ansichtdef0_.APPCONTEXTPATH as APPCONTE6_7_, ansichtdef0_.BEARBEITEN as
		// BEARBEITEN7_,
		// ansichtdef0_.BEREICHSID as BEREICHSID7_, ansichtdef0_.BEZEICHNUNG as BEZEICHN8_7_, ansichtdef0_.ENTFERNEN as
		// ENTFERNEN7_,
		// ansichtdef0_.FILTER as FILTER7_, ansichtdef0_.HINZUFUEGEN as HINZUFU11_7_, ansichtdef0_.LESEN as LESEN7_,
		// ansichtdef0_.ANSICHT as ANSICHT7_,
		// ansichtdef0_.REIHENFOLGE as REIHENF14_7_, ansichtdef0_.SORTIEREN as SORTIEREN7_, ansichtdef0_.URLPATH as
		// URLPATH7_
		// from FX_AnsichtDef_K ansichtdef0_
		// where 1=1
		// and ansichtdef0_.APPCONTEXTPATH='admin'
		// and (ansichtdef0_.URLPATH is not null)
		// order by ansichtdef0_.REIHENFOLGE asc;

	}

	// @Override
	// public List<IEntity[]> loadView( final String viewName, final List<SortKey>... sortKeys ) {
	//
	// List<SortKey> cols = prepareSortColumns( viewName, sortKeys );
	// String sql = buildSQL( viewName, cols );
	// List<IEntity[]> result = this.getOfdbDao().executeQuery( sql );
	//
	// return result;
	//
	// }

	// protected List<SortKey> prepareSortColumns( final String viewName, final List<SortKey>... sortKeys ) {
	// List<SortKey> cols = new ArrayList<SortKey>();
	//
	// if ( ArrayUtils.isEmpty( sortKeys ) ) {
	//
	// ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig( viewName );
	// List<IAnsichtOrderBy> ansichtOrderList = viewHandle.getViewOrders();
	//
	// for ( IAnsichtOrderBy orderBy : ansichtOrderList ) {
	//
	// ITabSpeig tabSpeig = viewHandle.findTabSpeigByAnsichtOrderBy( orderBy );
	// String propName = mapTabSpeig2Property( tabSpeig );
	//
	// SORTDIRECTION dir = (orderBy.getAufsteigend() ? SORTDIRECTION.ASC : SORTDIRECTION.DESC);
	// cols.add( new SortKey( propName, dir.getName() ) );
	// }
	//
	// } else {
	// cols = sortKeys[0];
	// }
	//
	// return cols;
	// }

	@Override
	public List<IEntity[]> executeQuery( final String sql ) {
		return this.getOfdbDao().executeQuery( sql );
	}

	// @Override
	// public long executeCountQuery( final String sqlCount ) {
	// return this.getOfdbDao().executeCountQuery( sqlCount );
	// }

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean isEmpty( final AbstractMWEntity entity ) throws OfdbMissingMappingException {

		// String fullClassName = entity.getClass().getName();
		TabDef tabDef = findTableDefByEntity( entity );

		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName( tabDef.getName() );
		List<ITabSpeig> tabSpeigs = viewHandle.getTableProps( tabDef );

		boolean isEmpty = true;
		for ( ITabSpeig tabSpeig : tabSpeigs ) {
			OfdbPropMapper propMapper = this.ofdbCacheManager.findPropertyMapperByTabSpeig( tabSpeig );
			if ( null == propMapper ) {
				// FIXME: should not happen. happens e.g. for TabDef.bereich but should be removed in future because of
				// mapping bereichId / BenutzerBereich.name
				LOGGER.warn( "TabSpeig not mapped: " + tabSpeig.getTabDef().getName() + "." + tabSpeig.getSpalte() );
				continue;

			}

			Object entityValue = this.getOfdbDao().getEntityValue( entity, propMapper.getPropertyIndex() );

			if ( entityValue instanceof String ) {
				if ( !StringUtils.isBlank( (String) entityValue ) ) {
					return false;
				}
			} else {
				if ( null != entityValue ) {
					return false;
				}
			}

		}

		return isEmpty;
	}

	@Override
	public List<AnsichtTab> findAnsichtTabByAnsichtId( final long ansichtId ) {
		return this.getOfdbDao().findAnsichtTabAnsichtId( ansichtId );
	}

	@Override
	public List<ITabSpeig> loadTablePropListByTableName( final String table ) {
		return this.getOfdbDao().findTabSpeigByTable( table );
	}

	@Override
	public ITabSpeig loadTablePropByTableName( final String tableName, final String columnName ) {

		List<ITabSpeig> tableProps = loadTablePropListByTableName( tableName );
		for ( ITabSpeig tabSpeig : tableProps ) {
			if ( columnName.equals( tabSpeig.getSpalte() ) ) {
				return tabSpeig;
			}
		}

		return null;
	}

	@Override
	public Map<String, OfdbPropMapper> initializeMapping( final Class<? extends AbstractMWEntity> type,
			final String tableName, final List<ITabSpeig> tabSpeigs ) {

		Map<String, OfdbPropMapper> propMap = this.getOfdbDao().initializeMapper( type, tableName );

		for ( ITabSpeig tabSpeig : tabSpeigs ) {
			if ( tabSpeig.getPrimSchluessel() ) {
				// TODO: how can we get the propertyname of the hibernate-@id-column ? FIXME: this does not work for
				// combined PS (e.g. two columns or more )
				OfdbPropMapper propMapper = propMap.get( tabSpeig.getSpalte().toUpperCase() );

				if ( null == propMapper ) {
					throw new OfdbInvalidConfigurationException( "Invalid Property Mapping on primary key column "
							+ tabSpeig.getSpalte().toUpperCase() + ". No property found on entity "
							+ type.getClass().getName() + " with this columnname." );
				}

				// propMapper.setPropertyName( Constants.SYS_PROP_ID );
			}
		}

		return propMap;

	}

	@Override
	public List<AnsichtOrderBy> findAnsichtOrderByAnsichtId( final long ansichtId ) {
		return this.getOfdbDao().findAnsichtOrderByAnsichtId( ansichtId );
	}

	@Override
	public Map<String, IAnsichtSpalte> findAnsichtSpaltenMapByAnsichtId( final long ansichtId ) {
		return this.getOfdbDao().findAnsichtSpaltenMapByAnsicht( ansichtId );
	}

	@Override
	public List<Object> getListOfValues( final OfdbField ofField, final ITabSpeig tabSpeig,
			final List<IAnsichtOrderBy> ansichtOrderList, final Class<? extends AbstractMWEntity> type ) {

		// if no hibernate-specific entity-property found
		if ( !ofField.isMapped() ) {
			return Collections.emptyList();
		}

		if ( tabSpeig.isEnum() ) {

			String fullClassName = tabSpeig.getTabDef().getFullClassName();
			Class<? extends AbstractMWEntity> entityClassType = null;

			try {
				entityClassType = ClassNameUtils.getClassType( fullClassName );
			} catch ( ClassNotFoundException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return this.getOfdbDao().getEnumValues( entityClassType, ofField.getPropName() );
		} else if ( null != type ) {
			Map<String, String> sortColumns = convertAnsichtOrderListToSortColumns( ansichtOrderList );
			List<AbstractMWEntity> lovs = this.crudService.findAll( type, sortColumns );

			return ListUtils.unmodifiableList( lovs );
		}

		return Collections.emptyList();

	}

	/**
	 * Converts the list of AnsichtOrderBy-elements to a map with key = spalte and value = asc / desc
	 *
	 * @param viewOrderList
	 * @return
	 */
	private Map<String, String> convertAnsichtOrderListToSortColumns( final List<IAnsichtOrderBy> viewOrderList ) {

		Map<String, String> sortColumns = new HashMap<String, String>();
		if ( null != viewOrderList && viewOrderList.size() > 0 ) {
			for ( IAnsichtOrderBy ansichtOrderBy : viewOrderList ) {

				ViewConfigHandle viewHandle = this.ofdbCacheManager
						.getViewConfig( ansichtOrderBy.getAnsichtTab().getAnsichtDef().getName() );
				ITabSpeig tabProp = viewHandle.findTabSpeigByAnsichtOrderBy( ansichtOrderBy );
				OfdbPropMapper propMapper = this.ofdbCacheManager.findPropertyMapperByTabSpeig( tabProp );
				// OfdbPropMapper propMapper = this.getOfdbDao().mapTabSpeigToProperty( tabProp );
				String asc = (ansichtOrderBy.getAufsteigend() ? "asc" : "desc");

				sortColumns.put( propMapper.getPropertyName(), asc );
			}

		}

		return sortColumns;

	}

	// @Override
	@Override
	public void doChainActionsBeforeCheck( final AbstractMWEntity entity, final CRUD crud ) {

		presetDefaultValues( entity );

		if ( null != this.nextChainItem ) {
			this.nextChainItem.doChainActionsBeforeCheck( entity, crud );
		}
	}

	@Override
	public TabDef findTableDefByEntity( final AbstractMWEntity entity ) {
		String fullClassName = entity.getClass().getName();
		return this.getOfdbDao().findTableDefByFullClassName( fullClassName ); // (TabDef) o;
	}

	public void checkAllUniques( final AbstractMWEntity entity, final CRUD crud )
			throws OfdbUniqueConstViolationException {

		TabDef tabDef = findTableDefByEntity( entity );
		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName( tabDef.getName() );
		Map<String, OfdbPropMapper> propMap = viewHandle.getPropertyMap();

		for ( Map.Entry<String, OfdbPropMapper> entry : propMap.entrySet() ) {
			OfdbPropMapper propMapper = entry.getValue();

			if ( propMapper.isAssociationType() ) {
				continue;
			}

			// // if no changes on property no unique check
			// if ( !changedEntity.hasChanged( propertyName ) ) {
			// continue;
			// }

			ITabSpeig uniqueTabSpeig = this.ofdbCacheManager.findTablePropByProperty( tabDef.getName(),
					propMapper.getPropertyName() );

			if ( null == uniqueTabSpeig ) {
				continue; // e.g. TabSpeig.getName() runs here ...
			}

			checkUnique( uniqueTabSpeig, entity );

		} // end for

	}

	@Override
	public void checkUnique( final ITabSpeig uniqueTabSpeig, final AbstractMWEntity entity )
			throws OfdbUniqueConstViolationException {

		ITabDef tabDef = uniqueTabSpeig.getTabDef();
		if ( uniqueTabSpeig.isEindeutig() ) {

			// 4. vergleichen mit den dirty feldern und unique prüfung durchführen
			// ViewConfigHandle viewHandle = ofdbCacheManager.findViewConfigByTableName( tabDef.getName() );
			UniqueTabSpeigBucket uniqueMap = new UniqueTabSpeigBucket(
					this.ofdbCacheManager.findRegisteredTabSpeigs( tabDef.getName() ) );
			List<ITabSpeig> uniqueTabSpeigs = uniqueMap.getTabSpeigsByUniqueIdentifier( uniqueTabSpeig.getEindeutig() );

			Map<String, OfdbPropMapper> propMap = this.ofdbCacheManager.getPropertyMap( tabDef.getName() );

			OfdbQueryBuilder b = new DefaultOfdbQueryBuilder();
			b.selectTable( entity.getClass().getSimpleName(), "tAlias" ).fromTable( entity.getClass().getSimpleName(),
					"tAlias" );

			for ( ITabSpeig tabSpeig : uniqueTabSpeigs ) {
				// FIXME: if more than one unique prop, than this check is only needed for one of all

				Object entityValue = null;
				OfdbPropMapper propMapper = propMap.get( tabSpeig.getSpalte() );
				String uniquePropNameItem = propMapper.getPropertyName();
				try {
					entityValue = this.ofdbDao.getEntityValue( entity, propMapper.getPropertyIndex() );
				} catch ( OfdbMissingMappingException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				QueryValue queryValue = convertValueToQueryValue( entityValue, tabSpeig );
				b = b.andWhereRestriction( "tAlias", uniquePropNameItem, OperatorEnum.Eq, queryValue.getData(),
						ValueType.STRING );
			}

			String sql = b.buildSQL();
			List<IEntity[]> results = executeQuery( sql );
			if ( results.size() > 0 ) {
				String message = "Unique violation. TableDef: " + tabDef.getName() + ", tableProperty: "
						+ uniqueTabSpeig.getSpalte();
				throw new OfdbUniqueConstViolationException( message );
			}

		}

	}

	// @Override
	@Override
	public void doChainCheck( final AbstractMWEntity entity, final CRUD crud ) throws OfdbInvalidCheckException {

		try {
			checkAllUniques( entity, crud );

		} catch ( OfdbUniqueConstViolationException e ) {
			throw new OfdbInvalidCheckException( "Unique check violation.", e );
		}

		if ( null != this.nextChainItem ) {
			this.nextChainItem.doChainCheck( entity, crud );
		}

	}

}
