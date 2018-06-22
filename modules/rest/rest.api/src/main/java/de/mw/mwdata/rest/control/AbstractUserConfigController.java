package de.mw.mwdata.rest.control;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.rest.RestUrlService;
import de.mw.mwdata.rest.uimodel.UiUserConfig;
import de.mw.mwdata.rest.url.RestUrl;
import de.mw.mwdata.rest.utils.SessionUtils;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class AbstractUserConfigController implements IUserConfigController {

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

		RestUrl url = new RestUrl(SessionUtils.getHttpServletRequest().getRequestURL().toString());

		String defaultEntity = this.configService.getPropertyValue("app.defaultEntity");
		String restUrl = this.urlService.createUrlForReadEntities(url.getServletPath(), defaultEntity);

		UiUserConfig userConfig = new UiUserConfig();
		userConfig.setDefaultRestUrl(restUrl);

		String sShowNotMappedColumnsInGrid = this.configService.getPropertyValue("app.showNotMappedColumnsInGrid");
		Boolean show = Boolean.valueOf(sShowNotMappedColumnsInGrid);
		userConfig.setShowNotMappedColumnsInGrid(show.booleanValue());

		return new ResponseEntity<UiUserConfig>(userConfig, HttpStatus.OK);

	}

}
