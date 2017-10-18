package de.mw.mwdata.core.ofdb.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.mw.mwdata.core.ofdb.def.OfdbPropMapper;
import de.mw.mwdata.core.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;

public class DefaultOfdbCacheManager implements OfdbCacheManager {

	private static final Logger	LOGGER	= LoggerFactory.getLogger( DefaultOfdbCacheManager.class );

	// FIXME: cache should be moved to ehcache
	private OfdbCache			ofdbCache;

	public DefaultOfdbCacheManager() {
		this.ofdbCache = OfdbCache.createInstance();
	}

	/**
	 *
	 * @param tableName
	 * @return a map containing the key = tabSpeig.spalte and value = propertyname of the requested tableName
	 */
	public Map<String, OfdbPropMapper> getPropertyMap( final String tableName ) {
		return this.ofdbCache.getPropertyMap( tableName );
	}

	public void registerView( final ViewConfigHandle viewHandle ) throws OfdbMissingMappingException {
		this.addViewConfiguration( viewHandle );

		List<IAnsichtTab> ansichtTabList = viewHandle.getViewTabs();
		boolean isReferenced = false;
		if ( null == ansichtTabList ) {
			return;
		}
		for ( IAnsichtTab ansichtTab : ansichtTabList ) {

			// check if TabDef is referenced in any other AnsichtTab
			ITabDef tabDef = ansichtTab.getTabDef();
			isReferenced = this.isReferencedByCachedAnsichtTab( tabDef, ansichtTab );
			if ( !isReferenced ) {
				this.ofdbCache.addPropertyMap( tabDef.getName(), viewHandle.getPropertyMap() );
				// this.ofdbCache.removePropertyMap( tabDef.getName() );
				// this.tablePropertyMap.remove( tabDef.getName() );
				// this.tabSpeigMap.remove( tabDef.getName() );
				// this.tabDefMap.remove( tabDef.getName() );
			}
		}

	}

	// @Override
	public void unregisterView( final String viewName ) {

		ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig( viewName );
		if ( null == viewHandle ) {
			return;
		}

		List<IAnsichtTab> ansichtTabList = viewHandle.getViewTabs(); // this.ansichtTabMap.get( viewName );
		// // for each ansichtTab search if tabDef is used in any other ansichtTab
		boolean isReferenced = false;
		if ( null != ansichtTabList ) {

			for ( IAnsichtTab ansichtTab : ansichtTabList ) {

				// check if TabDef is referenced in any other AnsichtTab
				ITabDef tabDef = ansichtTab.getTabDef();
				isReferenced = this.isReferencedByCachedAnsichtTab( tabDef, ansichtTab );
				if ( !isReferenced ) {
					this.ofdbCache.removePropertyMap( tabDef.getName() );
					// this.tablePropertyMap.remove( tabDef.getName() );
					// this.tabSpeigMap.remove( tabDef.getName() );
					// this.tabDefMap.remove( tabDef.getName() );
				}
			}
		}

		this.ofdbCache.removeViewData( viewName );

	}

	// @Override
	public boolean isViewRegistered( final String viewName ) {

		ViewConfigHandle viewHandle = this.getViewConfig( viewName );
		// this.ofdbCache.getAnsichtByAnsichtName( viewName );
		if ( null == viewHandle ) {
			return false;
		}
		IAnsichtTab ansichtTab = viewHandle.getMainAnsichtTab(); // this.getMainAnsichtTab( viewName );
		if ( null == ansichtTab ) {
			return false;
		}
		ITabDef tabDef = viewHandle.getTableByName( ansichtTab.getTabDef().getName() );
		return (null != tabDef);

	}

	public void addViewConfiguration( final ViewConfigHandle viewHandle ) {
		this.ofdbCache.putViewConfiguration( viewHandle.getViewDef().getName(), viewHandle.getViewConfiguration() );

	}

