package de.mw.mwdata.core.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import de.mw.mwdata.core.ofdb.ApplicationFactory;
import de.mw.mwdata.core.ofdb.ControllerConfiguration;

public class ControllerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	protected static Logger			log	= LoggerFactory.getLogger( ControllerBeanFactoryPostProcessor.class );

	private ControllerConfiguration	controllerConfiguration;

	private ApplicationFactory		applicationFactory;

	public void setControllerConfiguration( final ControllerConfiguration controllerConfiguration ) {
		this.controllerConfiguration = controllerConfiguration;
	}

	public void setApplicationFactory( final ApplicationFactory applicationFactory ) {
		this.applicationFactory = applicationFactory;
	}

	// @Override
	public void postProcessBeanFactory( final ConfigurableListableBeanFactory beanFactory ) throws BeansException {

		// ... http: // stackoverflow.com/questions/8339112/hard-coded-requestmapping-url-in-spring-mvc-controller?rq=1
		// http://static.springsource.org/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-ann-requestmapping-31-vs-30

		log.debug( "**************** Registration of OFDB-Objects * START ************************" );

		((DefaultControllerConfiguration) this.controllerConfiguration)
				.setConfigurableListableBeanFactory( beanFactory );
		this.applicationFactory.registerControllers( this.controllerConfiguration );

		log.debug( "**************** Registration of OFDB-Objects * END ************************" );

	}
}
