package de.mw.mwdata.core.ofdb.cache;

import java.util.List;
import java.util.Map;

import de.mw.mwdata.core.ofdb.def.OfdbPropMapper;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;

public interface OfdbCacheManager {

	
	/**
	 * Loads view information to cache given by the {@link ViewConfigHandle}
	 * 
	 */
	public void registerView( final ViewConfigHandle viewHandle ) throws OfdbMissingMappingException;

	/**
	 * @return true if view information given by the viewname is in cache
	 */
	public boolean isViewRegistered( final String viewName );
	
	/**
	 * 
	 * @return a list of all registered views given by their {@link ViewConfigHandle }
	 */
	public List<ViewConfigHandle> getRegisteredViewConfigs();
	
	/**
	 * 
	 * @param tabAKey
	 * @return
	 */
	public ITabDef findRegisteredTableDef( final String tabAKey );

	
	public List<ITabSpeig> findRegisteredTabSpeigs(String tableName);

	public Map<String, OfdbPropMapper> getPropertyMap(String tableName);

	public ViewConfigHandle getViewConfig(String ansichtSuchen);

	public OfdbPropMapper findPropertyMapperByTabSpeig( ITabSpeig suchWertTabSpeig);

	public ViewConfigHandle findViewConfigByTableName(String name);

	public ITabSpeig findTablePropByProperty(String name, String propertyName);

	public void unregisterView(String name);
	
}
