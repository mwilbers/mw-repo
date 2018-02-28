package de.mw.mwdata.rest.client.navigation;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.core.utils.SortKey;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.service.IOfdbService;
import de.mw.mwdata.rest.service.RestbasedMwUrl;

public class CrudNavigationManager implements NavigationManager, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4336377467585727437L;

	protected static Logger log = LoggerFactory.getLogger(CrudNavigationManager.class);

	private IOfdbService ofdbService;
	private OfdbCacheManager ofdbCacheManager;

	public void setOfdbService(IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	public void setOfdbCacheManager(OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	@Override
	public CrudNavigationState createNavigationState(final String requestedUrl,
			final EntityTO<? extends AbstractMWEntity> filterSet) {
		return new CrudNavigationState(requestedUrl, filterSet);

	}

	@Override
	public EntityTO<? extends AbstractMWEntity> createEmptyEntity(final String entityName) {

		IAnsichtDef viewDef = this.ofdbService.findAnsichtByUrlPath(entityName);
		ViewConfigHandle viewHandle = this.ofdbCacheManager.getViewConfig(viewDef.getName());
		IAnsichtTab viewTab = viewHandle.getMainAnsichtTab();
		Class clazz = ClassNameUtils.loadClass(viewTab.getTabDef().getFullClassName());

		return ClassNameUtils.createEntityTO(clazz);

	}

	// @Override
	@Override
	public void doList(final String urlPath, final Integer pageIndex, final String menue, final NavigationState state) {

		// state.setUrlPath(urlPath);

		state.setUrlPath(urlPath);
		state.setEntityId(0);
		if (!StringUtils.isBlank(menue)) {
			state.setMenue(menue);
		}

		if (null == pageIndex) {
			if (state.getPageIndex() == 0) {
				state.setPageIndex(1);
			}
		} else {
			state.setPageIndex(pageIndex);
		}

	}

	@Override
	public void doSort(final String col, final String asc, final NavigationState state) {

		Map<String, String> sortMap = new HashMap<String, String>();
		sortMap.put(col, ("1".equals(asc) ? "asc" : "desc"));

		state.setPageIndex(1);
	}

	@Override
	public void doFilter(final EntityTO<? extends AbstractMWEntity> filterSet, final boolean isFiltered,
			final NavigationState state) {

		state.setFilterSet(filterSet);
		state.setFiltered(isFiltered);

		state.setPageIndex(1);
	}

	@Override
	public void doEdit(final Long entityId, final EntityTO<? extends AbstractMWEntity> filterSet,
			final NavigationState state) {
		state.setEntityId(entityId);
		state.setFilterSet(filterSet);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void applyUrlPath(String requestUrl, NavigationState state) throws NavigationException {

		RestbasedMwUrl mwUrl = null;
		mwUrl = parseUrl(requestUrl);

		if (!mwUrl.hasEntityName()) {
			return; // is default url (.../admin/), not xmlhttprequest based
					// entity url from angularjs
		}

		if (!mwUrl.getEntityName().equals(state.getUrlPath())) {

			log.info("Url has changed. New Url: " + mwUrl.getServletPath());

			state.setUrlPath(mwUrl.getEntityName());
			state.setFiltered(false);
			EntityTO<? extends AbstractMWEntity> emptyEntity = createEmptyEntity(mwUrl.getEntityName());
			state.setFilterSet(emptyEntity);
			state.setPageIndex(1);
			state.setSorting(new ArrayList<SortKey>());

		}

	}

	private RestbasedMwUrl parseUrl(String requestUrl) {
		RestbasedMwUrl mwUrl = null;
		try {
			mwUrl = new RestbasedMwUrl(requestUrl);
		} catch (MalformedURLException e) {

			String msg = MessageFormat.format("Requested url {0} is not valid for application {1}.", requestUrl,
					Constants.APPLICATION_NAME);
			log.error(msg);
			throw new NavigationException(msg);
		}
		return mwUrl;
	}

	@Override
	public RestbasedMwUrl readUrl() {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		RestbasedMwUrl mwUrl = null;
		try {
			mwUrl = new RestbasedMwUrl(request.getRequestURL().toString());
		} catch (MalformedURLException e) {
			String msg = MessageFormat.format("Invalid url {0} in MWData application.",
					request.getRequestURL().toString());
			throw new NavigationException(msg);
		}

		return mwUrl;
	}

}
