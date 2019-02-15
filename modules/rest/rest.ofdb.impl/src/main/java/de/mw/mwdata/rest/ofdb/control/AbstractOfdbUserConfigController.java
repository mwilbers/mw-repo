package de.mw.mwdata.rest.ofdb.control;

import java.util.List;

import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.rest.control.AbstractUserConfigController;
import de.mw.mwdata.rest.uimodel.UiInputConfig;
import de.mw.mwdata.rest.uimodel.UiViewConfig;

/**
 * Abstract base controller for serving ofdb based user and system wide
 * configuration properties.
 * 
 * @author WilbersM
 *
 */
public abstract class AbstractOfdbUserConfigController extends AbstractUserConfigController {

	private IOfdbService ofdbService;
	private OfdbCacheManager ofdbCacheManager;

	public void setOfdbService(IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	public void setOfdbCacheManager(OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	public UiViewConfig loadUiInputConfigurations(final String urlPath) {
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(urlPath);

		if (null == viewDef) {
			return UiViewConfig.createEmptyUiViewConfig();
		}

		UiViewConfig uiViewConfig = new UiViewConfig();

		// #ViewLayout# FIXME: Use userService.applyViewLayout(
		// ofdbFields, userId, viewName )
		// better replace ofdbService - call here by userService.loadViewLayout(
		// viewName, userId, )
		List<OfdbField> ofdbFields = this.ofdbService.initializeOfdbFields(viewDef.getName());
		for (OfdbField field : ofdbFields) {
			uiViewConfig.addUiInputConfig(new UiInputConfig(field));
		}

		return uiViewConfig;
	}

	public void updateViewLayout(final String viewName, final List<UiInputConfig> uiFields, final int userId) {
		// #ViewLayout# 3. save new view Layout here and synchronice client and server
		// states
	}

	@Override
	public String getEntityNameByUrlPathToken(final String urlPath) {
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(urlPath);
		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(viewDef.getName());
		return viewHandle.getMainAnsichtTab().getTabDef().getFullClassName();
	}

}
