package de.mw.mwdata.rest.control;

import java.net.MalformedURLException;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.rest.RestUrlService;
import de.mw.mwdata.rest.uimodel.UiUserConfig;
import de.mw.mwdata.rest.uimodel.UiViewConfig;
import de.mw.mwdata.rest.utils.SessionUtils;
import de.mw.mwdata.rest.utils.UrlUtils;

// FIXME: session scope correct here?
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class AbstractUserConfigController implements IUserConfigController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUserConfigController.class);

	private RestUrlService urlService;
	private ApplicationConfigService applicationConfigService;

	public abstract UiViewConfig loadUiInputConfigurations(final String viewName);

	public void setUrlService(RestUrlService urlService) {
		this.urlService = urlService;
	}

	public void setApplicationConfigService(final ApplicationConfigService configService) {
		this.applicationConfigService = configService;
	}

	@RequestMapping(value = "**/userId/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UiUserConfig> loadUserConfiguration(@PathVariable("userId") final int userId) {

		String lastUrlPathToken = loadUserBasedUrlPathToken();
		String currentUrl = SessionUtils.getHttpServletRequest().getRequestURL().toString();
		String restUrl = org.apache.commons.lang.StringUtils.EMPTY;
		try {
			restUrl = this.urlService.createUrlForReadEntities(UrlUtils.getPathToken(currentUrl, 1), lastUrlPathToken);
		} catch (MalformedURLException e) {
			String message = MessageFormat.format("Invalid url path in url {0}", currentUrl);
			LOGGER.error(message);
		}

		UiUserConfig userConfig = new UiUserConfig();
		userConfig.setDefaultRestUrl(restUrl);

		UiViewConfig uiViewConfig = loadUiInputConfigurations(lastUrlPathToken);
		userConfig.setUiInputConfigs(uiViewConfig.getUiInputConfigs());

		String sShowNotMappedColumnsInGrid = this.applicationConfigService
				.getPropertyValue(ApplicationConfigService.KEY_SHOW_NOT_MAPPED_COLS);
		Boolean show = Boolean.valueOf(sShowNotMappedColumnsInGrid);
		userConfig.setShowNotMappedColumnsInGrid(show.booleanValue());

		String sShowSystemColumns = this.applicationConfigService
				.getPropertyValue(ApplicationConfigService.KEY_SHOW_SYSTEM_COLUMNS);
		show = Boolean.valueOf(sShowSystemColumns);
		userConfig.setShowSystemColumns(show.booleanValue());

		return new ResponseEntity<UiUserConfig>(userConfig, HttpStatus.OK);

	}

	public String loadUserBasedUrlPathToken() {

		String identifierUrlPathToken = this.applicationConfigService.createIdentifier(SessionUtils.MW_SESSION_URLPATH);
		String lastUrlPathToken = (String) SessionUtils.getAttribute(SessionUtils.getHttpServletRequest().getSession(),
				identifierUrlPathToken);

		if (StringUtils.isEmpty(lastUrlPathToken)) {
			String defaultEntity = this.applicationConfigService
					.getPropertyValue(ApplicationConfigService.KEY_DEFAULT_ENTITY);
			lastUrlPathToken = defaultEntity;
		}

		return lastUrlPathToken;
	}

}
