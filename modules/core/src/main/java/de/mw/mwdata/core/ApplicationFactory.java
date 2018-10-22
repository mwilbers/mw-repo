package de.mw.mwdata.core;

/**
 * Does the standard-initialization-loading of all necessary ofdb-data and
 * resources when starting application.
 *
 * @author mwilbers
 *
 */
public interface ApplicationFactory {

	/**
	 * Initialize-method for loading data at start of application (e.g.
	 * spring-driven init)
	 *
	 * @throws OfdbMissingMappingException
	 */
	public void init();

}