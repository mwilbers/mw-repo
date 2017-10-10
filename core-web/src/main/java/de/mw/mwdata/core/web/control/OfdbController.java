/**
 * 
 */
package de.mw.mwdata.core.web.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.mw.mwdata.core.ofdb.def.CRUD;
import de.mw.mwdata.core.ofdb.def.OfdbField;
import de.mw.mwdata.core.ofdb.domain.Menue;
import de.mw.mwdata.core.ofdb.service.IOfdbService;
import de.mw.mwdata.core.utils.ITree;
import de.mw.mwdata.core.utils.TreeItem;

/**
 * @author mwilbers
 * 
 */
@RequestMapping("/admin/ofdb/")
public class OfdbController {

	protected static Logger log = LoggerFactory.getLogger(OfdbController.class);

	// @Autowired
	private IOfdbService ofdbService;

	public void setOfdbService(final IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	public OfdbController() {
		log.debug("in ofdbcontroller");
	}

	public ITree<TreeItem<Menue>> findMenues() {
		return this.ofdbService.findMenues();
	}

	@RequestMapping(value = "list.htm", method = RequestMethod.GET)
	public ModelAndView list(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(value = "menue", required = false) final String menue) {

		return new ModelAndView();

	}

	// @RequestMapping(value = "/entity/{entityName}", method =
	// RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<OfdbField> findOfdbFields(final String entityName, final CRUD crud) {
		return this.ofdbService.initializeOfdbFields(entityName, crud);
		// return new ResponseEntity<List<OfdbField>>(ofdbFields,
		// HttpStatus.OK);
	}

	// public String getViewNameByUrl( final String urlPath ) {
	// AnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath( urlPath );
	// return viewDef.getName();
	// }

	// protected abstract void initView( final String viewName );
	//
	// protected abstract void addToView( final String name, final Object value
	// );
	//
	// protected abstract ModelAndView getView();

}
