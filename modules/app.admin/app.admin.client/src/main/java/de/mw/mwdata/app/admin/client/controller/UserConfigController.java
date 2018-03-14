package de.mw.mwdata.app.admin.client.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.mw.mwdata.app.admin.client.uimodel.UiUserConfig;
import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.rest.service.service.RestUrlService;

/**
 * Controller for serving all user specific and systemwide properties and
 * constants. <br>
 * TODO: class is in temporary state ...
 */
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequestMapping("/admin/userConfig/**")
public class UserConfigController {

	private RestUrlService urlService;
	private ApplicationConfigService configService;

	public void setUrlService(RestUrlService urlService) {
		this.urlService = urlService;
	}

	public void setApplicationConfigService(final ApplicationConfigService configService) {
		this.configService = configService;
	}

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UiUserConfig> loadSystemProperties() {

		String defaultEntity = this.configService.getPropertyValue("app.admin.defaultEntity");
		String restUrl = this.urlService.createUrlForReadEntities(defaultEntity);

		UiUserConfig userConfig = new UiUserConfig();
		userConfig.setDefaultRestUrl(restUrl);

		String sShowNotMappedColumnsInGrid = this.configService
				.getPropertyValue("app.admin.showNotMappedColumnsInGrid");
		Boolean show = Boolean.valueOf(sShowNotMappedColumnsInGrid);
		userConfig.setShowNotMappedColumnsInGrid(show.booleanValue());

		return new ResponseEntity<UiUserConfig>(userConfig, HttpStatus.OK);

	}

}
