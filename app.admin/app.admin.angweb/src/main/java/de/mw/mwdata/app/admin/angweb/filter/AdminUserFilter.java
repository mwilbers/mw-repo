package de.mw.mwdata.app.admin.angweb.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mw.mwdata.core.domain.Benutzer;
import de.mw.mwdata.core.web.UserFilter;
import de.mw.mwdata.core.web.util.SessionUtils;

/**
 * Adminuser-Filter that checks, if the logged user is Administrator. Else redirect to forbidden-page.
 * 
 * @author mwilbers
 * @since June, 2012
 * @version 1.0
 * 
 */
public class AdminUserFilter extends UserFilter {

	
	private static final Logger	LOGGER	= LoggerFactory.getLogger( AdminUserFilter.class );

	@Override
	public void init( final FilterConfig filterConfig ) throws ServletException {
		this.context = filterConfig.getServletContext();
		
		// ... bug here.: ho init-call : check filter-property: setTargetFilterLifecycle
	}

	@Override
	public void doFilter( final ServletRequest request, final ServletResponse response, final FilterChain chain )
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String menueId = httpRequest.getParameter( "menue" );
		
//		... idee navigationMamager: 
//			1. singelton-bean navigationStateHolder mit allen NavigationState-Klassen, singleton
//			NavigationManager, der diese states nimmt und weiterverarbetitet
//			bei session-anfang und ende wird NavigationState-Objekt instanziiert und über AdminUserFilter in 
//			Holder eingefügt
		
		if ( StringUtils.isEmpty( menueId ) ) {
			LOGGER.error( "Request-Parameter menue is missing in Filter." );
		}

//		IOfdbRechteService ofdbRechteService = (IOfdbRechteService) WebApplicationContextUtils
//				.getWebApplicationContext( this.context ).getBean( "ofdbRechteService" );

		// FIXME: httpRequest.getSession().invalidate() has to be called before new session is created:
		// http://stackoverflow.com/questions/10076243/how-to-create-a-new-session-in-spring
		// use @SessionAttributes for navigationState-objectin controller: 
		// http://fruzenshtein.com/spring-mvc-session-advanced/
		// session-timeout in web.xml definieren und verproben
		
		// FIXME: just provisoric for development here: set always user ADMINISTRATOR
		// for development-purposes: get user Administrator and save it to session (TODO)
		if ( !SessionUtils.isUserLoggedIn( httpRequest.getSession() ) ) {
			
			// FIXME: not yet implemented ... just new Benutzer() provisoric
			
			//			Benutzer benutzerAdmin = ofdbRechteService.findBenutzerWithRechte( de.mw.mwdata.core.Constants.USERNAME_ADMIN );
			SessionUtils.setAttribute( httpRequest.getSession(), "benutzer", new Benutzer() );
		}

		if ( SessionUtils.isUserLoggedIn( httpRequest.getSession() ) ) {

			// if user is not Administrator, than site is forbidden !!
//			Benutzer benutzer = (Benutzer) SessionUtils.getAttribute( httpRequest.getSession(), "benutzer" );
			chain.doFilter( request, response );

		} else {

			// TODO: instead of forbidden do redirect to Login-Page
			httpResponse.sendRedirect( httpRequest.getContextPath() + "/forbidden.jsp" );
		}

	}

	@Override
	public void destroy() {
		LOGGER.info("destroy called");
		
		// NOTE: session does not need to be destroyed. this is just Tomcat configuration in <%tomcat_homepath%>/conf/context.xml
		
	}

}
