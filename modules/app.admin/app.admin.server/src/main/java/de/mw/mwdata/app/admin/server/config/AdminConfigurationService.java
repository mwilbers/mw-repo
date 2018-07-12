package de.mw.mwdata.app.admin.server.config;

import java.util.ResourceBundle;

import de.mw.mwdata.core.service.AbstractApplicationConfigService;

public class AdminConfigurationService extends AbstractApplicationConfigService {

	private final String APPLICATION_ID = "MW.APP.ADMIN";

	public AdminConfigurationService(String bundleName) {
		super(bundleName);

	}

	@Override
	protected ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(getBundleName());
	}

	@Override
	public String createIdentifier(String token) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(APPLICATION_ID);
		buffer.append(this.getIdentDelimiter());
		buffer.append(token);
		return buffer.toString();
	}

}
