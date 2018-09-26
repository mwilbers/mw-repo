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

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.query.QueryResult;
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

	// protected abstract UiMenuNode convertToUiMenu(final EntityTO menuEntity);

	protected List<UiMenuNode> convertToUiMenuNodes(final QueryResult menuResult) {

		List<UiMenuNode> menuList = new ArrayList<>();

		// List<IEntity[]> objectArray = Utils.toObjectArray(menuResult.getRows());
		for (Object[] row : menuResult.getRows()) {
			UiMenuNode menu = convertToUiMenu((IEntity) row[0]);

			// FIXME: row[1] should be gained by result.metaData
			String urlPath = (String) row[1];
			if (!StringUtils.isEmpty(urlPath)) {
				String restUrl = this.urlService.createUrlForReadEntities(getServletName(), urlPath);
				menu.setRestUrl(restUrl);
			}
			menuList.add(menu);
		}
		return menuList;

	}

	protected UiMenuNode convertToUiMenu(final IEntity menuEntity) {

		IMenue menu = (IMenue) menuEntity;

		UiMenuNode node = new UiMenuNode();
		node.setId(menu.getId());
		node.setNodeType(menu.getTyp().name());
		node.setDisplayName(menu.getAnzeigeName() + " ( " + menu.getTyp().name() + " ) ");

		if (node.hasChildren()) {
			String menuUrl = this.getUrlService().createUrlForMenuItem(getServletName(), menu.getId());
			node.setUrl(menuUrl);
		}

		return node;
	}

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<UiMenuNode>> listMainMenus() {

		String defaultEntity = this.applicationConfigService
				.getPropertyValue(ApplicationConfigService.KEY_DEFAULT_ENTITY);
		List<UiMenuNode> selectedNodes = loadMenuPath(defaultEntity);

		// FIXME: return UI-object of menunodes with additional ofdb-infos (visible,
		// filterable, editable, etc.)
		String userAreaName = this.applicationConfigService.getPropertyValue(ApplicationConfigService.KEY_USERAREA);
		QueryResult menuResult = this.menuService.findMainMenus(userAreaName);

		List<UiMenuNode> menuList = convertToUiMenuNodes(menuResult); // convertToUiMenuList(entityTOs);

		if (!CollectionUtils.isEmpty(selectedNodes)) {
			List<UiMenuNode> currentNodes = menuList;

			for (int i = 0; i < selectedNodes.size() - 1; i++) { // for all nodes without the last node
				UiMenuNode selectedNode = selectedNodes.get(i);

				UiMenuNode parentNode = findNodeById(selectedNode.getId(), currentNodes);
				if (null != parentNode) {

					QueryResult childResult = this.menuService.findChildMenus(parentNode.getId(), userAreaName);

					List<UiMenuNode> uiChildNodes = this.convertToUiMenuNodes(childResult); // convertToUiMenuList(childEntityTOs);
					parentNode.addAllChildren(uiChildNodes);

					currentNodes = uiChildNodes;

					if (i == selectedNodes.size() - 2) { // for deepest layer look for leaf to mark as selected
						markSelected(uiChildNodes, selectedNodes.get(selectedNodes.size() - 1).getId());
					}

				}

			}

		}

		return new ResponseEntity<List<UiMenuNode>>(menuList, HttpStatus.OK);
	}

	private void markSelected(List<UiMenuNode> nodes, long id) {

		for (UiMenuNode node : nodes) {
			if (node.getId() == id) {
				node.setSelected(true);
				break;
			}
		}

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

		IMenue menu = (IMenue) this.menuService.findMenuByUrlPath(currentUrlPath);
		if (null == menu) {
			return menuPath;
		}

		// EntityTO entityTO = new EntityTO<AbstractMWEntity>((AbstractMWEntity) menu);

		UiMenuNode uiMenu = convertToUiMenu(menu);
		menuPath.add(uiMenu);

		// IMenue menu = (IMenue) entityTO.getItem();
		while (menu.getHauptMenueId() != null) {
			IMenue parentMenu = (IMenue) this.menuService.findParentMenu(menu.getHauptMenueId());

			// EntityTO parentMenuTO = new EntityTO<AbstractMWEntity>((AbstractMWEntity)
			// parentMenu);
			UiMenuNode uiParentMenu = convertToUiMenu(parentMenu);
			menuPath.add(0, uiParentMenu);

			menu = parentMenu;
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
		QueryResult menuResult = this.menuService.findChildMenus(parentMenuId, userAreaName);

		// List<EntityTO> entityTOs = convertToEntityTOList(menuResult.getRows(),
		// new JoinedPropertyTO("ansichtDef", "urlPath", 1));

		// FIXME : do convert directly from QueryResult to UiMenuNodes here
		List<UiMenuNode> menuList = this.convertToUiMenuNodes(menuResult); // convertToUiMenuList(entityTOs);

		return new ResponseEntity<List<UiMenuNode>>(menuList, HttpStatus.OK);
	}

}
