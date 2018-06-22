package de.mw.mwdata.rest.navigation;

import java.io.Serializable;
import java.util.List;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.utils.SortKey;

/**
 * A NavigationState hold all state information for the current user navigation
 * position like the url, the sorting and filtering informations or paging
 * information.
 * 
 * @author mwilbers
 *
 */
public interface NavigationState extends Serializable {

	enum NAVIGATIONACTION {
		LIST, EDIT, SAVE, SORT;
	}

	public NAVIGATIONACTION getAction();

	/**
	 * Returns the urlPath of the current called view.
	 * 
	 * @return
	 */
	public String getUrlPath();

	public void setUrlPath(final String urlPath);

	/**
	 * Returns the current paging-index of the last call of the current page
	 * 
	 * @return
	 */
	public int getPageIndex();

	public void setPageIndex(final int index);

	/**
	 * Returns a list containing sort column key objects
	 * 
	 * @return
	 */
	public List<SortKey> getSorting();

	public void setSorting(final List<SortKey> sortKeys);

	/**
	 * Returns the current menueid
	 * 
	 * @return
	 */
	public String getMenue();

	public void setMenue(final String menue);

	/**
	 * Returns the {@link EntityTO}-object simulating the filter-state of all
	 * filterfields from the current view
	 * 
	 * @return
	 */
	public EntityTO<? extends AbstractMWEntity> getFilterSet();

	public void setFilterSet(final EntityTO<? extends AbstractMWEntity> filterEntity);

	/**
	 * ID of the entity that has to be worked on. 0 if no entity is selected
	 */
	public long getEntityId();

	public void setEntityId(final long entityId);

	public boolean isFiltered();

	public void setFiltered(final boolean filtered);

}
