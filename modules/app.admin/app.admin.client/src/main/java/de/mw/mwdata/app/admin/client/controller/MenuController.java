package de.mw.mwdata.app.admin.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.mw.mwdata.app.admin.client.uimodel.UiMenuNode;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.service.IMenuService;
import de.mw.mwdata.ofdb.domain.impl.Menue;
import de.mw.mwdata.rest.service.service.RestUrlService;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequestMapping("/admin/nav/**")
public class MenuController {

	private IMenuService menuService;

	private RestUrlService urlService;

	public void setMenuService(IMenuService menuService) {
		this.menuService = menuService;
	}

	public void setUrlService(RestUrlService urlService) {
		this.urlService = urlService;
	}

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<UiMenuNode>> listMainMenus() {

		// FIXME: return UI-object of menunodes with additional ofdb-infos (visible,
		// filterable, editable, etc.)

		// initialize ofPropList

		List<EntityTO> menuResult = this.menuService.findMainMenus();
		List<UiMenuNode> menuList = new ArrayList<>();

		for (EntityTO item : menuResult) {
			UiMenuNode menu = convertToUiMenu(item);
			if (!StringUtils.isEmpty(item.getJoinedValue("urlPath"))) {
				String restUrl = this.urlService.createUrlForReadEntities(item.getJoinedValue("urlPath"));
				menu.setRestUrl(restUrl);
			}
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

		if (node.hasChildren()) {
			node.setUrl("nav/menu/" + menu.getId());
		}

		return node;
	}

	@RequestMapping(value = "/menu/{parentMenuId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<UiMenuNode>> listChildMenus(@PathVariable("parentMenuId") int parentMenuId) {

		// // FIXME: compare with where-restrictions from OfdbDao.findMenues()
		List<EntityTO> menuResult = this.menuService.findChildMenus(parentMenuId);

		List<UiMenuNode> menuList = new ArrayList<>();
		for (EntityTO item : menuResult) {
			UiMenuNode menu = convertToUiMenu(item);
			if (!StringUtils.isEmpty(item.getJoinedValue("urlPath"))) {
				String restUrl = this.urlService.createUrlForReadEntities(item.getJoinedValue("urlPath"));
				menu.setRestUrl(restUrl);
			}
			menuList.add(menu);
		}

		return new ResponseEntity<List<UiMenuNode>>(menuList, HttpStatus.OK);
	}

}
