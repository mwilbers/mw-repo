package de.mw.mwdata.rest.control;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.rest.RestUrlService;
import de.mw.mwdata.rest.uimodel.UiEntityList;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class AbstractCrudController<E extends AbstractMWEntity> {

	private RestUrlService urlService;

	public void setUrlService(RestUrlService urlService) {
		this.urlService = urlService;
	}

	protected RestUrlService getUrlService() {
		return this.urlService;
	}

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	public abstract ResponseEntity<UiEntityList<E>> listAllEntities(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize);

	@RequestMapping(value = "**/{id}", method = RequestMethod.PUT)
	public abstract ResponseEntity<EntityTO<E>> updateEntity(@PathVariable("id") long id, @RequestBody E entity);

	@RequestMapping(value = "**/", method = RequestMethod.POST)
	public abstract ResponseEntity<UiEntityList<E>> filterEntities(@RequestParam("filter") String filter,
			@RequestBody E entity);

}
