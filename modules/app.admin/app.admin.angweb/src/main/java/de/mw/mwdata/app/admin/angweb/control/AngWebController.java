package de.mw.mwdata.app.admin.angweb.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.SortKey;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.service.IPagingOfdbService;
import de.mw.mwdata.core.utils.PaginatedList;

@Controller
// @EnableWebMvc
// http://localhost:8080/app.admin.angweb/adminAngWeb/tabDef/tabDef/42 ->
/**
 * @see here:
 *      http://www.journaldev.com/2552/spring-rest-example-tutorial-spring-restful-web-services
 *      and dwonload folder -> SpringRestExample.zip
 *
 *      better for classic REST-based urls with whole restbased angularjs
 *      integration @see
 *      http://websystique.com/springmvc/spring-mvc-4-angularjs-example/ and
 *      download -> Spring4MVCAngularJSExample.zip
 * 
 *      next step: integrate with angular based ng-grid
 *      (http://angular-ui.github.io/ui-grid/)
 * 
 */
@RequestMapping(value = "/adminAngWeb/**")
public class AngWebController {

	@Autowired
	private IPagingOfdbService ofdbService;

	@RequestMapping(value = "**/tabDef/{id}", headers = "Accept=application/json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody JsonBasedTabDef find(@PathVariable Integer id) {

		// initialize ofPropList
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath("tabDef");

		// // keep filter-information when e.g. sorting table
		PaginatedList<IEntity[]> entitiesArray = null;
		int pageIndex = 1;
		List<SortKey> sortKeys = new ArrayList<SortKey>();
		sortKeys.add(new SortKey("name", "ASC"));
		entitiesArray = this.ofdbService.loadViewPaginated(viewDef.getName(), pageIndex, sortKeys);

		Object[] oEntity = entitiesArray.getItems().get(0);

		// final HttpHeaders httpHeaders= new HttpHeaders();
		// httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		// return new ResponseEntity<Object>("{\"test\":
		// \"jsonResponseExample\"}", httpHeaders, HttpStatus.OK);

		JsonBasedTabDef tabDef = new JsonBasedTabDef();
		tabDef.setName("jsonTestTabDef");
		return tabDef;

		// return (TabDef) oEntity[0];

	}

	public IPagingOfdbService getOfdbService() {
		return ofdbService;
	}

	public void setOfdbService(IPagingOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

}
