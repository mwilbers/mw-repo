package de.mw.mwdata.app.calendar.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import de.mw.mwdata.rest.control.AbstractMenuController;

@RequestMapping("/caladmin/nav/**")
public class CalAdminMenuController extends AbstractMenuController {

	// protected UiMenuNode convertToUiMenu(final EntityTO menuEntity) {
	//
	// IMenue menu = (IMenue) menuEntity.getItem();
	//
	// UiMenuNode node = new UiMenuNode();
	// node.setId(menu.getId());
	// node.setNodeType(menu.getTyp().name());
	// node.setDisplayName(menu.getAnzeigeName() + " ( " + menu.getTyp().name() + "
	// ) ");
	//
	// if (node.hasChildren()) {
	// String menuUrl = this.getUrlService().createUrlForMenuItem(getServletName(),
	// menu.getId());
	// node.setUrl(menuUrl);
	// }
	//
	// return node;
	// }

}