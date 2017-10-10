package de.mw.mwdata.core.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import de.mw.mwdata.core.ofdb.AbstractApplicationFactory;
import de.mw.mwdata.core.ofdb.ControllerConfiguration;
import de.mw.mwdata.core.ofdb.cache.ViewConfigHandle;
import de.mw.mwdata.core.ofdb.cache.ViewConfigValidationResultSet;
import de.mw.mwdata.core.ofdb.domain.IAnsichtDef;
import de.mw.mwdata.core.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.core.ofdb.domain.ITabDef;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.core.web.control.CrudController;
import de.mw.mwdata.core.web.control.GenericEntityController;

/**
 * This class resgisters the current application by its contextPath at the core-application by spring-injection.
 * 
 * @author mwilbers
 * @since May, 2013
 * @version 1.0
 * 
 */
public class DefaultApplicationFactory extends AbstractApplicationFactory {

	protected static Logger	log	= LoggerFactory.getLogger( DefaultApplicationFactory.class );

	public DefaultApplicationFactory(final String servletPath) {

		super( servletPath );
	}

	// @Override
	public void registerControllers( final ControllerConfiguration controllerConfiguration ) {

		DefaultControllerConfiguration defControllerConf = (DefaultControllerConfiguration) controllerConfiguration;
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) defControllerConf
				.getBeanFactory();

		List<ViewConfigHandle> viewConfigHandles = this.ofdbCacheManager.getRegisteredViewConfigs();

		for ( ViewConfigHandle viewHandle : viewConfigHandles ) {

			IAnsichtDef ansichtDef = viewHandle.getViewDef();
			IAnsichtTab ansichtTab = viewHandle.getMainAnsichtTab();

			ITabDef tabDef = ansichtTab.getTabDef();
			ViewConfigValidationResultSet tableResultSet = this.ofdbValidator.isTableValid( tabDef );
			if ( tableResultSet.hasErrors() ) {
				log.warn( tableResultSet.toString() );
				// log.warn( "Invalid TabDef-Configuration. Missing fullClassName for TabDef: " + tabDef.getName() );
				continue;
			}

			ViewConfigValidationResultSet ansichtResultSet = this.ofdbValidator.isAnsichtValid( ansichtDef );
			if ( ansichtResultSet.hasErrors() ) {
				log.warn( ansichtResultSet.toString() );
				// log.warn( "TabDef is defined, but no AnsichtDef / servletPath is defined for TabDef: "
				// + tabDef.getName() );
				continue;
			}

			
			
			String urlPath = ansichtDef.getUrlPath();
			Class<CrudController<?>> controllerClazz = defControllerConf.getControllerClass( urlPath );

			Class clazz = ClassNameUtils.loadClass( tabDef.getFullClassName() );

			// http://issues.appfuse.org/secure/attachmentzip/unzip/11137/10327%5B3%5D/GenericApplicationStackAnnotationBeanFactoryPostProcessor.java
			// ...http://42-incorrect-answers.blogspot.com/2009/08/marrying-gwt-with-spring-generic-way.html
			// create genericController
			String controllerName = defControllerConf.getControllerName( urlPath );
			AbstractBeanDefinition controllerBean = createGenericEntityController( controllerClazz, clazz,
					urlPath, defaultListableBeanFactory, controllerName );
			// defaultListableBeanFactory.registerBeanDefinition( controllerName, controllerBean );
			log.debug( "******************* Registered entityController: " + controllerName );

		}

	}

	private AbstractBeanDefinition createGenericEntityController( final Class<CrudController<?>> controllerClazz,
			final Class<?> clazz, final String urlPath, final DefaultListableBeanFactory defaultListableBeanFactory,
			final String controllerName ) {

		BeanDefinitionBuilder genericFormControllerBuilder = null;
		if ( null != controllerClazz ) {
			genericFormControllerBuilder = BeanDefinitionBuilder.rootBeanDefinition( controllerClazz );
		} else {
			genericFormControllerBuilder = BeanDefinitionBuilder.rootBeanDefinition( GenericEntityController.class );
			genericFormControllerBuilder.addConstructorArgValue( clazz );
		}

		genericFormControllerBuilder.addPropertyReference( "benutzerBereichEditor", "benutzerBereichEditor" );
		genericFormControllerBuilder.addPropertyReference( "enumDBTYPEEditor", "enumDBTYPEEditor" );
		genericFormControllerBuilder.addPropertyReference( "enumMENUETYPEEditor", "enumMENUETYPEEditor" );
		genericFormControllerBuilder.addPropertyReference( "ofdbController", "ofdbController" );
		genericFormControllerBuilder.addPropertyReference( "ofdbService", "ofdbService" );
		genericFormControllerBuilder.addPropertyReference( "crudService", "crudService" );
		genericFormControllerBuilder.addPropertyReference( "navigationManager", "navigationManager" );
		genericFormControllerBuilder.addPropertyReference( "ofdbCacheManager", "ofdbCacheManager" );

		genericFormControllerBuilder.addPropertyValue( "url", urlPath );
		genericFormControllerBuilder.addPropertyValue( "messageCodePrefix", urlPath + "." );

		// http://www.google.com/url?sa=t&source=web&cd=4&ved=0CDMQFjAD&url=http%3A%2F%2Fstackoverflow.com%2Fquestions%2F15415688%2Fspring-bean-custom-scope-jms&ei=612GUoqJO8PVtQa8oIGwCw&usg=AFQjCNGpYjijf7gfksUrHavUChVlRpJO3Q

		AbstractBeanDefinition beanDefinition = genericFormControllerBuilder.getBeanDefinition();
		
//		... WebIntegrationTest schlägt fehl, da navigationManager als 
//		session-scoped irgendwo von singelton referenziert wird
		
		beanDefinition.setScope("prototype"); // FIXME: should be conversation
		
		// FIXME: commented: no controllers more
//		BeanDefinitionHolder holder = new BeanDefinitionHolder( beanDefinition, controllerName );
//		BeanDefinitionHolder proxyBean = ScopedProxyUtils.createScopedProxy( holder, defaultListableBeanFactory, false );

		// BeanDefinitionReaderUtils.registerBeanDefinition( beanDefinition, defaultListableBeanFactory );
		// defaultListableBeanFactory.registerBeanDefinition( controllerName, proxyBean.getBeanDefinition() );
		return beanDefinition;
	}

}
