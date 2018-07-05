package de.mw.mwdata.app.admin.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;

import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.rest.control.AbstractUserConfigController;
import de.mw.mwdata.rest.uimodel.UiInputConfig;

/**
 * Controller for serving all user specific and systemwide properties and
 * constants. <br>
 * 
 */
@RequestMapping("/admin/userConfig/**")
public class UserConfigController extends AbstractUserConfigController {

	private IOfdbService ofdbService;

	public void setOfdbService(IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	public List<UiInputConfig> loadUiInputConfigurations(final String viewName) {
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(viewName);
		List<OfdbField> ofdbFields = this.ofdbService.initializeOfdbFields(viewDef.getName());

		List<UiInputConfig> configs = new ArrayList<>();
		for (OfdbField field : ofdbFields) {
			configs.add(new UiInputConfig(field));
		}

		return configs;
	}

}
