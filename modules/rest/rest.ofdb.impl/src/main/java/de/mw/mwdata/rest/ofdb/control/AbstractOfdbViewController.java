package de.mw.mwdata.rest.ofdb.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.Benutzer;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.service.ICrudService;
import de.mw.mwdata.core.utils.Utils;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtSpalten;
import de.mw.mwdata.ofdb.domain.impl.UserView;
import de.mw.mwdata.ofdb.domain.impl.UserViewColumn;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.ofdb.service.IViewService;
import de.mw.mwdata.rest.control.IUserConfigController;
import de.mw.mwdata.rest.ofdb.uimodel.UiUserView;
import de.mw.mwdata.rest.uimodel.UiInputConfig;

public abstract class AbstractOfdbViewController {

	private IViewService viewService;
	private ICrudService crudService;
	private OfdbCacheManager ofdbCacheManager;
	private IUserConfigController userConfigController;
	private IOfdbService ofdbService;

	public void setCrudService(final ICrudService<? extends AbstractMWEntity> crudService) {
		this.crudService = crudService;
	}

	public void setOfdbCacheManager(final OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	public void setViewService(IViewService viewService) {
		this.viewService = viewService;
	}

	public void setUserConfigController(IUserConfigController userConfigController) {
		this.userConfigController = userConfigController;
	}

	public void setOfdbService(IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	@RequestMapping(value = "**/", method = RequestMethod.POST)
	public ResponseEntity<EntityTO<UserView>> createUserView(@RequestBody UiUserView uiView) {

		// prepare object UserView
		UserView userView = new UserView();
		userView.setName(uiView.getName());
		Benutzer user = (Benutzer) this.crudService.findById(Benutzer.class, uiView.getUserId());
		userView.setUser(user);

		String entityName = this.userConfigController.loadUrlPathToken();
		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(entityName);
		userView.setViewDef((AnsichtDef) viewDef);

		// prepare objects UserViewColumn
		List<UserViewColumn> viewColumns = new ArrayList<>();
		for (UiInputConfig config : uiView.getUiInputConfigs()) {
			UserViewColumn col = new UserViewColumn();
			col.setUserView(userView);
			ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(viewDef.getName());
			AnsichtSpalten viewColumn = (AnsichtSpalten) viewHandle.findViewColumnByName(config.getPropOfdbName());
			col.setViewColumn(viewColumn);
			col.setOrder(config.getReihenfolge());
			col.setWidth(config.getSpaltenBreite());

			viewColumns.add(col);
		}

		userView = this.viewService.createUserView(userView, viewColumns);

		EntityTO<UserView> entityTO = new EntityTO<UserView>(userView);
		return new ResponseEntity<EntityTO<UserView>>(entityTO, HttpStatus.OK);
	}

	@RequestMapping(value = "**/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<EntityTO<UserView>>> listAllUserViews(@PathVariable("userId") long userId) {

		// FIXME : implement method
		List<UserView> userViews = this.viewService.listAllUserViews(userId);

		List<EntityTO> toList = Utils.entityListToEntityToList(userViews);
		return new ResponseEntity(toList, HttpStatus.OK); // <List<EntityTO<UserView>>>(toList, HttpStatus.OK);
	}

}
