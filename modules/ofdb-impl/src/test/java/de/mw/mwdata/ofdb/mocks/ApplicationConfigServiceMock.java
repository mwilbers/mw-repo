package de.mw.mwdata.ofdb.mocks;

import de.mw.mwdata.core.service.ApplicationConfigService;

public class ApplicationConfigServiceMock implements ApplicationConfigService {

	@Override
	public String getPropertyValue(String key) {

		if (key == ApplicationConfigService.KEY_PAGESIZE_FOR_LOAD) {
			return "100";
		} else if (key == ApplicationConfigService.KEY_USERAREA) {
			return "Administrator";
		} else {
			return null;
		}

	}

	@Override
	public String createIdentifier(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
