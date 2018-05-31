package de.mw.mwdata.ofdb.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import de.mw.mwdata.core.ApplicationFactory;
import de.mw.mwdata.core.ApplicationState;
import de.mw.mwdata.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.ofdb.cache.ViewConfigFactory;
import de.mw.mwdata.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.service.IOfdbService;

/**
 * Central base class for building up MW Data Application. An MW Data
 * Application runs a configuration phase where ofdb data has to be configured,
 * an initialization phase for loading the ofdb cache and the running phase when
 * applications runs.
 *
 * @author mwilbers
 *
 */
public class DefaultApplicationFactory implements ApplicationFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultApplicationFactory.class);

	private IOfdbService ofdbService;

	private List<String> registeredScopes;

	protected ViewConfigFactory viewConfigFactory;

	protected OfdbCacheManager ofdbCacheManager;

	private ApplicationState state;

	public void setViewConfigFactory(final ViewConfigFactory viewConfigFactory) {
		this.viewConfigFactory = viewConfigFactory;
	}

	public DefaultApplicationFactory(final String registeredScopes) {
		
		... hier den Bereiche-String einlesen, splitten und setzen. Dies sind für die Rest-Anwendung der string
		"Administrator, Termine"
		diese werden über die DefaultApplicationfActory einglesen und der OfdbCache aufgebaut.
		weiteres vorgehen:
		admin-app erweiteren um das Hinzufügen der OFDB-Datensätze für MWIV_ORT, MWIV_KATEGORIE, MWIV_GRUPPE etc.
		
		noch wichtiger: neues Ticket-SYstem verwenden, dort weitere Tickets anlegen:
			1. admin-app: neues Featuer Hinzufügen von Datensätzen ergänzen (verarbeitung von System-DS, defaults, etc.)
			2. insert-statements generieren und ins sql-changelog der app INTERVEREIN einfügen
			
		
		this.registeredScopes = parseScopes(registeredScopes);
	}

	private List<String> parseScopes(final String scopeToken) {

		List<String> scopes = new ArrayList<>();
		if (StringUtils.isEmpty(scopeToken)) {
			return scopes;
		}

		String[] tokens = StringUtils.split(scopeToken, ",");
		for (int i = 0; i < tokens.length; i++) {
			if (StringUtils.isEmpty(tokens[i])) {
				continue;
			}

			scopes.add(tokens[i]);
		}

		return scopes;
	}

	public void setOfdbService(final IOfdbService ofdbService) {
		this.ofdbService = ofdbService;
	}

	@Override
	public void configure() {
		this.state = ApplicationState.CONFIGURE;
	}

	@Override
	public void init() {
		this.state = ApplicationState.INITIALIZE;

		// FIXME: here add feature toggle for loading or not loading viewConfigs to
		// cache ...

		List<AnsichtDef> viewList = this.ofdbService.loadViewsForRegistration(this.servletPath);

		if (null == viewList) {
			return;
		}

		for (AnsichtDef ansichtDef : viewList) {

			if (this.ofdbCacheManager.isViewRegistered(ansichtDef.getName())) {
				// return;
				continue;
			}

			// register all ofdb-relevant data of the given view to cache
			ViewConfigHandle viewHandle = this.viewConfigFactory.createViewConfiguration(ansichtDef.getName());
			// } catch ( OfdbInvalidConfigurationException e ) {
			// LOGGER.error( e.getLocalizedMessage() );
			// }

			// ValidationResultSet viewValidationResultSet =
			// this.ofdbValidator.validateViewConfiguration( viewHandle );
			//
			// if ( viewValidationResultSet.hasErrors() ) {
			// String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME,
			// "invalidConfiguration" );
			// throw new OfdbInvalidConfigurationException( msg );
			// // continue;
			// } else {
			this.ofdbCacheManager.registerView(viewHandle);
			// }

		}

		this.state = ApplicationState.RUNNING;
	}

	public void setOfdbCacheManager(final OfdbCacheManager ofdbCacheManager) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	@Override
	public ApplicationState getState() {
		return this.state;
	}

}
