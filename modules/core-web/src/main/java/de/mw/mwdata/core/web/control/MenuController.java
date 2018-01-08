package de.mw.mwdata.core.web.control;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.domain.UiEntityList;
import de.mw.mwdata.core.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.core.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.core.ofdb.domain.Menue;
import de.mw.mwdata.core.service.IMenuService;

@RestController
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequestMapping("/admin/nav/**")
public class MenuController {

	private IMenuService menuService;

	private OfdbCacheManager ofdbCacheManager;

	public void setMenuService(IMenuService menuService) {
		this.menuService = menuService;
	}

	public void setOfdbCacheManager(OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	// ... 1. add new method listDefaultMenues -> menues von ebene 1 laden
	// 2. listAllTabDefs ohne argument machen
	// 3. menueController.js anpassen: data-object aus uiEntities transformieren ->
	// oder besser hier in java ?

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	public ResponseEntity<UiEntityList<Menue>> listMainMenus() {

		// initialize ofPropList

		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);

		List<IEntity[]> menuResult = this.menuService.findMainMenus();
		UiEntityList<Menue> uiEntities = new UiEntityList<Menue>(menuResult, viewHandle.getOfdbFieldList());

		return new ResponseEntity<UiEntityList<Menue>>(uiEntities, HttpStatus.OK);

	}

	@RequestMapping(value = "/menu/{parentMenuId}", method = RequestMethod.GET)
	public ResponseEntity<UiEntityList<Menue>> listChildMenus(@PathVariable("parentMenuId") int parentMenuId) {

		// initialize ofPropList
		ViewConfigHandle viewHandle = this.ofdbCacheManager.findViewConfigByTableName(Constants.SYS_TAB_MENUS);

		// // FIXME: compare with where-restrictions from OfdbDao.findMenues()
		List<IEntity[]> menuResult = this.menuService.findChildMenus(parentMenuId);

		UiEntityList<Menue> uiEntities = new UiEntityList<Menue>(menuResult, viewHandle.getOfdbFieldList());
		return new ResponseEntity<UiEntityList<Menue>>(uiEntities, HttpStatus.OK);
	}

}
