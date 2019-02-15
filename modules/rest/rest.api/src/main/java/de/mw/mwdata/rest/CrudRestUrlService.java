package de.mw.mwdata.rest;

import java.net.MalformedURLException;

import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.rest.url.RestUrl;

/**
 * Url service that builds and parses rest based url by following
 * convention:<br>
 * <code>http://applicationdomain/servletname/entity(/{entityId}) | HTTP-Statuscode</code><br>
 * FIXME: write unit-tests
 * 
 * @author WilbersM
 *
 */
public class CrudRestUrlService implements RestUrlService {

	private ApplicationConfigService configService;

	public void setApplicationConfigService(ApplicationConfigService configService) {
		this.configService = configService;
	}

	@Override
	public String createUrlForReadEntities(final String servletName, final String entityName) {
		String applicationUrl = this.configService.getPropertyValue(ApplicationConfigService.KEY_APPLICATION_URL);
		applicationUrl = addSafeContextPath(applicationUrl, servletName);
		applicationUrl = addSafeContextPath(applicationUrl, entityName);

		return applicationUrl;
	}

	private String addSafeContextPath(final String url, final String contextPath) {
		return url + (url.endsWith("/") ? contextPath : "/" + contextPath);
	}

	@Override
	public RestUrl parseRestUrl(String restUrl) throws MalformedURLException {
		return new RestUrl(restUrl);
	}

	@Override
	public String createUrlForMenuItem(String servletName, long menuId) {
		String applicationUrl = this.configService.getPropertyValue(ApplicationConfigService.KEY_APPLICATION_URL);
		applicationUrl = addSafeContextPath(applicationUrl, servletName);
		applicationUrl = addSafeContextPath(applicationUrl, "nav");
		applicationUrl = addSafeContextPath(applicationUrl, "menu");
		return addSafeContextPath(applicationUrl, String.valueOf(menuId));
	}

}
