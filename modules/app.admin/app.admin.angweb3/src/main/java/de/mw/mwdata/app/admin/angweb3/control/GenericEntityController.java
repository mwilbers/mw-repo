package de.mw.mwdata.app.admin.angweb3.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.service.ICrudService;

/**
 * @see http://www.java2blog.com/2016/08/spring-mvc-angularjs-example.html
 * @author mwilbers
 *
 * @param <E>
 */
// @Controller
@RestController
// @RequestMapping("/generic")
public class GenericEntityController<E extends AbstractMWEntity> {

	// call http://localhost:8080/app.admin.angweb3/

	private ICrudService<ITabDef> crudService;

	public void setCrudService(ICrudService<ITabDef> crudService) {
		this.crudService = crudService;
	}

	@RequestMapping("/")
	public String homepage() {
		return "homepage";
	}

	@RequestMapping("/hello/{player}")
	public Message message(@PathVariable String player) {// REST Endpoint.

		Message msg = new Message( player, "Hello " + player );
		return msg;
	}

	// value = "/tabDef/{tabDefId}"
	// @PathVariable String tabDefId
	// , headers = "Accept=application/json"
	@RequestMapping(value = "/tabDefs", method = RequestMethod.GET)
	public ModelAndView getTabDef() {

		Map<String, String> map = new HashMap<String, String>();
		// List<Map> mapList = new ArrayList<Map>();
		// mapList.add( map );
		// List<ITabDef> tabDefs = this.crudService.findAll( ITabDef.class, map
		// );
		List<ITabDef> tabDefs = new ArrayList<>();
		ITabDef t = new JsonTabDef();
		t.setName( "jsonTabDef" );
		t.setId( 42L );
		tabDefs.add( t );

		ModelAndView mav = new ModelAndView();
		mav.addObject( "tabDefs", tabDefs );
		mav.setViewName( "mikesAng" );
		return mav;

		// return tabDefs;

	}

}
