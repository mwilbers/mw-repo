package de.mw.mwdata.core.ofdb.cache;

public interface ViewConfigFactory {

	/**
	 * 
	 * @param viewName
	 * @return
	 */
	public ViewConfigHandle createViewConfiguration( final String viewName );
	
}
