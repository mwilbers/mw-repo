package de.mw.mwdata.ofdb.cache;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;

public class DefaultOfdbCacheManager implements OfdbCacheManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOfdbCacheManager.class);

	// FIXME: cache should be moved to ehcache
	private OfdbCache ofdbCache;

	public DefaultOfdbCacheManager() {
		this.ofdbCache = OfdbCache.createInstance();
	}

	/**
	 *
	 * @param tableName
	 * @return a map containing the key = tabSpeig.spalte and value = propertyname
	 *         of the requested tableName
	 */
	@Override
	public OfdbEntityMapping getEntityMapping(final String tableName) {
		return this.ofdbCache.getEntityMapping(tableName);
	}

	@Override
	public void registerView(final ViewConfigHandle viewHandle) {
		this.addViewConfiguration(viewHandle);

		List<IAnsichtTab> ansichtTabList = viewHandle.getViewTabs();
		boolean isReferenced = false;
		if (null == ansichtTabList) {
			return;
		}
		for (IAnsichtTab ansichtTab : ansichtTabList) {

			// check if TabDef is referenced in any other AnsichtTab
			ITabDef tabDef = ansichtTab.getTabDef();
			isReferenced = this.isReferencedByCachedAnsichtTab(tabDef, ansichtTab);
			if (!isReferenced) {
				this.ofdbCache.addPropertyMap(tabDef.getName(), viewHandle.getEntityMapping(tabDef.getName()));
			}
		}

	}

	@Override
	public void unregisterView(final String viewName) {

		ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig(viewName);
		if (null == viewHandle) {
			return;
		}

		List<IAnsichtTab> ansichtTabList = viewHandle.getViewTabs();
		// // for each ansichtTab search if tabDef is used in any other ansichtTab
		boolean isReferenced = false;
		if (null != ansichtTabList) {

			for (IAnsichtTab ansichtTab : ansichtTabList) {

				// check if TabDef is referenced in any other AnsichtTab
				ITabDef tabDef = ansichtTab.getTabDef();
				isReferenced = this.isReferencedByCachedAnsichtTab(tabDef, ansichtTab);
				if (!isReferenced) {
					this.ofdbCache.removePropertyMap(tabDef.getName());
				}
			}
		}

		this.ofdbCache.removeViewData(viewName);

	}

	@Override
	public boolean isViewRegistered(final String viewName) {

		ViewConfigHandle viewHandle = this.getViewConfig(viewName);
		if (null == viewHandle) {
			return false;
		}
		IAnsichtTab ansichtTab = viewHandle.getMainAnsichtTab();
		if (null == ansichtTab) {
			return false;
		}
		ITabDef tabDef = viewHandle.getTableByName(ansichtTab.getTabDef().getName());
		return (null != tabDef);

	}

	public void addViewConfiguration(final ViewConfigHandle viewHandle) {
		this.ofdbCache.putViewConfiguration(viewHandle.getViewDef().getName(), viewHandle.getViewConfiguration());

	}

	@Override
	public ViewConfigHandle getViewConfig(final String viewName) {
		return this.ofdbCache.getViewConfig(viewName);
	}

	@Override
	public ITabDef findRegisteredTableDef(final String tableName) {

		for (String viewName : this.ofdbCache) {
			ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig(viewName);

			ITabDef tableDef = viewHandle.findTableDefByName(tableName);
			if (null != tableDef) {
				return tableDef;
			}

		}

		return null;
	}

	private boolean isReferencedByCachedAnsichtTab(final ITabDef tabDef, final IAnsichtTab ansichtTabToIgnore) {

		for (String viewName : this.ofdbCache) {
			ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig(viewName);

			for (IAnsichtTab viewTab : viewHandle.getViewTabs()) {
				if (viewTab.equals(ansichtTabToIgnore)) {
					continue;
				}

				if (viewTab.getTabDef().equals(tabDef)) {
					return true;
				}

			}

		}

		return false;
	}

	@Override
	public ViewConfigHandle findViewConfigByTableName(final String tableName) {

		for (String viewName : this.ofdbCache) {
			ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig(viewName);
			if (viewHandle.getMainAnsichtTab().getTabDef().getName().equals(tableName)) {
				return viewHandle;
			}
		}

		return null;
	}

	@Override
	public List<ViewConfigHandle> getRegisteredViewConfigs() {

		List<ViewConfigHandle> viewConfigs = new ArrayList<ViewConfigHandle>();
		for (String viewName : this.ofdbCache) {
			ViewConfigHandle viewHandle = this.ofdbCache.getViewConfig(viewName);
			viewConfigs.add(viewHandle);
		}

		return viewConfigs;
	}

}
