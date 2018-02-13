package de.mw.mwdata.core.service;

/**
 * Global configuration service for every app that serves system wide and for user specific application properties
 *
 * @author WilbersM
 *
 */
public interface ApplicationConfigService {

	public static final String KEY_APPLICATION_URL = "app.applicationUrl";

	public void setPropertyBundle( final String bundleName );

	public String getPropertyValue( final String key );

}
