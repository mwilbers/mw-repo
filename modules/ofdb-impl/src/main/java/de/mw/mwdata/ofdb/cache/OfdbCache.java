package de.mw.mwdata.ofdb.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.mw.mwdata.ofdb.impl.OfdbPropMapper;

/**
 * Stateful cache for holding all ofdb-relevant ansicht- and tab-informations for all used views in application.
 *
 * @author mwilbers
 * @since Nov, 2012
 *
 */
public class OfdbCache implements Iterable<String> {

	private OfdbCache() {
		// no constructor, use singleton-pattern
	}

	public static OfdbCache	cache	= new OfdbCache();

	public static OfdbCache createInstance() {
		return cache;
	}

	/**
	 * map with key = tableName and value = (map of tabSpeig.spalte (key) and propertyname (value) of the given
	 * tableName)
	 */
	private Map<String, Map<String, OfdbPropMapper>>	tablePropertyMap	= new HashMap<String, Map<String, OfdbPropMapper>>();

	private Map<String, ViewConfiguration>				viewConfigs			= new HashMap<String, ViewConfiguration>();

	public void putViewConfiguration( final String viewName, final ViewConfiguration viewConfig ) {
		this.viewConfigs.put( viewName, viewConfig );
	}

	// FIXME: move method to OfdbCacheManager, class OfdbCache should be stupid ...
	public void removeViewData( final String viewName ) {
		this.viewConfigs.remove( viewName );
	}

	/**
	 * Adds a map containing key = tabSpeig.spalte and value = propertyname of the given tableName to the cache
	 *
	 * @param tableName
	 * @param map
	 */
	public void addPropertyMap( final String tableName, final Map<String, OfdbPropMapper> map ) {
		this.tablePropertyMap.put( tableName, map );
	}

	/**
	 *
	 * @param tableName
	 * @return a map containing a map with key = tabSpeig.spalte and value = propertname of the requested tableName
	 */
	public Map<String, OfdbPropMapper> getPropertyMap( final String tableName ) {
		// ... fehler bei laden der ansicht FX_TabSpeig_K
		return this.tablePropertyMap.get( tableName );
	}

	public void removePropertyMap( final String tableName ) {
		this.tablePropertyMap.remove( tableName );
	}

	public ViewConfigHandle getViewConfig( final String viewName ) {

		ViewConfiguration viewConfig = this.viewConfigs.get( viewName );

		if ( null != viewConfig ) {
			return new ViewConfigHandle( viewConfig );
		} else {
			return null;
		}
	}

	public Iterator<String> iterator() {
		return this.viewConfigs.keySet().iterator();
		// return null;
	}

}
