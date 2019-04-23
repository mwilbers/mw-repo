package de.mw.mwdata.ofdb.test.impl;

import de.mw.mwdata.core.ApplicationState;
import de.mw.mwdata.core.service.ApplicationConfigService;

// FIXME: remove complete ApplicationFactory from app / tests
public class TestApplicationFactory implements ConfigurableApplicationFactory {

	private ApplicationConfigService applicationConfigService;

	public ApplicationConfigService getApplicationConfigService() {
		return this.applicationConfigService;
	}

	public void setApplicationConfigService(ApplicationConfigService applicationConfigService) {
		this.applicationConfigService = applicationConfigService;
	}

	private ApplicationState state;

	@Override
	public void configure() {
		this.state = ApplicationState.CONFIGURE;
	}

	@Override
	public void initApplication() {
		this.state = ApplicationState.INITIALIZE;
		this.applicationConfigService.initApplication();
		this.state = ApplicationState.RUNNING;
	}

	@Override
	public ApplicationState getState() {
		return this.state;
	}

	@Override
	public String getPropertyValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createIdentifier(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
