package de.mw.mwdata.core.test.data;

import de.mw.mwdata.core.ofdb.AbstractApplicationFactory;
import de.mw.mwdata.core.ofdb.ControllerConfiguration;


public class ApplicationTestFactory extends AbstractApplicationFactory {

	public ApplicationTestFactory(final String contextPath) {
		super( contextPath );

	}

	public void registerControllers( final ControllerConfiguration controllerConfiguration ) {
		// dummy-method, no controllers in core-tests needed

	}

}
