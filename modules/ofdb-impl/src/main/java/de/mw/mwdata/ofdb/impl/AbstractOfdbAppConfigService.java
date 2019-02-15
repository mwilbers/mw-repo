package de.mw.mwdata.ofdb.impl;

import java.util.List;

import de.mw.mwdata.core.service.AbstractApplicationConfigService;
import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigFactory;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.service.IOfdbService;

public abstract class AbstractOfdbAppConfigService extends AbstractApplicationConfigService {

	private IOfdbService ofdbService;
	protected ViewConfigFactory viewConfigFactory;
	protected OfdbCacheManager ofdbCacheManager;

	public AbstractOfdbAppConfigService(String bundleName) {
		super(bundleName);
		// TODO Auto-generated constructor stub
	}

	public void setViewConfigFactory(final ViewConfigFactory viewConfigFactory) {
		this.viewConfigFactory = viewConfigFactory;
	}

	public void setOfdbService(final IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	public void setOfdbCacheManager(final OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	@Override
	public void initApplication() {
		// this.state = ApplicationState.INITIALIZE;

		// FIXME: here add feature toggle for loading or not loading viewConfigs to
		// cache ...

		String userAreaName = getPropertyValue(ApplicationConfigService.KEY_USERAREA);
		List<AnsichtDef> viewList = this.ofdbService.loadViewsForRegistration(userAreaName);

		if (null == viewList) {
			return;
		}

		for (AnsichtDef ansichtDef : viewList) {
			if (this.ofdbCacheManager.isViewRegistered(ansichtDef.getName())) {
				continue;
			}

			// register all ofdb-relevant data of the given view to cache
			ViewConfigHandle viewHandle = this.viewConfigFactory.createViewConfiguration(ansichtDef.getName());
			this.ofdbCacheManager.registerView(viewHandle);

		}

		// this.state = ApplicationState.RUNNING;
	}

}
