package de.mw.mwdata.rest;

import java.net.MalformedURLException;

import org.springframework.util.StringUtils;

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

	// FIXME move finally to config file
	private static final String URL_PATH_MENU = "nav/menu/{menuId}";
	private static final String URL_PATH_VIEW = "view";

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

	private String replaceUrlToken(final String url, final String pattern, final String newValue) {
		if (StringUtils.isEmpty(url)) {
			return url;
		}

		int index = url.indexOf(pattern);
		if (index > -1) {
			return url.replace(pattern, newValue);
		} else {
			return url;
		}

	}

	@Override
	public String createUrlForMenuItem(String servletName, long menuId) {
		String applicationUrl = this.configService.getPropertyValue(ApplicationConfigService.KEY_APPLICATION_URL);
		applicationUrl = addSafeContextPath(applicationUrl, servletName);
		applicationUrl = addSafeContextPath(applicationUrl, URL_PATH_MENU);
		return replaceUrlToken(applicationUrl, "{menuId}", String.valueOf(menuId));

		// applicationUrl = addSafeContextPath(applicationUrl, "menu");
		// return addSafeContextPath(applicationUrl, String.valueOf(menuId));
	}

}
