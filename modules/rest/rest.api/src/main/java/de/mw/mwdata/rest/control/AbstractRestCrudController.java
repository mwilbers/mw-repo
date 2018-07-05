package de.mw.mwdata.rest.control;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.rest.RestUrlService;
import de.mw.mwdata.rest.navigation.NavigationManager;
import de.mw.mwdata.rest.uimodel.UiEntityList;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class AbstractRestCrudController<E extends AbstractMWEntity> {

	private NavigationManager navigationManager;

	private RestUrlService urlService;

	public void setUrlService(RestUrlService urlService) {
		this.urlService = urlService;
	}

	protected RestUrlService getUrlService() {
		return this.urlService;
	}

	public void setNavigationManager(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
	}

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	public abstract ResponseEntity<UiEntityList<E>> listAllEntities();

	@RequestMapping(value = "**/tabDef/{id}", method = RequestMethod.PUT)
	public abstract ResponseEntity<EntityTO<E>> updateEntity(@PathVariable("id") long id, @RequestBody E entity);

	/**
	 * Populates the empty filterObject for spring-web-binding. Spring does set the
	 * properties of the choosen filter-options. <br>
	 * FIXME: filter method still needed ? filtering in slickgrid clientside ...
	 * 
	 */
	@ModelAttribute("filterSet")
	public EntityTO<? extends AbstractMWEntity> populateFilterSet() {
		return null;
	}

}
