package de.mw.mwdata.app.admin.server.config;

import java.util.ResourceBundle;

import de.mw.mwdata.ofdb.impl.AbstractOfdbAppConfigService;

public class AdminConfigurationService extends AbstractOfdbAppConfigService {

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
