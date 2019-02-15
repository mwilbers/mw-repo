package de.mw.mwdata.ofdb.mocks;

import java.util.ResourceBundle;

import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.ofdb.impl.AbstractOfdbAppConfigService;

public class ApplicationConfigServiceMock extends AbstractOfdbAppConfigService implements ApplicationConfigService {

	public ApplicationConfigServiceMock(String bundleName) {
		super(bundleName);
		// TODO Auto-generated constructor stub
	}

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

	@Override
	protected ResourceBundle getResourceBundle() {
		// TODO Auto-generated method stub
		return null;
	}

}
