package de.mw.mwdata.ofdb.cache;

public interface ViewConfigFactory {

	/**
	 * 
	 * @param viewName
	 * @return
	 */
	public ViewConfigHandle createViewConfiguration( final String viewName );
	
}
