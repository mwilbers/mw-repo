package de.mw.mwdata.rest.control;

import org.springframework.http.ResponseEntity;

import de.mw.mwdata.rest.uimodel.UiUserConfig;

/**
 * Common interface for serving user and system wide properties for running
 * application
 * 
 * @author WilbersM
 *
 */
public interface IUserConfigController {

	public ResponseEntity<UiUserConfig> loadUserConfiguration(final int userId);

	public String loadUserBasedUrlPathToken();

}
