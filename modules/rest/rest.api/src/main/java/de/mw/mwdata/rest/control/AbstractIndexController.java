package de.mw.mwdata.rest.control;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for defining ui template for common entry point of every
 * application
 * 
 * @author WilbersM
 *
 */
public abstract class AbstractIndexController {

	@RequestMapping(method = RequestMethod.GET)
	public String getIndexPage() {
		return "EntityManagement";
	}

}
