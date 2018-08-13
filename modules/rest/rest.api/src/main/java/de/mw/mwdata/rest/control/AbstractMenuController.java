package de.mw.mwdata.rest.control;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.core.service.IMenuService;
import de.mw.mwdata.ofdb.domain.IMenue;
import de.mw.mwdata.rest.RestUrlService;
import de.mw.mwdata.rest.uimodel.UiMenuNode;
import de.mw.mwdata.rest.utils.SessionUtils;

/**
 * Abstract base controller for providing ui menu tree with common actions for
 * navigating and selecting nodes.
 * 
 * @author WilbersM
 * 
 */
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class AbstractMenuController {

	private IMenuService menuService;
	private RestUrlService urlService;
	private ApplicationConfigService applicationConfigService;

	public void setUrlService(RestUrlService urlService) {
		this.urlService = urlService;
	}

	protected RestUrlService getUrlService() {
		return this.urlService;
	}

	public void setMenuService(IMenuService menuService) {
		this.menuService = menuService;
	}

	public ApplicationConfigService getApplicationConfigService() {
		return applicationConfigService;
	}

	public void setApplicationConfigService(ApplicationConfigService applicationConfigService) {
		this.applicationConfigService = applicationConfigService;
	}

	protected abstract UiMenuNode convertToUiMenu(final EntityTO menuEntity);

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<UiMenuNode>> listMainMenus() {

		String defaultEntity = this.applicationConfigService
				.getPropertyValue(ApplicationConfigService.KEY_DEFAULT_ENTITY);
		List<UiMenuNode> selectedNodes = loadMenuPath(defaultEntity);

		// FIXME: return UI-object of menunodes with additional ofdb-infos (visible,
		// filterable, editable, etc.)
		String userAreaName = this.applicationConfigService.getPropertyValue(ApplicationConfigService.KEY_USERAREA);
		List<EntityTO> menuResult = this.menuService.findMainMenus(userAreaName);
		List<UiMenuNode> menuList = convertToUiMenuList(menuResult);

		if (!CollectionUtils.isEmpty(selectedNodes)) {
			List<UiMenuNode> currentNodes = menuList;

			for (int i = 0; i < selectedNodes.size() - 1; i++) { // for all nodes without the last node
				UiMenuNode selectedNode = selectedNodes.get(i);

				UiMenuNode parentNode = findNodeById(selectedNode.getId(), currentNodes);
				if (null != parentNode) {

					List<EntityTO> childNodes = this.menuService.findChildMenus(parentNode.getId(), userAreaName);
					List<UiMenuNode> uiChildNodes = convertToUiMenuList(childNodes);
					parentNode.addAllChildren(uiChildNodes);

					currentNodes = uiChildNodes;
				}

			}

		}

		// for (EntityTO item : menuResult) {
		// UiMenuNode menu = convertToUiMenu(item);
		// if (!StringUtils.isEmpty(item.getJoinedValue("urlPath"))) {
		// String restUrl = this.urlService.createUrlForReadEntities(getServletName(),
		// item.getJoinedValue("urlPath"));
		// menu.setRestUrl(restUrl);
		// }
		// menuList.add(menu);
		// }

		return new ResponseEntity<List<UiMenuNode>>(menuList, HttpStatus.OK);
	}

	private UiMenuNode findNodeById(final long id, final List<UiMenuNode> menuList) {
		for (UiMenuNode node : menuList) {
			if (node.getId() == id) {
				return node;
			}
		}

		return null;
	}

	private List<UiMenuNode> loadMenuPath(final String currentUrlPath) {
		List<UiMenuNode> menuPath = new ArrayList<>();

		EntityTO<AbstractMWEntity> menuTO = this.menuService.findMenuByUrlPath(currentUrlPath);
		UiMenuNode uiMenu = convertToUiMenu(menuTO);
		menuPath.add(uiMenu);

		IMenue menu = (IMenue) menuTO.getItem();
		while (menu.getHauptMenueId() != null) {
			EntityTO<AbstractMWEntity> parentMenuTO = this.menuService.findParentMenu(menu.getHauptMenueId());
			UiMenuNode uiParentMenu = convertToUiMenu(parentMenuTO);
			menuPath.add(0, uiParentMenu);

			menu = (IMenue) parentMenuTO.getItem();
		}

		return menuPath;
	}

	protected String getServletName() {

		String servletPath = SessionUtils.getHttpServletRequest().getServletPath();
		if (servletPath.startsWith("/")) {
			servletPath = servletPath.substring(1);
		}
		String[] urlTokens = StringUtils.split(servletPath, "/");
		return urlTokens[0];
	}

	@RequestMapping(value = "/menu/{parentMenuId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<UiMenuNode>> listChildMenus(@PathVariable("parentMenuId") int parentMenuId) {

		// // FIXME: compare with where-restrictions from OfdbDao.findMenues()
		String userAreaName = this.applicationConfigService.getPropertyValue(ApplicationConfigService.KEY_USERAREA);
		List<EntityTO> menuResult = this.menuService.findChildMenus(parentMenuId, userAreaName);
		List<UiMenuNode> menuList = convertToUiMenuList(menuResult);

		return new ResponseEntity<List<UiMenuNode>>(menuList, HttpStatus.OK);
	}

	private List<UiMenuNode> convertToUiMenuList(final List<EntityTO> menuResult) {

		List<UiMenuNode> menuList = new ArrayList<>();
		for (EntityTO item : menuResult) {
			UiMenuNode menu = convertToUiMenu(item);
			if (!StringUtils.isEmpty(item.getJoinedValue("urlPath"))) {
				String restUrl = this.urlService.createUrlForReadEntities(getServletName(),
						item.getJoinedValue("urlPath"));
				menu.setRestUrl(restUrl);
			}
			menuList.add(menu);
		}
		return menuList;
	}

}
