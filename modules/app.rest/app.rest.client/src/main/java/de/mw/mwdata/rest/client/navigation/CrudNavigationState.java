package de.mw.mwdata.rest.client.navigation;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.rest.service.RestbasedMwUrl;

public class CrudNavigationState implements NavigationState {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8646212390797333822L;

	private NAVIGATIONACTION action;
	private String urlPath;

	/**
	 * ID of the entity that has to be worked on. 0 if no entity is selected
	 */
	private long entityId;
	private String menue;
	private int pageIndex;

	private EntityTO<? extends AbstractMWEntity> filterSet;
	private boolean isFiltered = false;

	/**
	 * the ordered List of Columns the entities have to be sorted by. Key is
	 * columnname, value is asc/desc (ascending/descending)
	 */
	protected List<SortKey> sortKeys = new ArrayList<SortKey>();

	public CrudNavigationState(final String requestedUrl, final EntityTO<? extends AbstractMWEntity> filterSet) {
		this.pageIndex = 1;
		this.action = NAVIGATIONACTION.LIST;
		this.filterSet = filterSet; // here empty filterSet

		initUrl(requestedUrl);

	}

	private void initUrl(final String requestedUrl) {

		RestbasedMwUrl url = null;
		try {
			url = new RestbasedMwUrl(requestedUrl);
		} catch (MalformedURLException e) {
			String msg = MessageFormat.format("Invalid url {0} for MWData application.", requestedUrl);
			throw new NavigationException(msg);
		}

		this.urlPath = url.getEntityName();
	}

	public void setAction(final NAVIGATIONACTION action) {
		this.action = action;
	}

	@Override
	public NAVIGATIONACTION getAction() {
		return this.action;
	}

	@Override
	public String getUrlPath() {
		return urlPath;
	}

	@Override
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	/**
	 * Sets the ID of the current entity. 0 if no entity has to be selected.
	 * 
	 * @param entityId
	 */
	@Override
	public void setEntityId(final long entityId) {
		this.entityId = entityId;
	}

	@Override
	public long getEntityId() {
		return this.entityId;
	}

	@Override
	public void setMenue(final String menue) {
		this.menue = menue;
	}

	@Override
	public String getMenue() {
		return this.menue;
	}

	@Override
	public void setPageIndex(final int pageIndex) {
		// if ( null == pageIndex ) {
		// this.pageIndex = 1;
		// } else {
		this.pageIndex = pageIndex;
		// }
	}

	@Override
	public int getPageIndex() {
		return this.pageIndex;
	}

	private void clearSorting() {
		this.sortKeys.clear();

	}

	public void addSorting(final String col, final String ascOrDesc) {
		this.sortKeys.add(new SortKey(col, ascOrDesc));

	}

	@Override
	public List<SortKey> getSorting() {
		return this.sortKeys;
	}

	@Override
	public void setFilterSet(final EntityTO<? extends AbstractMWEntity> filterSet) {
		this.filterSet = filterSet;
	}

	@Override
	public EntityTO<? extends AbstractMWEntity> getFilterSet() {
		return this.filterSet;
	}

	@Override
	public void setFiltered(final boolean isFiltered) {
		this.isFiltered = isFiltered;
	}

	@Override
	public boolean isFiltered() {
		return this.isFiltered;
	}

	@Override
	public void setSorting(List<SortKey> sortKeys) {
		this.clearSorting();
		this.sortKeys = new ArrayList<SortKey>(sortKeys);
	}

}
