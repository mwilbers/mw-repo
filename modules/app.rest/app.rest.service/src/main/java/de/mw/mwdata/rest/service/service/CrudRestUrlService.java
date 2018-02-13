package de.mw.mwdata.rest.service.service;

import java.net.MalformedURLException;

import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.rest.service.RestbasedMwUrl;

/**
 * Url service that builds and parses rest based url by following
 * convention:<br>
 * <code>http://applicationdomain/servletname/entity(/{entityId}) | HTTP-Statuscode</code>
 * 
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
	public String createUrlForReadEntities(final String entityName) {
		String applicationUrl = this.configService.getPropertyValue(ApplicationConfigService.KEY_APPLICATION_URL);
		applicationUrl = addSafeUrlToken(applicationUrl,
				this.configService.getPropertyValue("app.applicationServletName"));
		return addSafeUrlToken(applicationUrl, entityName);
	}

	private String addSafeUrlToken(final String url, final String token) {
		return url + (url.endsWith("/") ? token : "/" + token);
	}

	@Override
	public RestbasedMwUrl parseRestUrl(String restUrl) throws MalformedURLException {
		return new RestbasedMwUrl(restUrl);
		// return url;
	}

}
