package de.mw.mwdata.rest.service.config;

import java.util.ResourceBundle;

import de.mw.mwdata.core.service.AbstractApplicationConfigService;

/**
 * FIXME: create abstract base class of all application config services
 * 
 * @author WilbersM
 *
 */
public class RestConfigService extends AbstractApplicationConfigService {

	@Override
	protected ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(getBundleName());
	}

}
