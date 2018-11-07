package de.mw.mwdata.ofdb.cache;

import java.util.List;

import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;

public interface OfdbCacheManager {

	// FIXME: method isCacheBuild() should be added
	// FIXME: OfdbCacheManager can only be used when isCacheBuild() == true

	/**
	 * Loads view information to cache given by the {@link ViewConfigHandle}
	 * 
	 */
	public void registerView(final ViewConfigHandle viewHandle);

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

	public OfdbEntityMapping getEntityMapping(String tableName);

	public ViewConfigHandle getViewConfig(String ansichtSuchen);

	public ViewConfigHandle findViewConfigByTableName(String name);

	public void unregisterView(String name);

	public String mapTabSpeig2Property(final ITabSpeig tabSpeig);

}
