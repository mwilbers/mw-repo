package de.mw.mwdata.core.web.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.ofdb.SortKey;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.service.IOfdbService;
import de.mw.mwdata.core.service.IPagingEntityService;
import de.mw.mwdata.core.utils.PaginatedList;

@Controller
@RequestMapping(value = "/adminAngular/**")
public class AngularController {

	@Autowired
	private IOfdbService ofdbService;

	@Autowired
	private IPagingEntityService entityService;

	@RequestMapping(value = "**/listAngular", method = RequestMethod.GET, produces = { "application/json" })
	public AbstractMWEntity listAngular() {

		// initialize ofPropList
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath("tabDef");

		// // keep filter-information when e.g. sorting table
		PaginatedList<IEntity[]> entitiesArray = null;
		int pageIndex = 1;
		List<SortKey> sortKeys = new ArrayList<SortKey>();
		sortKeys.add(new SortKey("name", "ASC"));
		entitiesArray = this.entityService.loadViewPaginated(viewDef.getName(), pageIndex, sortKeys);

		Object[] oEntity = entitiesArray.getItems().get(0);
		return (AbstractMWEntity) oEntity[0];

	}

}
