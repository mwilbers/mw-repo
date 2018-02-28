package de.mw.mwdata.rest.client.control;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.service.IPagingEntityService;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.impl.OfdbField;
import de.mw.mwdata.ofdb.impl.UiEntityList;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.rest.client.navigation.NavigationManager;
import de.mw.mwdata.rest.client.navigation.NavigationState;
import de.mw.mwdata.rest.client.util.SessionUtils;
import de.mw.mwdata.rest.service.InvalidRestUrlException;
import de.mw.mwdata.rest.service.RestbasedMwUrl;
import de.mw.mwdata.rest.service.service.RestUrlService;

/**
 * FIXME rename controller i.e. to RestCrudController
 */
@RestController
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequestMapping("/rest/**")
public class OfdbRestController<E extends AbstractMWEntity> {

	private IPagingEntityService entityService;

	private IOfdbService ofdbService;

	private NavigationManager navigationManager;

	private ICrudService crudService;

	private RestUrlService urlService;

	public void setEntityService(IPagingEntityService entityService) {
		this.entityService = entityService;
	}

	public void setUrlService(RestUrlService urlService) {
		this.urlService = urlService;
	}

	public void setOfdbService(IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	public void setNavigationManager(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
	}

	public void setCrudService(ICrudService<? extends IEntity> crudService) {
		this.crudService = crudService;
	}

	/**
	 * Populates the empty filterObject for spring-web-binding. Spring does set the
	 * properties of the choosen filter-options. <br>
	 * FIXME: filter method still needed ? filtering in slickgrid clientside ...
	 * 
	 */
	@ModelAttribute("filterSet")
	public EntityTO<? extends AbstractMWEntity> populateFilterSet() {
		// RestbasedMwUrl mwUrl = this.navigationManager.readUrl();
		// if (mwUrl.hasEntityName()) {
		// return this.navigationManager.createEmptyEntity(mwUrl.getEntityName());
		// }

		return null;
	}

	@RequestMapping(value = "**/", method = RequestMethod.GET)
	public ResponseEntity<UiEntityList<E>> listAllTabDefs() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		// initialize navigation state
		HttpSession session = request.getSession();
		NavigationState state = SessionUtils.getNavigationState(session);

		// initialize ofPropList
		RestbasedMwUrl url = null;
		try {
			url = this.urlService.parseRestUrl(request.getRequestURL().toString());
		} catch (MalformedURLException e) {
			String message = MessageFormat.format("URL '{0}' does not apply to url format of REST API.",
					request.getRequestURL().toString());
			throw new InvalidRestUrlException(message, e);
		}
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(url.getEntityName());
		List<OfdbField> ofdbFieldList = this.ofdbService.initializeOfdbFields(viewDef.getName());

		PaginatedList<IEntity[]> entityResult = null;
		// int pageIndex = state.getPageIndex();
		// if (!state.isFiltered()) {
		List<SortKey> sortKeys = new ArrayList<>();
		entityResult = this.entityService.loadViewPaginated(viewDef.getName(), 1, sortKeys);
		// } else {
		// // ... what to do here if filtering
		// entityResult = this.entityService.findByCriteriaPaginated(viewDef.getName(),
		// state.getFilterSet(),
		// pageIndex, state.getSorting());
		// }

		UiEntityList<E> uiEntities = new UiEntityList<E>(entityResult.getItems(), ofdbFieldList);

		return new ResponseEntity<UiEntityList<E>>(uiEntities, HttpStatus.OK);
	}

	@RequestMapping(value = "/tabDef/{id}", method = RequestMethod.PUT)
	public ResponseEntity<EntityTO> updateEntity(@PathVariable("id") long id, @RequestBody E entity) {
		System.out.println("Updating Entity " + id);

		// ...
		// generic type von UIEntityList auf AbstractMWEntity geändert, überall
		// angepasst,
		// jetzt neu bauen und testen
		// ansonsten @see
		// http://www.davismol.net/2015/03/05/jackson-json-deserialize-a-list-of-objects-of-subclasses-of-an-abstract-class/

		Object foundEntity = this.crudService.findById(entity.getClass(), id);

		if (foundEntity == null) {
			System.out.println("Entity with id " + id + " not found");
			return new ResponseEntity<EntityTO>(HttpStatus.NOT_FOUND);
		}

		this.crudService.update(entity);
		EntityTO<E> entityTO = new EntityTO<E>(entity);
		// FIXME: mapValues not set in entityTO
		return new ResponseEntity<EntityTO>(entityTO, HttpStatus.OK);
	}

	// -------------------Create a
	// User--------------------------------------------------------

	// @RequestMapping(value = "/user/", method = RequestMethod.POST)
	// public ResponseEntity<Void> createUser(@RequestBody User user,
	// UriComponentsBuilder ucBuilder) {
	// System.out.println("Creating User " + user.getUsername());
	//
	// if (userService.isUserExist(user)) {
	// System.out.println("A User with name " + user.getUsername() + " already
	// exist");
	// return new ResponseEntity<Void>(HttpStatus.CONFLICT);
	// }
	//
	// userService.saveUser(user);
	//
	// HttpHeaders headers = new HttpHeaders();
	// headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
	// return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	// }

	// ------------------- Update a User
	// --------------------------------------------------------

	// @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	// public ResponseEntity<User> updateUser(@PathVariable("id") long id,
	// @RequestBody User user) {
	// System.out.println("Updating User " + id);
	//
	// User currentUser = userService.findById(id);
	//
	// if (currentUser == null) {
	// System.out.println("User with id " + id + " not found");
	// return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	// }
	//
	// currentUser.setUsername(user.getUsername());
	// currentUser.setAddress(user.getAddress());
	// currentUser.setEmail(user.getEmail());
	//
	// userService.updateUser(currentUser);
	// return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	// }

	// ------------------- Delete a User
	// --------------------------------------------------------

	// @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	// public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
	// System.out.println("Fetching & Deleting User with id " + id);
	//
	// User user = userService.findById(id);
	// if (user == null) {
	// System.out.println("Unable to delete. User with id " + id + " not
	// found");
	// return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	// }
	//
	// userService.deleteUserById(id);
	// return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	// }

	// ------------------- Delete All Users
	// --------------------------------------------------------

	// @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
	// public ResponseEntity<User> deleteAllUsers() {
	// System.out.println("Deleting All Users");
	//
	// userService.deleteAllUsers();
	// return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	// }

}