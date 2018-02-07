package de.mw.mwdata.app.admin.client.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.mw.mwdata.app.admin.client.uimodel.UiUserConfig;

/**
 * Controller for serving all user specific and systemwide properties and
 * constants. <br>
 * TODO: class is in temporary state ...
 */
@RestController
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequestMapping("/admin/userConfig/**")
public class UserConfigController {

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	public ResponseEntity<UiUserConfig> loadSystemProperties() {

		UiUserConfig userConfig = new UiUserConfig();
		return new ResponseEntity<UiUserConfig>(userConfig, HttpStatus.OK);

	}

}
