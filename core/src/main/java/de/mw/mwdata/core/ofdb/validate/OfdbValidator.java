package de.mw.mwdata.core.ofdb.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.mw.mwdata.core.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.core.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.core.ofdb.cache.ViewConfigValidationResultSet;
import de.mw.mwdata.core.ofdb.domain.AnsichtTab;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.domain.IAnsichtOrderBy;
import de.mw.mwdata.core.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.core.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.utils.ClassNameUtils;

public class OfdbValidator implements OfdbValidatable {

	private static final Logger	LOGGER	= LoggerFactory.getLogger( OfdbValidator.class );

	private OfdbCacheManager	ofdbCacheManager;

	public void setOfdbCacheManager( final OfdbCacheManager ofdbCacheManager ) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	// @Override
	public ViewConfigValidationResultSet isAnsichtValid( final IAnsichtDef ansichtDef ) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();
		if ( StringUtils.isEmpty( ansichtDef.getUrlPath() ) ) {
			set.addValidationResult( "illegalOfdbNullValue", ansichtDef.getName(), "urlPath" );
		}

		return set;
	}

	private void isViewTabUnique( final List<AnsichtTab> ansichtTabList, final ViewConfigValidationResultSet set ) {

		Map<String, List<AnsichtTab>> map = new HashMap<String, List<AnsichtTab>>();

		for ( AnsichtTab ansichtTab : ansichtTabList ) {

			String tableName = ansichtTab.getTabDef().getName();
			List<AnsichtTab> aTabs = null;
			if ( map.containsKey( tableName ) ) {

				aTabs = map.get( tableName );
				for ( AnsichtTab aTab : aTabs ) {

					// if duplicate tabAKey per refrenced table
					if ( aTab.getTabAKey().equalsIgnoreCase( ansichtTab.getTabAKey() ) ) {
						set.addValidationResult( "invalidOfdbConfig.FX_AnsichtTab.TabAKey", tableName, ansichtTab
								.getAnsichtDef().getName() );
					}

				}

			} else {
				aTabs = new ArrayList<AnsichtTab>();
				map.put( tableName, aTabs );
			}

			if ( ansichtTab.getTabAKey().equalsIgnoreCase( tableName ) ) {
				if ( !StringUtils.isBlank( ansichtTab.getTabelle() ) ) {
					set.addValidationResult( "invalidOfdbConfig.FX_AnsichtTab.TabelleNull", tableName, ansichtTab
							.getAnsichtDef().getName() );
				}

			} else {
				if ( StringUtils.isBlank( ansichtTab.getTabelle() ) ) {

					set.addValidationResult( "invalidOfdbConfig.FX_AnsichtTab.TabelleNotNull", tableName, ansichtTab
							.getAnsichtDef().getName() );

				}
			}
			aTabs = map.get( tableName );
			aTabs.add( ansichtTab );

		}
	}

	// @Override
	public ViewConfigValidationResultSet isAnsichtTabListValid( final IAnsichtDef ansichtDef,
			final List<AnsichtTab> ansichtTabList ) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();

		List<String> foundTables = new ArrayList<String>();
		for ( AnsichtTab viewTab : ansichtTabList ) {
			if ( viewTab.getJoinTyp().equals( "x" ) ) {
				foundTables.add( viewTab.getTabDef().getName() );
			}

			if ( foundTables.size() > 1 ) {
				set.addValidationResult( "invalidOfdbConfig.FX_AnsichtTab.duplicatedMainViewTab", ansichtDef.getName(),
						foundTables.toString() );
			}

		}

		isViewTabUnique( ansichtTabList, set );

		return set;

	}

	public ViewConfigValidationResultSet isTableValid( final ITabDef tabDef ) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();
		if ( StringUtils.isEmpty( tabDef.getFullClassName() ) ) {
			set.addValidationResult( "illegalOfdbNullValue", tabDef.getName(), "fullClassName" );

		}

		// check if fullClassName is valid
		try {
			ClassNameUtils.getClassType( tabDef.getFullClassName() );
		} catch ( ClassNotFoundException e ) {
			set.addValidationResult( "invalidOfdbValue", tabDef.getName(), "fullClassName", tabDef.getFullClassName() );
		}

		return set;
	}

	// @Override
	public ViewConfigValidationResultSet isAnsichtSpalteValid( final IAnsichtSpalte spalte,
			final ViewConfigHandle viewHandle ) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();
		// TabSpeig tabSpeig = viewHandle.findTabSpeigByAnsichtSpalte( spalte );
		// if ( null == tabSpeig ) {
		// // String msg = LocalizedMessages.getString( Config.BUNDLE_NAME, "MissingTabSpeigMapping",
		// // spalte.getSpalteAKey(), spalte.getAnsichtDef().getName() );
		// // result.setErrorMessage( msg );
		//
		// set.addValidationResult( "MissingTabSpeigMapping", spalte.getSpalteAKey(), spalte.getAnsichtDef().getName()
		// );
		//
		// }

		if ( !StringUtils.isEmpty( spalte.getAnsichtSuchen() ) ) {

			if ( StringUtils.isEmpty( spalte.getSuchwertAusTabAKey() )
					|| StringUtils.isEmpty( spalte.getSuchwertAusSpalteAKey() ) ) {
				// String message = LocalizedMessages.getString( Config.BUNDLE_NAME,
				// "invalidOfdbConfig.FX_AnsichtSpalten.AnsichtSuchen", spalte.getSpalteAKey(), spalte.getName() );
				// result.setErrorMessage( message );
				set.addValidationResult( "invalidOfdbConfig.FX_AnsichtSpalten.AnsichtSuchen", spalte.getSpalteAKey(),
						spalte.getName() );
			}

			ViewConfigHandle viewHandleSuchen = this.ofdbCacheManager.getViewConfig( spalte.getAnsichtSuchen() );
			if ( spalte.getSuchwertAusTabAKey().equals( spalte.getTabAKey() ) ) {
				viewHandleSuchen = viewHandle;
			}

			IAnsichtDef ansichtDef = viewHandleSuchen.getViewDef();
			// this.ofdbService.findAnsichtByName( spalte.getAnsichtSuchen() );

			// FIXME: null-check no more needed when we use db-foreign-keys on ansichtDefId
			if ( null == ansichtDef ) {
				// String msg =
				// "Fehlende AnsichtDef für Verknüpfung im Feld FX_AnsichtSpalten_K.AnsichtSuchen für Ansicht: "
				// + spalte.getName() + ", Spalte: " + spalte.getSpalteAKey();
				// result.setErrorMessage( msg );
				set.addValidationResult( "invalidOfdbConfig.FX_AnsichtSpalten.AnsichtSuchen_missingEntry",
						spalte.getName(), spalte.getSpalteAKey() );
			}

			ITabDef suchTabDef = this.ofdbCacheManager.findRegisteredTableDef( spalte.getSuchwertAusTabAKey() );
			if ( spalte.getSuchwertAusTabAKey().equals( spalte.getTabAKey() ) ) {
				viewHandleSuchen = viewHandle;
				suchTabDef = viewHandle.getMainAnsichtTab().getTabDef();
			} else if ( null == suchTabDef ) {
				set.addValidationResult(
						"invalidOfdbConfig.FX_AnsichtTab.missingTabDef_AnsichtSpalte_SuchWertAusTabAKey",
						spalte.getName(), spalte.getSpalteAKey() ); // -> siehe aufruf createOfdbFields()
			}

			// IGenericDao<AbstractMWEntity> dao = this.ofdbService.findDaoByTableName( suchTabDef );
			// if ( null == dao ) {
			// set.addValidationResult( "invalidOfdbConfig.FX_AnsichtSpalten.SuchWertAusTabAKey_missingDao",
			// spalte.getSuchwertAusTabAKey(), spalte.getName(), spalte.getAnsichtDef().getName() );
			// }

			IAnsichtTab ansichtTabSuchen = viewHandleSuchen.findAnsichtTabByTabAKey( spalte.getSuchwertAusTabAKey() );
			if ( null == ansichtTabSuchen ) {
				// String message =
				// "Fehlende Tabelle für Verknüpfung in FX_AnsichtTab_K zum Feld FX_AnsichtSpalten_K.SuchWertAusTabAKey für Ansicht: "
				// + spalte.getName() + ", Spalte: " + spalte.getSpalteAKey();
				set.addValidationResult(
						"invalidOfdbConfig.FX_AnsichtTab.missingMapping_AnsichtSpalte_SuchWertAusTabAKey",
						spalte.getName(), spalte.getSpalteAKey() ); // -> siehe aufruf createOfdbFields()
			}

			if ( !StringUtils.isEmpty( spalte.getVerdeckenDurchTabAKey() )
					&& !StringUtils.isEmpty( spalte.getVerdeckenDurchSpalteAKey() ) ) {

				if ( !spalte.getVerdeckenDurchTabAKey().equals( spalte.getSuchwertAusTabAKey() ) ) {
					// String message = LocalizedMessages.getString( Config.BUNDLE_NAME,
					// "invalidOfdbConfig.FX_AnsichtSpalten.VerdeckenDurchTabAKey",
					// spalte.getVerdeckenDurchTabAKey() );
					// result.setErrorMessage( message );
					set.addValidationResult( "invalidOfdbConfig.FX_AnsichtSpalten.VerdeckenDurchTabAKey",
							spalte.getVerdeckenDurchTabAKey() );
				}

				ITabDef verdeckenTabDef = this.ofdbCacheManager.findRegisteredTableDef( spalte
						.getVerdeckenDurchTabAKey() );
				if ( spalte.getSuchwertAusTabAKey().equals( spalte.getTabAKey() ) ) {
					verdeckenTabDef = viewHandle.getMainAnsichtTab().getTabDef();
				}
				if ( null == verdeckenTabDef ) {
					set.addValidationResult(
							"invalidOfdbConfig.FX_AnsichtTab.missingTabDef_AnsichtSpalte_VerdeckenDurchTabAKey",
							spalte.getName(), spalte.getSpalteAKey() ); // -> siehe aufruf createOfdbFields()
				}

			}

		}

		return set;

	}

	// FIXME: remove method
	public ViewConfigValidationResultSet isTabSpeigValid( final ITabSpeig tabSpeig ) {

		ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();

		// FIXME: hier noch ofdbService.checkUnique verdrahten

		// Validate.notNull( tabSpeig );

		// if ( null == tabSpeig ) {
		// String msg = LocalizedMessages.getString( Config.BUNDLE_NAME, "MissingTabSpeigMapping",
		// spalte.getSpalteAKey(), spalte.getAnsichtDef().getName() );
		// result.addErrorMessage( msg );
		// }

		return set;

	}

	// private void checkNull( final AbstractMWEntity entity, final ViewConfigValidationResultSet validationResultSet )
	// {
	//
	// if ( null == entity ) {
	// LOGGER.error( validationResultSet.toString() );
	// String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME_OFDB, "invalidConfiguration" );
	// throw new OfdbInvalidConfigurationException( msg );
	// }
	//
	// }

	// @Override
	public ViewConfigValidationResultSet isViewOrderByValid( final IAnsichtOrderBy ansichtOrderBy,
			final ViewConfigHandle viewHandle ) {

		// ViewConfigValidationResultSet set = new ViewConfigValidationResultSet();
		// // AnsichtTab viewTab = viewHandle.findAnsichtTabByTabAKey( ansichtOrderBy.getTabAKey() );
		// TabSpeig orderTabSpeig = viewHandle.findTabSpeigByAnsichtOrderBy( ansichtOrderBy );
		// if ( null == orderTabSpeig ) {
		// set.addValidationResult( "invalidOfdbConfig.FX_AnsichtOrderBy.missingTabSpeig", ansichtOrderBy.getId()
		// .toString(), ansichtOrderBy.getName() ); // -> siehe aufruf createOfdbFields()
		// } else {
		//
		// List<TabSpeig> orderList = new ArrayList<TabSpeig>();
		// orderList.add( orderTabSpeig );
		//
		// ... fehler: NPE hier im TabSpeigDefaultsTEst: dazu tue:
		// 1. ANSICHTORDERBY tabelle muss über ANSICHTTABID und ANSICHTSPALTENID gehen, nicht über TABDEFID
		// 2. alle unit-tests testen
		//
		// OfdbPropMapper orderPropMapper = this.ofdbCacheManager.findPropertyMapperByTabSpeig( orderTabSpeig );
		// String orderPropName = orderPropMapper.getPropertyName();
		// // ... NPE: orderPropName darf nicht aus ofdbCache geladen werdne, weil noch nicht da ...
		// // Map<String, OfdbPropMapper> propertyMap = this.ofdbService.loadPropertyMapping( orderList );
		// // String propName = propertyMap.get( orderTabSpeig.getSpalte().toUpperCase() ).getPropertyName();
		//
		// if ( null == orderPropName ) {
		// set.addValidationResult( "invalidOfdbConfig.FX_AnsichtOrderBy.missingProperty", ansichtOrderBy.getId()
		// .toString(), ansichtOrderBy.getSpalteAKey() ); // -> siehe aufruf createOfdbFields()
		//
		// }
		// }

		return new ViewConfigValidationResultSet();
	}
}
