package de.mw.mwdata.core.ofdb.cache;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.LocalizedMessages;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.ofdb.cache.ViewConfiguration.Builder;
import de.mw.mwdata.core.ofdb.def.CRUD;
import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.core.ofdb.def.OfdbFieldComparator;
import de.mw.mwdata.core.ofdb.def.OfdbPropMapper;
import de.mw.mwdata.core.ofdb.domain.AnsichtOrderBy;
import de.mw.mwdata.core.ofdb.domain.AnsichtTab;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.core.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.ofdb.exception.OfdbInvalidConfigurationException;
import de.mw.mwdata.core.ofdb.query.DefaultOfdbQueryModel;
import de.mw.mwdata.core.ofdb.service.IOfdbService;
import de.mw.mwdata.core.ofdb.validate.OfdbValidatable;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.ordb.query.OfdbQueryModel;

public class ViewConfigFactoryBean implements ViewConfigFactory {

	private static final Logger	LOGGER	= LoggerFactory.getLogger( ViewConfigFactoryBean.class );

	// FIXME: replace autwiring by xml-injections

	@Autowired
	private OfdbCacheManager	ofdbCacheManager;

	@Autowired
	private OfdbValidatable		ofdbValidator;

	@Autowired
	private IOfdbService		ofdbService;

