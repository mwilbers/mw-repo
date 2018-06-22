package de.mw.mwdata.rest.control;

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

import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.service.IMenuService;
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

	public void setUrlService(RestUrlService urlService) {
		this.urlService = urlService;
	}

	public void setMenuService(IMenuService menuService) {
		this.menuService = menuService;
	}

	protected abstract UiMenuNode convertToUiMenu(final EntityTO menuEntity);

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<UiMenuNode>> listMainMenus() {

		// FIXME: return UI-object of menunodes with additional ofdb-infos (visible,
		// filterable, editable, etc.)

		List<EntityTO> menuResult = this.menuService.findMainMenus();
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

		return new ResponseEntity<List<UiMenuNode>>(menuList, HttpStatus.OK);
	}

	private String getServletName() {

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
		List<EntityTO> menuResult = this.menuService.findChildMenus(parentMenuId);

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

		return new ResponseEntity<List<UiMenuNode>>(menuList, HttpStatus.OK);
	}

}
