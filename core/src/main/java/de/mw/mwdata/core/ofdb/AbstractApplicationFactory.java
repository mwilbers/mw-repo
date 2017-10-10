package de.mw.mwdata.core.ofdb;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.mw.mwdata.core.ofdb.cache.OfdbCacheManager;
import de.mw.mwdata.core.ofdb.cache.ViewConfigFactory;
import de.mw.mwdata.core.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.core.ofdb.domain.AnsichtDef;
import de.mw.mwdata.core.ofdb.exception.OfdbMissingMappingException;
import de.mw.mwdata.core.ofdb.service.IOfdbService;
import de.mw.mwdata.core.ofdb.validate.OfdbValidatable;

/**
 * Central base class for building up MW Data Application. An MW Data Application runs a configuration phase where ofdb
 * data has to be configured, an initialization phase for loading the ofdb cache and the running phase when applications
 * runs.
 *
 * @author mwilbers
 *
 */
public abstract class AbstractApplicationFactory implements ApplicationFactory {

	private static final Logger	LOGGER	= LoggerFactory.getLogger( AbstractApplicationFactory.class );

	private IOfdbService		ofdbService;

	private String				servletPath;

	protected OfdbValidatable	ofdbValidator;

	protected ViewConfigFactory	viewConfigFactory;

	protected OfdbCacheManager	ofdbCacheManager;

	private ApplicationState	state;

	public void setViewConfigFactory( final ViewConfigFactory viewConfigFactory ) {
		this.viewConfigFactory = viewConfigFactory;
	}

	public AbstractApplicationFactory(final String servletPath) {
		this.servletPath = servletPath;
	}

	public void setOfdbService( final IOfdbService ofdbService ) {
		this.ofdbService = ofdbService;
	}

	public void setOfdbValidator( final OfdbValidatable ofdbValidator ) {
		this.ofdbValidator = ofdbValidator;
	}

	public void configure() {
		this.state = ApplicationState.CONFIGURE;
	}

	public void init() throws OfdbMissingMappingException {
		this.state = ApplicationState.INITIALIZE;

		List<AnsichtDef> viewList = this.ofdbService.loadViewsForRegistration( this.servletPath );

		if ( null == viewList ) {
			return;
		}

		for ( AnsichtDef ansichtDef : viewList ) {

			if ( this.ofdbCacheManager.isViewRegistered( ansichtDef.getName() ) ) {
				// return;
				continue;
			}

			// register all ofdb-relevant data of the given view to cache
			ViewConfigHandle viewHandle = this.viewConfigFactory.createViewConfiguration( ansichtDef.getName() );
			// } catch ( OfdbInvalidConfigurationException e ) {
			// LOGGER.error( e.getLocalizedMessage() );
			// }

			// ValidationResultSet viewValidationResultSet = this.ofdbValidator.validateViewConfiguration( viewHandle );
			//
			// if ( viewValidationResultSet.hasErrors() ) {
			// String msg = LocalizedMessages.getString( Constants.BUNDLE_NAME, "invalidConfiguration" );
			// throw new OfdbInvalidConfigurationException( msg );
			// // continue;
			// } else {
			this.ofdbCacheManager.registerView( viewHandle );
			// }

		}

		this.state = ApplicationState.RUNNING;
	}

	public void setOfdbCacheManager( final OfdbCacheManager ofdbCacheManager ) {
		this.ofdbCacheManager = ofdbCacheManager;
	}

	public ApplicationState getState() {
		return this.state;
	}

}