	private void handleValidationErrors( final ViewConfigValidationResultSet resultSet ) {

		StringBuilder b = new StringBuilder();
		for ( ViewConfigValidationResult result : resultSet ) {
			LOGGER.error( result.toString() );
			b.append( result.toString() ).append( "\n" );
		}

		if ( resultSet.hasErrors() ) {
			String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, "invalidConfiguration" );
			// abort directly: no further validation-process needed
			b.append( msg );
			throw new OfdbInvalidConfigurationException( b.toString() );
		}
	}

	@Override
	public ViewConfigHandle createViewConfiguration( final String viewName ) {

		// initialize AnsichtDef
		IAnsichtDef ansicht = this.ofdbService.findAnsichtByName( viewName );
		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();

		// initialize AnsichtDef
		ViewConfigValidationResultSet partSet = this.ofdbValidator.isAnsichtValid( ansicht );
		set.merge( partSet );
		ViewConfiguration.Builder builder = new Builder( ansicht );

		// check AnsichtTab - entries
		List<AnsichtTab> ansichtTabList = this.ofdbService.findAnsichtTabByAnsichtId( ansicht.getId() );
		if ( CollectionUtils.isEmpty( ansichtTabList ) ) {
			set.addValidationResult( "invalidOfdbConfig.FX_AnsichtTab.missingAnsichtTabEntry", ansicht.getName() );
			// if no AnsichtTab-entries show errors and abort
			this.handleValidationErrors( set );
		}
		partSet = this.ofdbValidator.isAnsichtTabListValid( ansicht, ansichtTabList );
		set.merge( partSet );
		if ( set.hasErrors() ) {
			this.handleValidationErrors( set );
		}

		// before validating ansichtTab-objects do validation to underlying tabDefs and tabSpeigs
		String tableName = null;
		Map<String, OfdbPropMapper> mainPropertyMap = new HashMap<String, OfdbPropMapper>();
		for ( IAnsichtTab ansichtTab : ansichtTabList ) {

			tableName = ansichtTab.findTableName(); // this.getTableNameByViewTab( ansichtTab );

			// check if table is already registered. possible because one table can belong to several views
			ITabDef tabDef = this.ofdbCacheManager.findRegisteredTableDef( tableName );
			List<ITabSpeig> tabSpeigs = null;
			tabDef = ansichtTab.getTabDef();
			tabSpeigs = this.ofdbService.loadTablePropListByTableName( tableName );

			partSet = this.ofdbValidator.isTableValid( tabDef );
			set.merge( partSet );
			if ( set.hasErrors() ) {
				this.handleValidationErrors( set );
			}

			if ( CollectionUtils.isEmpty( tabSpeigs ) ) {
				String msg = MessageFormat.format( "No TabSpeigs found for tablename {0} in ViewConfiguration.",
						tableName );
				LOGGER.warn( msg );
			}
			partSet = validateTabSpeigList( tabSpeigs );
			set.merge( partSet );
			if ( set.hasErrors() ) {
				handleValidationErrors( set );
			}

			builder.addTableDef( tabDef );
			builder.addTableProps( tabDef, tabSpeigs );

			// // initialize TabBez
			// List<TabBez> tabBez = this.ofdbDao.findTabBezByTable( tableName );
			// // FIXME: do validation-check of all tabBezs

			builder.addViewTab( ansichtTab );

			tableName = ansichtTab.getTabDef().getName();

			// if table props are called for first time, than do initialize properties and add them to cache
			if ( CollectionUtils.isEmpty( this.ofdbCacheManager.getPropertyMap( tableName ) ) ) {

				Class<? extends AbstractMWEntity> entityClassType = checkFullClassName( ansichtTab.getTabDef() );
				Map<String, OfdbPropMapper> propertyMap = this.ofdbService.initializeMapping( entityClassType,
						tableName, builder.getTableProps( ansichtTab.getTabDef() ) );

				checkMappingTabSpeig2Property( tabSpeigs, propertyMap );

				builder.setPropertyMap( propertyMap );

				if ( builder.buildHandle().getMainAnsichtTab().getTabDef().equals( tabDef ) ) {
					mainPropertyMap = propertyMap;
				}

			}

		} // end for each AnsichtTab

		// initialize ansichtOrderBy
		List<AnsichtOrderBy> viewOrderByList = this.ofdbService.findAnsichtOrderByAnsichtId( ansicht.getId() );
		for ( IAnsichtOrderBy viewOrderBy : viewOrderByList ) {

			partSet = this.ofdbValidator.isViewOrderByValid( viewOrderBy, builder.buildHandle() );
			set.merge( partSet );
			if ( !partSet.hasErrors() ) {
				builder.addViewOrderBy( viewOrderBy );
			}

		}

		// initialize AnsichtSpalten
		Map<String, IAnsichtSpalte> ansichtSpalten = this.ofdbService
				.findAnsichtSpaltenMapByAnsichtId( ansicht.getId() );
		for ( Map.Entry<String, IAnsichtSpalte> entry : ansichtSpalten.entrySet() ) {

			// ... TestQueryBuilder: get the relevant tabSpeig of the ansichtSpalte
			IAnsichtSpalte ansichtSpalte = entry.getValue();

			partSet = this.ofdbValidator.isAnsichtSpalteValid( ansichtSpalte, builder.buildHandle() );
			set.merge( partSet );
		}

		if ( set.hasErrors() ) {
			handleValidationErrors( set );
		}
		builder.setViewColumns( ansichtSpalten );

		// ... FIXME: no mainAnsichtTab for ADAnsichtenDef
		initQueryModel( viewName, builder );

		List<OfdbField> ofFieldList = createOfdbFields( builder.buildHandle(), CRUD.SELECT, mainPropertyMap );
		builder.setOfdbFields( ofFieldList );

		return builder.buildHandle();
	}

	private void checkMappingTabSpeig2Property( final List<ITabSpeig> tabSpeigs,
			final Map<String, OfdbPropMapper> propertyMap ) {

		for ( ITabSpeig tabProp : tabSpeigs ) {

			OfdbPropMapper ofdbPropMapper = propertyMap.get( tabProp.getSpalte().toUpperCase() );
			if ( null == ofdbPropMapper ) {
				String msg = MessageFormat.format( "Table property {0} of table {1} could not be mapped to property.",
						tabProp.getSpalte(), tabProp.getTabDef().getName() );
				LOGGER.warn( msg );
			}

		}

	}

	private Class<? extends AbstractMWEntity> checkFullClassName( final ITabDef tabDef )
			throws OfdbInvalidConfigurationException {
		Class<? extends AbstractMWEntity> entityClassType = null;
		try {
			entityClassType = ClassNameUtils.getClassType( tabDef.getFullClassName() );
		} catch ( ClassNotFoundException e ) {
			// do nothing. Conversion already checked in OfdbValidator.
			String msg = MessageFormat.format(
					"Invalid TabDef-configuration: Field fullClassName {0} of TabDef {1} not valid: ",
					tabDef.getFullClassName(), tabDef );
			LOGGER.error( msg );
			throw new OfdbInvalidConfigurationException( msg );
		}
		return entityClassType;
	}

	// FIXMEi: remove method
	private ViewConfigValidationResultSet validateTabSpeigList( final List<ITabSpeig> tabSpeigs ) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();

		for ( ITabSpeig tabSpeig : tabSpeigs ) {

			// checkNull( tabSpeig, set );
			ViewConfigValidationResultSet partSet = this.ofdbValidator.isTabSpeigValid( tabSpeig );
			set.merge( partSet );

		}

		return set;
	}

	// //@Override
	private void initQueryModel( final String viewName, final Builder builder ) {

		OfdbQueryModel queryModel = new DefaultOfdbQueryModel();

		IAnsichtTab ansichtTab = builder.buildHandle().getMainAnsichtTab();
		if ( null == ansichtTab ) {
			String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, "missingAnsichtTabJoinTypX",
					viewName );
			throw new OfdbInvalidConfigurationException( msg );
		}

		queryModel.setMainTable( ansichtTab.getTabDef() );
		// FIXME: getTabDef WRONG !!! We have to use T entity from method-argument here.

		builder.setQueryModel( queryModel );

	}

	private int calculateMaxLength( final ITabSpeig tabSpeig ) {

		if ( null == tabSpeig.getMaximum() ) {

			int result = 0;
			switch ( tabSpeig.getDbDatentyp() ) {
				case BOOLEAN:
					result = 1;
				case DATE:
					result = 10;
				case DATETIME:
					result = 10;
				case LONGINTEGER:
					result = 9;
				case STRING:
					result = 255;
				default: {
					LOGGER.error( "No maxlength defined for DBType: " + tabSpeig.getDbDatentyp().getDescription()
							+ ", tabSpeig: " + tabSpeig.getSpalte() );
				}
			}

			return result;
		} else {
			return Integer.valueOf( (String) tabSpeig.getMaximum() );
		}

	}

	/**
	 *
	 * @param tabDef
	 * @return the simple name of the entity given by the fullclassname of the TabDef-column
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

	@Transactional(propagation = Propagation.REQUIRED)
	private List<OfdbField> createOfdbFields( final ViewConfigHandle viewHandle, final CRUD crud,
			final Map<String, OfdbPropMapper> mainPropertyMap ) {
		List<OfdbField> ofFields = new ArrayList<OfdbField>();

		Map<String, IAnsichtSpalte> ansichtSpalten = viewHandle.getViewColumns();
		int resultIndex = 0;

		OfdbQueryModel queryModel = viewHandle.getQueryModel();

		for ( Map.Entry<String, IAnsichtSpalte> entry : ansichtSpalten.entrySet() ) {

			IAnsichtSpalte ansichtSpalte = entry.getValue();
			ITabSpeig tabSpeig = viewHandle.findTabSpeigByTabAKeyAndSpalteAKey( ansichtSpalte.getTabAKey(),
					ansichtSpalte.getSpalteAKey() );

			// FIXME: exception can be removed if there is foreign key from FX_AnsichtSpalten_K.spalteakey to
			// FX_TabSpEig_K.spalte
			if ( null == tabSpeig ) {
				String msg = MessageFormat.format(
						"No TabSpEig can be found for AnsichtSpalte with spalteAKey {0} and tabAKey {1}.",
						ansichtSpalte.getSpalteAKey(), ansichtSpalte.getTabAKey() );
				throw new OfdbInvalidConfigurationException( msg );
			}

			OfdbField ofField = new OfdbField( tabSpeig, entry.getValue(), crud );

			OfdbPropMapper propMapper = mainPropertyMap.get( tabSpeig.getSpalte().toUpperCase() );

			String propName = StringUtils.EMPTY;
			if ( null != propMapper ) {
				propName = propMapper.getPropertyName();
				ofField.setPropName( propName );
			}

			ofField.setMaxlength( calculateMaxLength( tabSpeig ) );

			if ( !StringUtils.isEmpty( ansichtSpalte.getAnsichtSuchen() ) ) {

				// zu 1.:
				ViewConfigHandle viewHandleSuchen = this.ofdbCacheManager
						.getViewConfig( ansichtSpalte.getAnsichtSuchen() );

				IAnsichtTab ansichtTab = viewHandle.findAnsichtTabByTabAKey( ansichtSpalte.getSuchwertAusTabAKey() );

				// FIXME: null-check no more needed when we use db-foreign-keys on ansichtDefId
				if ( null == ansichtTab ) {
					String msg = MessageFormat.format(
							"Fehlende Tabelle für Verknüpfung in FX_AnsichtTab_K zum Feld FX_AnsichtSpalten_K.SuchWertAusTabAKey für Ansicht: {0}, Spalte: {1}",
							ansichtSpalte.getAnsichtDef().getName(), ansichtSpalte.getSpalteAKey() );
					throw new OfdbInvalidConfigurationException( msg );
				}

				// ... replace genericofdbdao with ofdbDao
				ITabDef tabDef = null;
				if ( ansichtSpalte.getSuchwertAusTabAKey().equals( ansichtSpalte.getTabAKey() ) ) {
					tabDef = viewHandle.getMainAnsichtTab().getTabDef();
					viewHandleSuchen = viewHandle;
					// } else if ( null == tabDef ) {
					// // case if view references to parent instance of itself (e.g. FX_Menues_K -> FX_(Haupt)Menues_K)
					// // if ( viewHandle.getMainAnsichtTab().getTabDef().getName()
					// // .equals( ansichtSpalte.getSuchwertAusTabAKey() ) ) {
					// // } else {
					// String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME,
					// "invalidOfdbConfig.FX_AnsichtSpalten.SuchWertAusTabAKey_missingEntry",
					// ansichtSpalte.getSuchwertAusTabAKey(), viewHandle.getViewDef().getName() );
					// throw new OfdbMissingMappingException( msg );
					// // }
				} else {

					tabDef = this.ofdbCacheManager.findRegisteredTableDef( ansichtSpalte.getSuchwertAusTabAKey() );
					if ( null == tabDef ) {
						String msg = MessageFormat.format(
								"Wrong order of loading registered views. View {0} is referencing missing view {1}",
								ansichtSpalte.getAnsichtDef().getName(), ansichtSpalte.getSuchwertAusTabAKey() );
						LOGGER.error( msg );
					}

				}

				// } catch ( OfdbMissingMappingException e ) {
				// String msg = MessageFormat.format( "Technical error: dao not found for table : {0}",
				// tabDef.getName() );
				// LOGGER.error( msg );
				// throw new OfdbInvalidConfigurationException( msg );
				// }

				if ( ofField.isMapped() ) {

					// IGenericDao<AbstractMWEntity> mappedDao = this.ofdbService.findDaoByTableName( tabDef );
					// TODO: tabBeziehnungen here still needed? this.daoMap still needed ?
					Class<? extends AbstractMWEntity> clazz = ClassNameUtils.loadClass( tabDef.getFullClassName() );
					List<Object> listOfValues = this.ofdbService.getListOfValues( ofField, tabSpeig,
							viewHandleSuchen.getViewOrders(), clazz );
					ofField.setListOfValues( listOfValues );

					ITabSpeig suchWertTabSpeig = viewHandleSuchen.findTabSpeigByTabAKeyAndSpalteAKey(
							ansichtSpalte.getSuchwertAusTabAKey(), ansichtSpalte.getSuchwertAusSpalteAKey() );

					// e.g. TableA references TableA recursively ...
					OfdbPropMapper suchPropMapper = null;
					if ( tabSpeig.getTabDef().equals( suchWertTabSpeig.getTabDef() ) ) {
						suchPropMapper = mainPropertyMap.get( suchWertTabSpeig.getSpalte().toUpperCase() );
					} else {
						suchPropMapper = viewHandleSuchen.findPropertyMapperByTabProp( suchWertTabSpeig );
						// this.ofdbCacheManager.findPropertyMapperByTabSpeig( suchWertTabSpeig );
					}

					String listPropValueName = suchPropMapper.getPropertyName();
					String itemKey = null;
					ofField.setItemValue( listPropValueName );

					if ( !StringUtils.isEmpty( ansichtSpalte.getVerdeckenDurchTabAKey() )
							&& !StringUtils.isEmpty( ansichtSpalte.getVerdeckenDurchSpalteAKey() ) ) {

						suchWertTabSpeig = viewHandleSuchen.findTabSpeigByTabAKeyAndSpalteAKey(
								ansichtSpalte.getVerdeckenDurchTabAKey(), ansichtSpalte.getVerdeckenDurchSpalteAKey() );

						OfdbPropMapper mapper = null;
						if ( tabSpeig.getTabDef().equals( suchWertTabSpeig.getTabDef() ) ) {
							mapper = mainPropertyMap.get( suchWertTabSpeig.getSpalte().toUpperCase() );
						} else {
							mapper = viewHandleSuchen.findPropertyMapperByTabProp( suchWertTabSpeig );
							// this.ofdbCacheManager.findPropertyMapperByTabSpeig( suchWertTabSpeig );
						}

						// OfdbPropMapper mapper = this.ofdbCacheManager.findPropertyMapperByTabSpeig( suchWertTabSpeig
						// );
						listPropValueName = mapper.getPropertyName();
						queryModel.addAlias( ansichtTab, suchWertTabSpeig );
						ofField.setResultIndex( ++resultIndex );
						ofField.setColumnTitle( suchWertTabSpeig.getSpaltenkopf() );
						ofField.setItemValue( listPropValueName );
						itemKey = getSimpleName( suchWertTabSpeig.getTabDef() ) + "." + listPropValueName;

					} else {

						ofField.setResultIndex( 0 );
						itemKey = getSimpleName( ansichtTab.getTabDef() ) + "." + listPropValueName;

					}
					queryModel.addJoinTable( ansichtTab );
					ofField.setItemLabel( listPropValueName );
					ofField.setItemKey( itemKey );

				} else {
					// no index needed here
				}

			} else { // AnsichtSuchen is empty

				if ( ofField.isEnum() ) {
					ofField.setItemLabel( propName );
					ofField.setItemValue( propName );
					ofField.setItemKey( propName );

					List<Object> listOfValues = this.ofdbService.getListOfValues( ofField, tabSpeig, null, null );
					ofField.setListOfValues( listOfValues );
					// this.ofdbDao.setListOfValues( ofField, tabSpeig, null, null );

				} else {

				}
				ofField.setResultIndex( 0 );
			}

			ofFields.add( ofField );

		}

		Collections.sort( ofFields, new OfdbFieldComparator() );

		return ofFields;

	}
}