	public ViewConfigHandle getViewConfig( final String viewName ) {
		return this.ofdbCache.getViewConfig( viewName );
	}

	public ITabDef findRegisteredTableDef( final String tableName ) {

		for ( String viewName : this.ofdbCache ) {
			ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig( viewName );

			for ( IAnsichtTab viewTab : viewHandle.getViewTabs() ) {
				if ( viewTab.getTabDef().getName().equals( tableName ) ) {
					return viewTab.getTabDef();
				}
			}

		}

		return null;
	}

	public List<ITabSpeig> findRegisteredTabSpeigs( final String tableName ) {

		for ( String viewName : this.ofdbCache ) {
			ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig( viewName );

			IAnsichtTab viewTab = viewHandle.getMainAnsichtTab();
			if ( viewTab.getTabDef().getName().equals( tableName ) ) {
				return viewHandle.getTableProps( viewTab.getTabDef() );
			}

		}

		return Collections.emptyList();

	}

	// FIXME: move method to OfdbCacheManager
	private boolean isReferencedByCachedAnsichtTab( final ITabDef tabDef, final IAnsichtTab ansichtTabToIgnore ) {

		for ( String viewName : this.ofdbCache ) {
			ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig( viewName );

			for ( IAnsichtTab viewTab : viewHandle.getViewTabs() ) {
				if ( viewTab.equals( ansichtTabToIgnore ) ) {
					continue;
				}

				if ( viewTab.getTabDef().equals( tabDef ) ) {
					return true;
				}

			}

		}

		return false;
	}

	public ViewConfigHandle findViewConfigByTableName( final String tableName ) {

		for ( String viewName : this.ofdbCache ) {
			ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig( viewName );
			if ( viewHandle.getMainAnsichtTab().getTabDef().getName().equals( tableName ) ) {
				return viewHandle;
			}
		}

		return null;
	}

	public List<ViewConfigHandle> getRegisteredViewConfigs() {

		List<ViewConfigHandle> viewConfigs = new ArrayList<ViewConfigHandle>();
		for ( String viewName : this.ofdbCache ) {
			ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig( viewName );
			viewConfigs.add( viewHandle );
		}

		return viewConfigs;
	}

	public ITabSpeig findTablePropByProperty( final String tableName, final String propertyName ) {

		Map<String, OfdbPropMapper> propMap = this.getPropertyMap( tableName );
		ITabSpeig tabSpeig = null;
		for ( Map.Entry<String, OfdbPropMapper> entry : propMap.entrySet() ) {
			if ( entry.getValue().getPropertyName().equals( propertyName ) ) {
				String spalte = entry.getKey();
				tabSpeig = findTablePropBySpalteName( tableName, spalte );
				break;
			}
		}

		return tabSpeig;
	}

	private ITabSpeig findTablePropBySpalteName( final String tableName, final String spalte ) {
		List<ITabSpeig> tabSpeigs = findRegisteredTabSpeigs( tableName );
		for ( ITabSpeig tabSpeig : tabSpeigs ) {
			if ( tabSpeig.getSpalte().equals( spalte ) ) {
				return tabSpeig;
			}
		}

		return null;
	}

	public OfdbPropMapper findPropertyMapperByTabSpeig( final ITabSpeig suchWertTabSpeig ) {

		Map<String, OfdbPropMapper> propMap = getPropertyMap( suchWertTabSpeig.getTabDef().getName() );

		OfdbPropMapper propMapper = propMap.get( suchWertTabSpeig.getSpalte().toUpperCase() );

		return propMapper; // this.findPropertyNameByTabSpeig( tabSpeig );

		// List<TabSpeig> tList = new ArrayList<TabSpeig>();
		// tList.add( suchWertTabSpeig );
		// Map<String, OfdbPropMapper> propMap = this.ofdbService.loadPropertyMapping( tList );
		//
		// return propMap.get( suchWertTabSpeig.getSpalte() ).getPropertyName();

	}

}
