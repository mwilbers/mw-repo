package de.mw.mwdata.app.admin.angwebrest.config;

import javax.servlet.Filter;

import de.mw.mwdata.core.web.CorsFilter;

public class AngWebRestInitializer /*
									 * extends
									 * AbstractAnnotationConfigDispatcherServletInitializer
									 */ {

	// @Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AngWebRestConfiguration.class };
	}

	// @Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	// @Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	// @Override
	protected Filter[] getServletFilters() {
		Filter[] singleton = { new CorsFilter() };
		return singleton;
	}

}