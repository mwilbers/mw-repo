package de.mw.mwdata.ofdb.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mw.mwdata.core.ApplicationFactory;
import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigFactory;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.service.IOfdbService;

/**
 * Central base class for building up MW Data Application. An MW Data
 * Application runs a configuration phase where ofdb data has to be configured,
 * an initialization phase for loading the ofdb cache and the running phase when
 * applications runs.
 *
 * @author mwilbers
 *
 */
public class DefaultApplicationFactory implements ApplicationFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultApplicationFactory.class);

	private IOfdbService ofdbService;

	protected ViewConfigFactory viewConfigFactory;

	protected OfdbCacheManager ofdbCacheManager;

	// protected String nameBenutzerBereich;

	private ApplicationConfigService applicationConfigService;

	public void setViewConfigFactory(final ViewConfigFactory viewConfigFactory) {
		this.viewConfigFactory = viewConfigFactory;
	}

	public ApplicationConfigService getApplicationConfigService() {
		return applicationConfigService;
	}

	public void setApplicationConfigService(ApplicationConfigService applicationConfigService) {
		this.applicationConfigService = applicationConfigService;
	}

	public DefaultApplicationFactory() {
	}

	public void setOfdbService(final IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	@Override
	public void init() {
		// this.state = ApplicationState.INITIALIZE;

		// FIXME: here add feature toggle for loading or not loading viewConfigs to
		// cache ...

		String userAreaName = this.applicationConfigService.getPropertyValue(ApplicationConfigService.KEY_USERAREA);
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

	public void setOfdbCacheManager(final OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

}
