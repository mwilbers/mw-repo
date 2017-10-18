package de.mw.mwdata.app.admin.web.control;

import java.beans.PropertyEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.ofdb.domain.TabDef;
import de.mw.mwdata.core.web.control.GenericEntityController;

/**
 * Controller-class for managing communication between TabDef-Service and the
 * correspondant view. Controller is called over path /admin/tabDef/...
 * 
 * @author Wilbers, Markus
 * @version 1.0
 * @since Jan, 2011
 * 
 */
// @Controller
@RequestMapping("/admin/tabDef")
// @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class TabDefController extends GenericEntityController<TabDef> {

	final static Logger log = LoggerFactory.getLogger(TabDefController.class);

	private static final String SERVLET_SUBPATH = "tabDef";

	private static final String MESSAGE_CODE_PREFIX = "tabDef.";

	public TabDefController() {

		log.warn("???????????????????????????????????????????????????????????????");
		this.url = SERVLET_SUBPATH;
		// this.ofdbController = ofdbController;
		setMessageCodePrefix(MESSAGE_CODE_PREFIX);
		// this.filterObject = populateFilterObject();
		this.type = TabDef.class;
		// this.filterSet = populateFilterSet();

		// this.menues = this.ofdbController.findMenues( 4, StringUtils.EMPTY );
	}

	/**
	 * FX-PropertyEditor for fx-relevant enumtype-checks when filtering TODO:
	 * PropertyEditoren konfigurativ im xml-servlet zuweisen per setter oder
	 * auch in eine Liste und dann in der methode setBinding alle Editoren aus
	 * der Liste registrieren
	 */
	@Autowired
	private PropertyEditor enumDATENBANKEditor;

	// public void setEnumDATENBANKEditor( final PropertyEditor
	// enumDATENBANKEditor ) {
	// this.enumDATENBANKEditor = enumDATENBANKEditor;
	// }

	@Autowired
	private PropertyEditor enumZEITTYPEditor;

	// @Autowired
	// private FxEntityPropertyEditor benutzerBereichEditor;

	// @Override
	// @Override
	// @Autowired
	// @Qualifier("tabDefService")
	// public void setEntityService( final IGenericOfdbService<TabDef>
	// entityService ) {
	// this.entityService = entityService;
	//
	// // if ( entityService instanceof ITabDefService ) {
	// // this.tabDefService = (ITabDefService) entityService;
	// // }
	// }

	// @Override
	// @Autowired
	// @Qualifier("genericEntityValidator")
	// public void setValidator( final IFxAdminEntityValidator validator ) {
	// this.validator = validator;
	// }

	@Override
	protected void setBinding(final WebDataBinder binder) {
		super.setBinding(binder);
		binder.registerCustomEditor(TabDef.DATENBANK.class, this.enumDATENBANKEditor);
		binder.registerCustomEditor(TabDef.ZEITTYP.class, this.enumZEITTYPEditor);
		// binder.registerCustomEditor( BenutzerBereich.class,
		// this.benutzerBereichEditor );
		// SimpleDateFormat format = new SimpleDateFormat( "dd.MM.yyyy" );
		// CustomDateEditor dateEditor = new CustomDateEditor( format, true );
		// binder.registerCustomEditor( java.util.Date.class, dateEditor );

	}

	// @Override
	// @ModelAttribute("filterObject")
	// public TabDef populateFilterObject() {
	// TabDef d = new TabDef();
	// // d.setDatenbank( DATENBANK.K );
	// return d;
	// }

	@Override
	@ModelAttribute("filterSet")
	public EntityTO<TabDef> populateFilterSet() {

		TabDef d = new TabDef();
		// d.setDatenbank( DATENBANK.FW );
		EntityTO<TabDef> entityTO = new EntityTO<TabDef>(d);

		// // ... member-var this.type anpassen in klasse
		// entityTO.setItem( d );

		return entityTO;

	}

	// @Override
	// @ModelAttribute("currentObject")
	// public EntityTO<? extends AbstractMWEntity> populateCurrentObject() {
	// TabDef t = new TabDef();
	//
	// }

	@RequestMapping(value = "/foo")
	public void foo() {
		String x = "x";
	}

	@Override
	@RequestMapping(value = "list.htm", method = RequestMethod.GET, params = "menue")
	public ModelAndView list(final HttpServletRequest request, final HttpServletResponse response, final Integer pi,
			final String menue) {
		// TODO Auto-generated method stub
		return super.list(request, response, pi, menue);
	}

}
