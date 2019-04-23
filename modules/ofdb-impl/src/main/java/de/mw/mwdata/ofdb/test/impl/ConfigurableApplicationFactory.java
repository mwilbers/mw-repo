package de.mw.mwdata.ofdb.test.impl;

import de.mw.mwdata.core.ApplicationState;
import de.mw.mwdata.core.service.ApplicationConfigService;

public interface ConfigurableApplicationFactory extends ApplicationConfigService {

	public void configure();

	/**
	 * The factory knows about the current initialization state of building up the
	 * application
	 *
	 * @return
	 */
	public ApplicationState getState();

}
