package de.mw.mwdata.ofdb.mocks;

import de.mw.mwdata.core.service.ApplicationConfigService;

public class ApplicationConfigServiceMock implements ApplicationConfigService {

	@Override
	public String getPropertyValue(String key) {

		if (key == "app.hibernate.pageSizeForLoad") {
			return "100";
		} else {
			return null;
		}

	}

}
