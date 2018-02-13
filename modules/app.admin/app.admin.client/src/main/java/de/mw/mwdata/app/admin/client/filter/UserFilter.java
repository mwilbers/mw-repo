package de.mw.mwdata.app.admin.client.filter;

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

/**
 * User-filter checks if the given user is authorized in Ofdb for the requested
 * AnsichtDef by the choosen menu
 * 
 * @author mwilbers
 * 
 */
public class UserFilter implements Filter {

	protected ServletContext context;

	public void init(final FilterConfig filterConfig) throws ServletException {
		this.context = filterConfig.getServletContext();

	}

	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		// TODO: move all the benutzer-handling-code to a spring-session-scoped bean
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String menueId = httpRequest.getParameter("menue");

		// if (SessionUtils.isUserLoggedIn(httpRequest.getSession())) {
		//
		// IOfdbService ofdbService = (IOfdbService)
		// WebApplicationContextUtils.getWebApplicationContext(this.context)
		// .getBean("ofdbService");
		//
		// // if user is not Administrator, than site is forbidden !!
		// Benutzer benutzer = (Benutzer)
		// SessionUtils.getAttribute(httpRequest.getSession(), "benutzer");
		//
		// // IOfdbRechteService ofdbRechteService = (IOfdbRechteService)
		// // WebApplicationContextUtils
		// // .getWebApplicationContext( this.context ).getBean( "ofdbRechteService" );
		// //
		// // AnsichtDef ansicht = ofdbService.findAnsichtById(
		// // menue.getAnsichtDef().getId() );
		// //
		// // if ( !ofdbRechteService.isAuthorized( benutzer, ansicht ) ) {
		// // httpResponse.sendRedirect( httpRequest.getContextPath() + "/forbidden.jsp"
		// );
		// // } else {
		// // chain.doFilter( request, response );
		// // }
		//
		// } else {
		//
		// // TODO: instead of forbidden do redirect to Login-Page
		// httpResponse.sendRedirect(httpRequest.getContextPath() + "/forbidden.jsp");
		// }

		chain.doFilter(request, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
