package de.mw.mwdata.rest.service.service;

import java.net.MalformedURLException;

import de.mw.mwdata.rest.service.RestbasedMwUrl;

/**
 * REST based url service for simple crud operations (CREATE, READ, UPDATE,
 * DELETE)
 * 
 * @author WilbersM
 *
 */
public interface RestUrlService {

	public String createUrlForReadEntities(final String entityName);

	public RestbasedMwUrl parseRestUrl(final String restUrl) throws MalformedURLException;

}
