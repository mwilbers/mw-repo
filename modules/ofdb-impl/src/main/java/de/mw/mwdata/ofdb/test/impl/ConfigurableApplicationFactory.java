package de.mw.mwdata.ofdb.test.impl;

import de.mw.mwdata.core.ApplicationFactory;
import de.mw.mwdata.core.ApplicationState;

public interface ConfigurableApplicationFactory extends ApplicationFactory {

	public void configure();

	/**
	 * The factory knows about the current initialization state of building up the
	 * application
	 *
	 * @return
	 */
	public ApplicationState getState();

}
