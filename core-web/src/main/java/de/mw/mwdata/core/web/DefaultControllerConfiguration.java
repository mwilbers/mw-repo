package de.mw.mwdata.core.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import de.mw.mwdata.core.ofdb.ControllerConfiguration;
import de.mw.mwdata.core.utils.ClassNameUtils;
import de.mw.mwdata.core.web.control.CrudController;

/**
 * 
 * @author mwilbers
 * 
 */
public class DefaultControllerConfiguration implements ControllerConfiguration {

	protected static Logger						log							= LoggerFactory.getLogger( DefaultControllerConfiguration.class );

	private Map<String, Class<CrudController<?>>>	overwrittenControllerMap	= new HashMap<String, Class<CrudController<?>>>();

	private ConfigurableListableBeanFactory		beanFactory;

	public void setConfigurableListableBeanFactory( final ConfigurableListableBeanFactory beanFactory ) {
		this.beanFactory = beanFactory;
	}

	public ConfigurableListableBeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	public DefaultControllerConfiguration() {
		log.debug( "in controllerFactory" );

		// ... beim deploy und aufruf wird controllerFactory / TabDefController zweimal instanziiert: warum ?
		// ... dann fehler cannot convert item.datenbank from string to enum ...

	}

	public Class<CrudController<?>> getControllerClass( final String urlPath ) {
		return this.overwrittenControllerMap.get( urlPath );
	}

	public void setOverwrittenControllerMap( final Map<String, Class<CrudController<?>>> overwrittenControllerMap ) {
		this.overwrittenControllerMap = overwrittenControllerMap;
	}

	public String getBeanName( final String fullQualifiedClassName ) {
		String beanName = Config.PREFIX_FILTEROBJECT;
		if ( StringUtils.isEmpty( fullQualifiedClassName ) ) {
			log.error( "Missing ClassName : " + fullQualifiedClassName );
			return StringUtils.EMPTY;
		}

		String simpleClassName = ClassNameUtils.getSimpleClassName( fullQualifiedClassName );
		if ( null == simpleClassName ) {
			log.error( "Invalid ClassName : " + fullQualifiedClassName );
			return StringUtils.EMPTY;
		}

		beanName += simpleClassName;
		return beanName;
	}

	public String getControllerName( final String urlPath ) {
		return urlPath + Config.SUFFIX_CONTROLLER;
	}

}
