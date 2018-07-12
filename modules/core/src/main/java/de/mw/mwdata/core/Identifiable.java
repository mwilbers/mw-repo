package de.mw.mwdata.core;

/**
 * Common interface for applying application wide unique identifiers to
 * arbitrary java objects.
 * 
 * @author WilbersM
 *
 */
public interface Identifiable {

	/**
	 * Creates unique identifier by concatenating the given token to an prefix that
	 * forms an application wide unique id
	 * 
	 * @param token
	 * @return
	 */
	public String createIdentifier(final String token);

}
