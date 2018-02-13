package de.mw.mwdata.rest.service.service.test;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.rest.service.service.CrudRestUrlService;

public class CrudRestUrlServiceTest {

	@Test
	public void testcreateUrlForEntities() {

		CrudRestUrlService restService = new CrudRestUrlService();
		ApplicationConfigService configService = this.createAppConfigServiceMock();
		restService.setApplicationConfigService(configService);

		String url = restService.createUrlForReadEntities("tabDef");
		Assert.assertEquals(url, "http://www.mydomain.com/myApplication/myServlet/tabDef");

	}

	private ApplicationConfigService createAppConfigServiceMock() {
		ApplicationConfigService configService = EasyMock.createMock(ApplicationConfigService.class);

		EasyMock.expect(configService.getPropertyValue("app.applicationUrl"))
				.andReturn("http://www.mydomain.com/myApplication").anyTimes();

		EasyMock.expect(configService.getPropertyValue("app.applicationServletName")).andReturn("myServlet").anyTimes();

		EasyMock.replay(configService);
		return configService;
	}

}
