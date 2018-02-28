package de.mw.mwdata.ofdb.cache;

import java.util.List;
import java.util.Map;

import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.impl.OfdbPropMapper;

public interface OfdbCacheManager {

	// FIXME: method isCacheBuild() should be added
	// FIXME: OfdbCacheManager can only be used when isCacheBuild() == true
	// FIXME: move all methods here that return objects under ViewConfigHandle to
	// class ViewConfigHandle. In Future OfdbCacheManager is only for searching
	// ViewConfigHandle - objects

	/**
	 * Loads view information to cache given by the {@link ViewConfigHandle}
	 * 
	 */
	public void registerView(final ViewConfigHandle viewHandle) throws OfdbMissingMappingException;

	/**
	 * @return true if view information given by the viewname is in cache
	 */
	public boolean isViewRegistered(final String viewName);

	/**
	 * 
	 * @return a list of all registered views given by their {@link ViewConfigHandle
	 *         }
	 */
	public List<ViewConfigHandle> getRegisteredViewConfigs();

	/**
	 * 
	 * @param tabAKey
	 * @return
	 */
	public ITabDef findRegisteredTableDef(final String tabAKey);

	public Map<String, OfdbPropMapper> getPropertyMap(String tableName);

	public ViewConfigHandle getViewConfig(String ansichtSuchen);

	public ViewConfigHandle findViewConfigByTableName(String name);

	public void unregisterView(String name);

}
