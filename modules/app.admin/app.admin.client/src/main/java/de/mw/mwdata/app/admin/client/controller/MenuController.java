package de.mw.mwdata.app.admin.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.mw.mwdata.app.admin.client.uimodel.UiMenuNode;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.ofdb.domain.Menue;
import de.mw.mwdata.core.service.IMenuService;

@RestController
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequestMapping("/admin/nav/**")
public class MenuController {

	@Autowired
	private IMenuService menuService;

	public void setMenuService(IMenuService menuService) {
		this.menuService = menuService;
	}

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	public ResponseEntity<List<UiMenuNode>> listMainMenus() {

		// initialize ofPropList

		List<EntityTO> menuResult = this.menuService.findMainMenus();
		List<UiMenuNode> menuList = new ArrayList<>();
		for (EntityTO item : menuResult) {
			UiMenuNode menu = convertToUiMenu(item);
			menuList.add(menu);
		}

		return new ResponseEntity<List<UiMenuNode>>(menuList, HttpStatus.OK);
	}

	private UiMenuNode convertToUiMenu(final EntityTO menuEntity) {

		Menue menu = (Menue) menuEntity.getItem();

		UiMenuNode node = new UiMenuNode();
		node.setId(menu.getId());
		node.setNodeType(menu.getTyp().name());
		node.setDisplayName(menu.getAnzeigeName() + " ( " + menu.getTyp().name() + " ) ");
		node.setRestUrl(menuEntity.getJoinedValue("urlPath"));

		if (node.hasChildren()) {
			node.setUrl("nav/menu/" + menu.getId());
		}

		return node;
	}

	@RequestMapping(value = "/menu/{parentMenuId}", method = RequestMethod.GET)
	public ResponseEntity<List<UiMenuNode>> listChildMenus(@PathVariable("parentMenuId") int parentMenuId) {

		// // FIXME: compare with where-restrictions from OfdbDao.findMenues()
		List<EntityTO> menuResult = this.menuService.findChildMenus(parentMenuId);

		List<UiMenuNode> menuList = new ArrayList<>();
		for (EntityTO item : menuResult) {
			UiMenuNode menu = convertToUiMenu(item);
			menuList.add(menu);
		}

		return new ResponseEntity<List<UiMenuNode>>(menuList, HttpStatus.OK);
	}

}
