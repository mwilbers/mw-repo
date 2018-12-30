package de.mw.mwdata.rest.ofdb.control;

import java.util.ArrayList;
import java.util.List;

import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.rest.control.AbstractUserConfigController;
import de.mw.mwdata.rest.uimodel.UiInputConfig;

/**
 * Abstract base controller for serving ofdb based user and system wide
 * configuration properties.
 * 
 * @author WilbersM
 *
 */
public abstract class AbstractOfdbUserConfigController extends AbstractUserConfigController {

	private IOfdbService ofdbService;

	public void setOfdbService(IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	public List<UiInputConfig> loadUiInputConfigurations(final String viewName) {
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(viewName);

		List<UiInputConfig> configs = new ArrayList<>();
		if (null == viewDef) {
			return configs;
		}

		// #ViewLayout# FIXME: Use userService.applyViewLayout(
		// ofdbFields, userId, viewName )
		// better replace ofdbService - call here by userService.loadViewLayout(
		// viewName, userId, )
		List<OfdbField> ofdbFields = this.ofdbService.initializeOfdbFields(viewDef.getName());

		for (OfdbField field : ofdbFields) {
			configs.add(new UiInputConfig(field));
		}

		return configs;
	}

	public void updateViewLayout(final String viewName, final List<UiInputConfig> uiFields, final int userId) {
		// #ViewLayout# 3. save new view Layout here and synchronice client and server
		// states
	}

}
