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
import de.mw.mwdata.core.ofdb.service.IOfdbService;

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

	@RequestMapping(value = "list.htm", method = RequestMethod.GET)
	public ModelAndView list(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(value = "menue", required = false) final String menue) {

		return new ModelAndView();

	}

	public List<OfdbField> findOfdbFields(final String entityName, final CRUD crud) {
		return this.ofdbService.initializeOfdbFields(entityName, crud);
	}

}
