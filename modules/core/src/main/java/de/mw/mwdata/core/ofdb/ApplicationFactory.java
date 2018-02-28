package de.mw.mwdata.core.ofdb;

import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;

/**
 * Does the standard-initialization-loading of all necessary ofdb-data and
 * resources when starting application.
 *
 * @author mwilbers
 *
 */
public interface ApplicationFactory {

	public void configure();

	/**
	 * Initialize-method for loading data at start of application (e.g.
	 * spring-driven init)
	 *
	 * @throws OfdbMissingMappingException
	 */
	public void init() throws OfdbMissingMappingException;

	/**
	 * The factory knows about the current initialization state of building up the
	 * application
	 *
	 * @return
	 */
	public ApplicationState getState();

}