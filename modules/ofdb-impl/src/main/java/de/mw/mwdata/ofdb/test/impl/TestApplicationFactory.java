package de.mw.mwdata.ofdb.test.impl;

import de.mw.mwdata.core.ApplicationState;
import de.mw.mwdata.ofdb.impl.DefaultApplicationFactory;

public class TestApplicationFactory extends DefaultApplicationFactory implements ConfigurableApplicationFactory {

	private ApplicationState state;

	@Override
	public void configure() {
		this.state = ApplicationState.CONFIGURE;
	}

	@Override
	public void init() {
		this.state = ApplicationState.INITIALIZE;
		super.init();
		this.state = ApplicationState.RUNNING;
	}

	@Override
	public ApplicationState getState() {
		return this.state;
	}

}
