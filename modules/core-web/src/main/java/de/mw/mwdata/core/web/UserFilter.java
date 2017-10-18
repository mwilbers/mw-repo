package de.mw.mwdata.core.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import de.mw.mwdata.core.domain.Benutzer;
import de.mw.mwdata.core.ofdb.domain.IMenue;
import de.mw.mwdata.core.ofdb.service.IOfdbService;
import de.mw.mwdata.core.web.util.SessionUtils;

/**
 * User-filter checks if the given user is authorized in Ofdb for the requested AnsichtDef by the choosen menu
 * 
 * @author mwilbers
 * 
 */
public class UserFilter implements Filter {

	// private IOfdbRechteService ofdbRechteService;

	protected ServletContext	context;

	// @Override
	public void init( final FilterConfig filterConfig ) throws ServletException {
		this.context = filterConfig.getServletContext();

	}

	// @Override
	public void doFilter( final ServletRequest request, final ServletResponse response, final FilterChain chain )
			throws IOException, ServletException {

		// TODO: move all the benutzer-handling-code to a spring-session-scoped bean
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String menueId = httpRequest.getParameter( "menue" );

		if ( SessionUtils.isUserLoggedIn( httpRequest.getSession() ) ) {

			IOfdbService ofdbService = (IOfdbService) WebApplicationContextUtils
					.getWebApplicationContext( this.context ).getBean( "ofdbService" );
			IMenue menue = ofdbService.findMenuById( Long.valueOf( menueId ) );

			// if user is not Administrator, than site is forbidden !!
			Benutzer benutzer = (Benutzer) SessionUtils.getAttribute( httpRequest.getSession(), "benutzer" );

//			IOfdbRechteService ofdbRechteService = (IOfdbRechteService) WebApplicationContextUtils
//					.getWebApplicationContext( this.context ).getBean( "ofdbRechteService" );
//
//			AnsichtDef ansicht = ofdbService.findAnsichtById( menue.getAnsichtDef().getId() );
//
//			if ( !ofdbRechteService.isAuthorized( benutzer, ansicht ) ) {
//				httpResponse.sendRedirect( httpRequest.getContextPath() + "/forbidden.jsp" );
//			} else {
//				chain.doFilter( request, response );
//			}

		} else {

			// TODO: instead of forbidden do redirect to Login-Page
			httpResponse.sendRedirect( httpRequest.getContextPath() + "/forbidden.jsp" );
		}

		chain.doFilter( request, response );
	}

	// @Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	// public void setOfdbRechteService( final IOfdbRechteService ofdbRechteService ) {
	// this.ofdbRechteService = ofdbRechteService;
	// }

}
