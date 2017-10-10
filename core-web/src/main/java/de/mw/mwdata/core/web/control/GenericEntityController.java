/**
 *
 */
package de.mw.mwdata.core.web.control;

import java.beans.PropertyEditor;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.MapValue;
import de.mw.mwdata.core.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.core.ofdb.def.CRUD;
import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.domain.Menue;
import de.mw.mwdata.core.ofdb.domain.TabSpeig;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.core.ofdb.service.IOfdbService;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.service.IPagingEntityService;
import de.mw.mwdata.core.utils.ITree;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.core.utils.TreeItem;
import de.mw.mwdata.core.web.Config;
import de.mw.mwdata.core.web.navigation.NavigationException;
import de.mw.mwdata.core.web.navigation.NavigationManager;
import de.mw.mwdata.core.web.navigation.NavigationState;
import de.mw.mwdata.core.web.util.MwUrl;
import de.mw.mwdata.core.web.util.SessionUtils;
import de.mw.mwdata.core.web.validate.EntityPropertyEditor;

/**
 * Problems in url-mapping from spring mvc 3.1 upwards:
 * http://forum.springsource
 * .org/showthread.php?51463-Error-using-AnnotationMethodHandlerAdapter
 * -and-UrlFilenameViewController
 * https://java.net/projects/dwr/lists/users/archive/2012-09/message/91
 * http://forum
 * .springsource.org/showthread.php?123440-Multiple-instances-of-annotated
 * -Controller-with-RequestMapping http
 * ://forum.springsource.org/showthread.php?
 * 125224-Mapping-some-Controller-without-Annotation-for-path-Spring-MVC-3-1
 * http
 * ://forum.springsource.org/showthread.php?127014-Spring-MVC-2-5-upgrade-to-
 * Spring-3-1 http://static.springsource.org
 * /spring/docs/3.1.x/spring-framework-
 * reference/html/mvc.html#mvc-ann-requestmapping-31-vs-30 HttpRequestHandler,
 * Controller
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Feb, 2011
 *
 */
