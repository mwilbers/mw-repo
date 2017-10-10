package de.mw.mwdata.core.web.navigation;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.web.util.RestbasedMwUrl;

public interface NavigationManager {

	public final String SESSIONKEY_NAVIGATIONSTATE = "de.mw.mwdata.core.web.navigationstate";

	/**
	 * Does a simple list-navigation with all parameters
	 * 
	 * @param pageIndex
	 * @param menue
	 */
	public void doList(final String urlPath, final Integer pageIndex, final String menue, final NavigationState state);

	/**
	 * Does a simple filter-navigation with the given filterSet-Object
	 * containing the entity and additional view-data mapped by the current
	 * view-definition.
	 * 
	 * @param filterSet
	 *            the entity-object with the values to filter for
	 * @param isFiltered
	 *            true if there is any real value to filter for in the filterSet
	 */
	public void doFilter(final EntityTO<? extends AbstractMWEntity> filterSet, final boolean isFiltered,
			final NavigationState state);

	/**
	 * Does a simple sort-navigation with all parameters
	 */
	public void doSort(final String col, final String asc, final NavigationState state);

	/**
	 * Does the navigation of all parameters for the update-event.
	 * 
	 * @param entityId
	 *            the id of the entity which has to be edited. Can be null, that
	 *            case new entity has to be inserted
	 * @param filterSet
	 *            the filterSet for predefinition of empty fields if new entity
	 *            has to be inserted
	 */
	public void doEdit(final Long entityId, final EntityTO<? extends AbstractMWEntity> filterSet,
			final NavigationState state);

	/**
	 * 
	 * @param requestedUrl
	 *            the requested url that is called
	 * @return
	 */
	public CrudNavigationState createNavigationState(final String requestedUrl,
			final EntityTO<? extends AbstractMWEntity> filterSet);

	/**
	 * The given url will be resolved for mwdata application and the
	 * {@link NavigationState} will be adjusted for resolved url
	 * 
	 * @param urlPath
	 * @param state
	 */
	public void applyUrlPath(final String requestUrl, final NavigationState state) throws NavigationException;

	public EntityTO<? extends AbstractMWEntity> createEmptyEntity(final String servletSubPath);

	/**
	 * Can read the url of the underlying servlet request and parse all
	 * necessary url tokens for navigation rules
	 * 
	 * @return
	 */
	public RestbasedMwUrl readUrl();

}
