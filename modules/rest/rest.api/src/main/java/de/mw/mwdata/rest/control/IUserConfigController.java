package de.mw.mwdata.rest.control;

import org.springframework.http.ResponseEntity;

import de.mw.mwdata.rest.uimodel.UiUserConfig;

public interface IUserConfigController {

	public ResponseEntity<UiUserConfig> loadSystemProperties();

}
