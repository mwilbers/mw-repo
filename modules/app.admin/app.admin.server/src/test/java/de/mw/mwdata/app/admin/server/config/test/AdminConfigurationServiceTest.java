package de.mw.mwdata.app.admin.server.config.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.mwdata.app.admin.server.config.AdminConfigurationService;
import de.mw.mwdata.core.service.ApplicationConfigService;

public class AdminConfigurationServiceTest {

	@Test
	public void testApplicationConfigService() {

		ApplicationConfigService configService = new AdminConfigurationService();
		configService.setPropertyBundle("de.mw.mwdata.app.admin.config");

		String urlValue = configService.getPropertyValue(ApplicationConfigService.KEY_APPLICATION_URL);
		Assert.assertEquals(urlValue, "http://localhost:8080/app.admin.client");

	}

}
