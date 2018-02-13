package de.mw.mwdata.app.admin.server.config;

import java.util.ResourceBundle;

import de.mw.mwdata.core.service.AbstractApplicationConfigService;

public class AdminConfigurationService extends AbstractApplicationConfigService {

	@Override
	protected ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(getBundleName());
	}

}