@Controller
@RequestMapping("/admin/**")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GenericEntityController<E extends AbstractMWEntity> implements CrudController<E> {

	protected static Logger log = LoggerFactory.getLogger(GenericEntityController.class);

	private PropertyEditor enumDBTYPEEditor;

	private PropertyEditor enumMENUETYPEEditor;

	private EntityPropertyEditor benutzerBereichEditor;

	protected List<E> entities;

	private String messageCodePrefix;

	protected OfdbController ofdbController;

	private IOfdbService ofdbService;

	private IPagingEntityService entityService;

	private ICrudService crudService;

	private NavigationManager navigationManager;

	private OfdbCacheManager ofdbCacheManager;

	protected ITree<TreeItem<Menue>> menues;

	private MessageSource messageSource;

	// constructors ...

	public GenericEntityController(final Class<E> type) {
		// this.type = type;
		// FIXME: constructor still needed with type ?
		assert true;
	}

	public GenericEntityController() {
		assert true;
	}

	public void setOfdbService(final IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	public void setCrudService(final ICrudService crudService) {
		this.crudService = crudService;
	}

	public void setBenutzerBereichEditor(final EntityPropertyEditor benutzerBereichEditor) {
		this.benutzerBereichEditor = benutzerBereichEditor;
	}

	public void setEnumDBTYPEEditor(final PropertyEditor enumDBTYPEEditor) {
		this.enumDBTYPEEditor = enumDBTYPEEditor;
	}

	public void setEnumMENUETYPEEditor(final PropertyEditor enumMENUETYPEEditor) {
		this.enumMENUETYPEEditor = enumMENUETYPEEditor;
	}

	public void setOfdbController(final OfdbController ofdbController) {
		this.ofdbController = ofdbController;
	}

	public void setNavigationManager(final NavigationManager navigationManager) {

		this.navigationManager = navigationManager;
	}

	public void setOfdbCacheManager(final OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	// spring-bean-setters ...

	public void setMessageCodePrefix(final String messageCodePrefix) {
		this.messageCodePrefix = messageCodePrefix;
	}

	// common methods ...

	protected void setBinding(final WebDataBinder binder) {

		binder.registerCustomEditor(TabSpeig.DBTYPE.class, this.enumDBTYPEEditor);
		binder.registerCustomEditor(Menue.MENUETYP.class, this.enumMENUETYPEEditor);
		binder.registerCustomEditor(BenutzerBereich.class, this.benutzerBereichEditor);

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		CustomDateEditor dateEditor = new CustomDateEditor(format, true);
		binder.registerCustomEditor(java.util.Date.class, dateEditor);

		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor("true", "false", true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));

	}

	@InitBinder(value = "entitiesPl")
	public void initbinderEntitiesPl(final WebDataBinder binder) {
		log.debug("----------- in initbinderEntitiesPl-method -----------");
	}

	@InitBinder(value = "filterSet")
	public void initbinderEntityTO(final WebDataBinder binder) {

		// binder.setIgnoreUnknownFields( false );
		log.debug("----------- in initBinderEntityTO-method -----------");
		setBinding(binder);

		setMessageCodesResolver(binder);

	}

	@InitBinder(value = "currentObject")
	public void initbinderCurrentObject(final WebDataBinder binder) {

		log.debug("----------- in initBinderfilterobject-method -----------");
		setBinding(binder);

		// set validator
		// binder.setValidator( this.validator );

		setMessageCodesResolver(binder);

	}

	@Override
	@RequestMapping(value = "**/list.htm", method = RequestMethod.GET)
	public ModelAndView list(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(value = "pi", required = false) final Integer pi,
			@RequestParam(value = "menue", required = false) final String menue) {

		// bind filterObject to session, than take it from session, when calling
		// filter()
		// http://dark-it.blogspot.com/2010/11/generic-crud-methods-spring-3.html
		// http://www.devx.com/java/Article/41629/1954
		// http://www.weheartcode.com/2008/10/16/reusing-annotation-based-controllers-in-spring-mvc/
		// http://www.java-forum.org/application-tier/116427-spring-mvc-modelattribute.html

		HttpSession session = request.getSession();
		NavigationState state = SessionUtils.getNavigationState(session);
		this.navigationManager.doList(getUrl().getServletSubPath(), pi, menue, state);

		if (null == this.menues) {
			// FIXME: check if this.menues is loaded redundant
			// TODO: improve performance of menu-call
			this.menues = this.ofdbController.findMenues();
		}

		// initialize ofPropList
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(state.getUrlPath());
		List<OfdbField> ofPropList = this.ofdbService.initializeOfdbFields(viewDef.getName(), CRUD.SELECT);

		// // keep filter-information when e.g. sorting table
		PaginatedList<IEntity[]> entitiesArray = null;
		int pageIndex = state.getPageIndex();
		if (!state.isFiltered()) {
			entitiesArray = this.entityService.loadViewPaginated(viewDef.getName(), pageIndex, state.getSorting());
		} else {
			// ... what to do here if filtering
			entitiesArray = this.entityService.findByCriteriaPaginated(viewDef.getName(), state.getFilterSet(),
					pageIndex, state.getSorting());
		}

		ModelAndView mav = getView(Config.TILES_VIEWDEF_LIST_ENTITIES);
		mav.addObject("menues", this.menues);

		// ... testen, indem im menue AnsichtSpalten aufgerufen wird
		mav.addObject("entitiesArray", entitiesArray.getItems());
		mav.addObject("filterSet", state.getFilterSet());

		mav.addObject("entitiesPl", entitiesArray);
		mav.addObject("ofPropList", ofPropList);
		mav.addObject("navigationState", state);

		return mav;

	}

	// @Override
	@Override
	@RequestMapping(value = "**/sort.htm", method = RequestMethod.GET)
	public ModelAndView sort(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(value = "col", required = true) final String col,
			@RequestParam(value = "asc", required = true) final String asc) {

		// bind filterObject to session, than take it from session, when calling
		// filter()
		// http://dark-it.blogspot.com/2010/11/generic-crud-methods-spring-3.html
		// http://www.devx.com/java/Article/41629/1954
		// http://www.weheartcode.com/2008/10/16/reusing-annotation-based-controllers-in-spring-mvc/
		// http://www.java-forum.org/application-tier/116427-spring-mvc-modelattribute.html

		NavigationState state = SessionUtils.getNavigationState(request.getSession());
		SessionUtils.logSession(request.getSession());

		if (null == this.menues) {
			// TODO: improve performance of menu-call
			this.menues = this.ofdbController.findMenues();
		}

		// if we want to sort the records the paging has to be reset
		this.navigationManager.doSort(col, asc, state);

		// initialize ofPropList
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(state.getUrlPath());
		List<OfdbField> ofPropList = this.ofdbService.initializeOfdbFields(viewDef.getName(), CRUD.SELECT);

		PaginatedList<IEntity[]> entitiesArray = null;
		int pageIndex = state.getPageIndex();
		if (!state.isFiltered()) {

			entitiesArray = this.entityService.loadViewPaginated(viewDef.getName(), pageIndex, state.getSorting());
		} else {
			// ... what to do here if filtering
			entitiesArray = this.entityService.findByCriteriaPaginated(viewDef.getName(), state.getFilterSet(),
					pageIndex, state.getSorting());
		}

		ModelAndView mav = getView(Config.TILES_VIEWDEF_LIST_ENTITIES);
		mav.addObject("menues", this.menues);

		// ... testen, indem im menue AnsichtSpalten aufgerufen wird
		mav.addObject("entitiesArray", entitiesArray.getItems());
		mav.addObject("filterSet", state.getFilterSet());

		mav.addObject("entitiesPl", entitiesArray);
		mav.addObject("ofPropList", ofPropList);
		mav.addObject("navigationState", state);

		return mav;

	}

	private MwUrl getUrl() {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		MwUrl mwUrl = null;
		try {
			mwUrl = new MwUrl(request.getRequestURL().toString());
		} catch (MalformedURLException e) {
			String msg = MessageFormat.format("Invalid url {0} in MWData application.",
					request.getRequestURL().toString());
			throw new NavigationException(msg);
		}

		return mwUrl;
	}

	/**
	 * Populates the empty filterObject for spring-web-binding. Spring does set
	 * the properties of the choosen filter-options.
	 * 
	 */
	@ModelAttribute("filterSet")
	public EntityTO<? extends AbstractMWEntity> populateFilterSet() {
		MwUrl mwUrl = this.getUrl();
		return this.navigationManager.createEmptyEntity(mwUrl.getServletSubPath());
	}

	private boolean isEmpty(final EntityTO<E> entityTO) {

		boolean isEmpty = true;
		try {
			isEmpty = this.ofdbService.isEmpty(entityTO.getItem());
		} catch (OfdbMissingMappingException e) {
			log.error(e.getLocalizedMessage());
		}
		if (!isEmpty) {
			return isEmpty;
		}

		for (Entry<String, MapValue> entry : entityTO.getMap().entrySet()) {
			if (!StringUtils.isBlank(entry.getValue().getValue())) {
				return false;
			}
		}

		return isEmpty;

	}

	@Override
	@RequestMapping(value = "**/filter.htm", method = RequestMethod.POST)
	// @Override
	public ModelAndView filter(final HttpServletRequest request, final HttpServletResponse response,
			@ModelAttribute("filterSet") final EntityTO<E> filterSet, final BindingResult result) {

		NavigationState state = SessionUtils.getNavigationState(request.getSession());
		SessionUtils.logSession(request.getSession());

		// ... bindingerror wenn man menueeintrag AnsichtSpalten wählt und dann
		// IndexGrid leert und Ansicht
		// = 'FX_TabDef_K' wählt, denn IndexGrid wird fälschlicherweise mit 0
		// vorbelegt -> BindingError in Ausgabe

		// if we want to filter the records the paging has to be reset
		boolean isEmpty = this.isEmpty(filterSet);
		this.navigationManager.doFilter(filterSet, isEmpty, state);

		ModelAndView mav = getView(Config.TILES_VIEWDEF_LIST_ENTITIES);
		doValidate(result, mav);

		PaginatedList<IEntity[]> entitiesArray = null;

		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(state.getUrlPath());
		entitiesArray = this.entityService.findByCriteriaPaginated(viewDef.getName(), state.getFilterSet(),
				state.getPageIndex(), state.getSorting());

		List<OfdbField> ofPropList = null;
		ofPropList = this.ofdbService.initializeOfdbFields(viewDef.getName(), CRUD.SELECT);

		mav.addObject("entitiesArray", entitiesArray.getItems());
		mav.addObject("menues", this.menues);
		mav.addObject("filterSet", state.getFilterSet());
		mav.addObject("ofPropList", ofPropList);
		mav.addObject("entitiesPl", entitiesArray);
		mav.addObject("navigationState", state);

		return mav;

	}

	// FIXME: for REST API Design use /edit/id/{id}?menue={menue} ... as url
	@Override
	@RequestMapping(value = "**/edit.htm", method = RequestMethod.GET, params = { "menue" })
	public ModelAndView edit(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(value = "id", required = false) final Long id,
			@RequestParam(value = "menue", required = false) final String menue,
			@ModelAttribute("filterSet") final EntityTO<E> filterSet) {

		// ... bug required id-parameter: @see
		// https://jira.spring.io/browse/SPR-10592

		NavigationState state = SessionUtils.getNavigationState(request.getSession());

		E entity = null;
		EntityTO<E> entityTO;
		this.navigationManager.doEdit(id, filterSet, state);
		if (null != id) {
			entity = (E) this.crudService.findById(filterSet.getItem().getClass(), id);
			entityTO = new EntityTO<E>(entity);
		} else {
			entityTO = (EntityTO<E>) state.getFilterSet(); // (EntityTO<AbstractMWEntity>)
		}

		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(state.getUrlPath());
		List<OfdbField> ofPropList = this.ofdbService.initializeOfdbFields(viewDef.getName(), CRUD.UPDATE);

		ModelAndView mav = getView(Config.TILES_VIEWDEF_EDIT_ENTITY);
		mav.addObject("menues", this.menues);
		mav.addObject("ofPropList", ofPropList);
		// TODO: instead of currentObject use name editObject here ...
		mav.addObject("currentObject", entityTO);
		mav.addObject("navigationState", state);

		return mav;
	}

	@ModelAttribute("currentObject")
	public EntityTO<? extends AbstractMWEntity> populateCurrentObject() {

		MwUrl mwUrl = this.getUrl();
		return this.navigationManager.createEmptyEntity(mwUrl.getServletSubPath());

		// IAnsichtDef viewDef =
		// this.ofdbService.findAnsichtByUrlPath(getUrl().getServletSubPath());
		// ViewConfigHandle viewHandle =
		// this.ofdbCacheManager.getViewConfig(viewDef.getName());
		// IAnsichtTab viewTab = viewHandle.getMainAnsichtTab();
		// Class clazz =
		// ClassNameUtils.loadClass(viewTab.getTabDef().getFullClassName());
		// return ClassNameUtils.createEntityTO(clazz);

	}

	// @Override
	@RequestMapping("**/save.htm")
	public ModelAndView save(final HttpServletRequest request, final HttpServletResponse response,
			@ModelAttribute("currentObject") final E currentObject, final BindingResult result) {

		NavigationState state = SessionUtils.getNavigationState(request.getSession());

		this.navigationManager.doList(getUrl().getServletSubPath(), null, null, state);
		ModelAndView mav = getView(Config.TILES_VIEWDEF_LIST_ENTITIES);

		// for validation of nested objects use adress-validator-example in 5.2
		// spring-reverence:
		// http://static.springsource.org/spring/docs/2.5.5/reference/validation.html
		doValidate(result, mav);

		if (currentObject.isInDB()) {
			this.crudService.update(currentObject);
		} else {
			this.crudService.insert(currentObject);
		}

		if (currentObject instanceof Menue) {
			this.menues = this.ofdbController.findMenues();
		}

		int pageIndex = state.getPageIndex();
		PaginatedList<E> entitiesPl = null;
		PaginatedList<IEntity[]> entitiesArray = null;
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(state.getUrlPath());
		if (!state.isFiltered()) {
			entitiesArray = this.entityService.loadViewPaginated(viewDef.getName(), pageIndex);
		} else {
			entitiesArray = this.entityService.findByCriteriaPaginated(viewDef.getName(), state.getFilterSet(),
					pageIndex, state.getSorting());
		}
		this.entities = entitiesPl.getItems();

		List<OfdbField> ofPropList = this.ofdbService.initializeOfdbFields(viewDef.getName(), CRUD.SELECT);

		mav.addObject("menues", this.menues);
		mav.addObject("ofPropList", ofPropList);
		mav.addObject("entitiesPl", entitiesPl);
		mav.addObject("entities", this.entities);
		mav.addObject("navigationState", state);

		return mav;
	}

	protected ModelAndView getView(final String viewName) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	protected void setMessageCodesResolver(final WebDataBinder binder) {
		DefaultMessageCodesResolver resolver = new DefaultMessageCodesResolver();
		resolver.setPrefix(this.messageCodePrefix);
		// !!! spring-bug SPR-7590 ? use method only when there is a real
		// BindingResult
		binder.setMessageCodesResolver(resolver);
	}

	protected void doValidate(final BindingResult result, final ModelAndView mav) {
		if (result.hasErrors()) {
			mav.addAllObjects(result.getModel());
			mav.getModel().putAll(result.getModel());
		}
	}

	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public MessageSource getMessageSource() {
		return this.messageSource;
	}

}
