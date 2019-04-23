package de.mw.mwdata.rest.ofdb.control;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import de.mw.mwdata.core.daos.PagingModel;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.service.ApplicationConfigService;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.ofdb.service.IViewService;
import de.mw.mwdata.rest.control.AbstractCrudController;
import de.mw.mwdata.rest.control.IUserConfigController;
import de.mw.mwdata.rest.uimodel.UiEntityList;
import de.mw.mwdata.rest.utils.SessionUtils;

public abstract class AbstractOfdbCrudController<E extends AbstractMWEntity> extends AbstractCrudController<E> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOfdbCrudController.class);

	private IViewService<IEntity> viewService;
	private IOfdbService ofdbService;
	private ICrudService crudService;
	private ApplicationConfigService configService;
	private IUserConfigController userConfigController;

	public void setViewService(IViewService viewService) {
		this.viewService = viewService;
	}

	public void setOfdbService(IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	protected ApplicationConfigService getApplicationConfigService() {
		return configService;
	}

	public void setApplicationConfigService(ApplicationConfigService configService) {
		this.configService = configService;
	}

	public void setCrudService(ICrudService<? extends IEntity> crudService) {
		this.crudService = crudService;
	}

	public void setUserConfigController(IUserConfigController userConfigController) {
		this.userConfigController = userConfigController;
	}

	public ResponseEntity<UiEntityList<E>> listAllEntities(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize) {

		String entityName = this.userConfigController
				.applyUrlPathToken(SessionUtils.getHttpServletRequest().getRequestURL().toString());
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(entityName);
		PagingModel pagingModel = new PagingModel((pageSize == 0 ? this.loadPageSize() : pageSize), pageIndex);
		if (null == viewDef) {
			UiEntityList<E> uiEntities = UiEntityList.createEmptyUiEntityList(pagingModel);
			return new ResponseEntity<UiEntityList<E>>(uiEntities, HttpStatus.OK);
		}

		QueryResult entityResult = this.viewService.executeViewQuery(viewDef.getName(), pagingModel,
				new ArrayList<SortKey>());
		UiEntityList<E> uiEntities = new UiEntityList<E>(entityResult.getRows(), entityResult.getMetaData(),
				pagingModel);

		return new ResponseEntity<UiEntityList<E>>(uiEntities, HttpStatus.OK);
	}

	private int loadPageSize() {

		String pageSizeString = this.configService.getPropertyValue(ApplicationConfigService.KEY_PAGESIZE_FOR_LOAD);

		int pageSize = 0;
		try {
			pageSize = Integer.parseInt(pageSizeString);
		} catch (NumberFormatException e) {
			LOGGER.warn(
					"Configuration of application property 'app.hibernate.pageSizeForLoad' is not valid integer. Will use default page size.");
		}

		return pageSize;
	}

	@Override
	public ResponseEntity<EntityTO<E>> updateEntity(@PathVariable("id") long id, @RequestBody E entity) {
		System.out.println("Updating Entity " + id);

		// @see
		// http://www.davismol.net/2015/03/05/jackson-json-deserialize-a-list-of-objects-of-subclasses-of-an-abstract-class/

		Object foundEntity = this.crudService.findById(entity.getClass(), id);

		if (foundEntity == null) {
			LOGGER.warn("Entity with id " + id + " not found");
			return new ResponseEntity<EntityTO<E>>(HttpStatus.NOT_FOUND);
		}

		this.crudService.update(entity);
		EntityTO<E> entityTO = new EntityTO(entity);
		// FIXME: mapValues not set in entityTO
		return new ResponseEntity<EntityTO<E>>(entityTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UiEntityList<E>> filterEntities(@RequestParam("filter") String filter,
			@RequestBody E entity) {
		LOGGER.info("Filter Entity " + entity.toString());

		String entityName = this.userConfigController
				.applyUrlPathToken(SessionUtils.getHttpServletRequest().getRequestURL().toString());
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(entityName);
		PagingModel pagingModel = new PagingModel(loadPageSize(), 1);
		if (null == viewDef) {
			UiEntityList<E> uiEntities = UiEntityList.createEmptyUiEntityList(pagingModel);
			return new ResponseEntity<UiEntityList<E>>(uiEntities, HttpStatus.OK);
		}

		List<OfdbField> ofdbFieldList = this.ofdbService.initializeOfdbFields(viewDef.getName());

		EntityTO entityTO = new EntityTO<AbstractMWEntity>(entity);
		QueryResult queryResult = this.viewService.executePaginatedViewQuery(viewDef.getName(), entityTO, pagingModel,
				new ArrayList<SortKey>());
		pagingModel.setCount(queryResult.size());

		UiEntityList<E> uiEntities = new UiEntityList<E>(queryResult.getRows(), ofdbFieldList, pagingModel);

		return new ResponseEntity<UiEntityList<E>>(uiEntities, HttpStatus.OK);

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