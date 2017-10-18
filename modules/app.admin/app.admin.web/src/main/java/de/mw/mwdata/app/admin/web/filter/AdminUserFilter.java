package de.mw.mwdata.app.admin.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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

	
	private static final Logger	LOGGER	= Logger.getLogger( AdminUserFilter.class );

	// private ServletContext context;

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

		if ( StringUtils.isEmpty( menueId ) ) {
			LOGGER.error( "Request-Parameter menue is missing in Filter." );
		}

//		IOfdbRechteService ofdbRechteService = (IOfdbRechteService) WebApplicationContextUtils
//				.getWebApplicationContext( this.context ).getBean( "ofdbRechteService" );

		// FIXME: just provisoric for development here: set always user ADMINISTRATOR
		// for development-purposes: get user Administrator and save it to session (TODO)
		if ( !SessionUtils.isUserLoggedIn( httpRequest.getSession() ) ) {
			
			// FIXME: not yet implemented ... just new Benutzer() provisoric
			
//			Benutzer benutzerAdmin = ofdbRechteService.findBenutzerWithRechte( de.mw.mwdata.core.Constants.USERNAME_ADMIN );
			SessionUtils.setAttribute( httpRequest.getSession(), "benutzer", new Benutzer() );
		}

		if ( SessionUtils.isUserLoggedIn( httpRequest.getSession() ) ) {

			// IOfdbService ofdbService = (IOfdbService) WebApplicationContextUtils
			// .getWebApplicationContext( this.context ).getBean( "ofdbService" );
			// Menue menue = ofdbService.findMenuById( Long.valueOf( menueId ) );

			// if user is not Administrator, than site is forbidden !!
			Benutzer benutzer = (Benutzer) SessionUtils.getAttribute( httpRequest.getSession(), "benutzer" );

			// ... todo:
			// 1. allgemeinen Admin-UserFilter in core-rights schreiben, der
			// isAuthorized(benutzer, ansichtId)aufruft
			// 2. hier AdminUserFilter mit eigenem Filter überschreibne, der nur auswertet, ob der an
			// gemeldete User ADMINISTRATOR ist
			// 3. in web.xml prüfen, ob richtiger Filter eingebunden ist
			// 4. testen
			// 5. von Architektur UML-Klassendiagramm machen für Übersicht, Einarbeitung
			// 6. todos, Bugs in JIRA erfassen

			// List<BenutzerRecht> benutzerRechte = benutzer.getBenutzerRechte();
			// BenutzerBereich bereich = benutzerRechte.get( 0 ).getBereich();
//			if ( !ofdbRechteService.isAdmin( benutzer ) ) {
//				httpResponse.sendRedirect( httpRequest.getContextPath() + "/forbidden.jsp" );
//
//			} else {
				chain.doFilter( request, response );
//			}

		} else {

			// TODO: instead of forbidden do redirect to Login-Page
			httpResponse.sendRedirect( httpRequest.getContextPath() + "/forbidden.jsp" );
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
