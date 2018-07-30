package de.mw.mwdata.rest;

import java.net.MalformedURLException;

import de.mw.mwdata.rest.url.RestUrl;

/**
 * REST based url service for simple crud operations (CREATE, READ, UPDATE,
 * DELETE)
 * 
 * @author WilbersM
 *
 */
public interface RestUrlService {

	public String createUrlForReadEntities(final String servletName, final String entityName);

	public RestUrl parseRestUrl(final String restUrl) throws MalformedURLException;

	public String createUrlForMenuItem(final String servletName, final long menuId);

}
