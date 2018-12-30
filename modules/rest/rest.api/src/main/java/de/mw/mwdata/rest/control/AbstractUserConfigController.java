package de.mw.mwdata.rest.control;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.rest.RestUrlService;
import de.mw.mwdata.rest.uimodel.UiInputConfig;
import de.mw.mwdata.rest.uimodel.UiUserConfig;
import de.mw.mwdata.rest.url.RestUrl;
import de.mw.mwdata.rest.utils.SessionUtils;

// FIXME: session scope correct here?
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class AbstractUserConfigController implements IUserConfigController {

	private RestUrlService urlService;
	private ApplicationConfigService configService;

	public abstract List<UiInputConfig> loadUiInputConfigurations(final String viewName);

	public void setUrlService(RestUrlService urlService) {
		this.urlService = urlService;
	}

	public void setApplicationConfigService(final ApplicationConfigService configService) {
		this.configService = configService;
	}

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UiUserConfig> loadSystemProperties() {

		RestUrl url = new RestUrl(SessionUtils.getHttpServletRequest().getRequestURL().toString());
		String identifierUrlPath = this.configService.createIdentifier(SessionUtils.MW_SESSION_URLPATH);
		String lastUrlPath = (String) SessionUtils.getAttribute(SessionUtils.getHttpServletRequest().getSession(),
				identifierUrlPath);

		if (StringUtils.isEmpty(lastUrlPath)) {
			String defaultEntity = this.configService.getPropertyValue(ApplicationConfigService.KEY_DEFAULT_ENTITY);
			lastUrlPath = defaultEntity;
		}
		String restUrl = this.urlService.createUrlForReadEntities(url.getServletPath(), lastUrlPath);

		UiUserConfig userConfig = new UiUserConfig();
		userConfig.setDefaultRestUrl(restUrl);

		List<UiInputConfig> uiInputConfigs = loadUiInputConfigurations(lastUrlPath);
		userConfig.setUiInputConfigs(uiInputConfigs);

		String sShowNotMappedColumnsInGrid = this.configService
				.getPropertyValue(ApplicationConfigService.KEY_SHOW_NOT_MAPPED_COLS);
		Boolean show = Boolean.valueOf(sShowNotMappedColumnsInGrid);
		userConfig.setShowNotMappedColumnsInGrid(show.booleanValue());

		String sShowSystemColumns = this.configService
				.getPropertyValue(ApplicationConfigService.KEY_SHOW_SYSTEM_COLUMNS);
		show = Boolean.valueOf(sShowSystemColumns);
		userConfig.setShowSystemColumns(show.booleanValue());

		return new ResponseEntity<UiUserConfig>(userConfig, HttpStatus.OK);

	}

}
