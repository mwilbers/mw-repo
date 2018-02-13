package de.mw.mwdata.app.calendar.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * FIXME: should be moved to app.calendar.client. Then clean up POM
 * 
 * @author WilbersM
 *
 */
@Controller
@RequestMapping("/cal/")
public class IndexController {

	@RequestMapping(method = RequestMethod.GET)
	public String getIndexPage() {
		return "index";
	}

}