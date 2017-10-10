package de.mw.mwdata.app.admin.angweb2.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mkyong.common.model.JsonTabDef;

import de.mw.mwdata.core.ofdb.domain.ITabDef;

@Controller
@RequestMapping("/kfc/brands")
public class AngularWeb2Controller {
	// call http://localhost:8080/app.admin.angweb2/rest/kfc/brands/kfc-kampar
	@RequestMapping(value = "{name}", method = RequestMethod.GET)
	public @ResponseBody ITabDef getShopInJSON(@PathVariable String name) {

		// ITabDef tabDef = new TabDef();
		// tabDef.setId( 1L );
		// tabDef.setName( "TestTabDef" );
		JsonTabDef shop = new JsonTabDef();
		shop.setName( name );

		// Fehlerursache für AbstractMWEntity not resolving jo JSON: Boolean
		// getSystem()

		return shop;

	}

}