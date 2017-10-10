package de.mw.mwdata.core.web.navigation;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.FrameworkServlet;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.web.control.AngWebRestController;
import de.mw.mwdata.core.web.util.SessionUtils;

public class NavigationFilter implements Filter {

	private ServletContext context;

	// public NavigationFilter() {
	// assert true;
	// }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.context = filterConfig.getServletContext();

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(); // get or initial create
														// session
		NavigationState state = SessionUtils.getNavigationState(session);

		WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(this.context,
				FrameworkServlet.SERVLET_CONTEXT_PREFIX + "admin");
		NavigationManager navigationManager = webContext.getBean(NavigationManager.class);

		if (null == state) {

			AngWebRestController<IEntity> controller = (AngWebRestController<IEntity>) webContext
					.getBean("angWebRestController");
			EntityTO<? extends AbstractMWEntity> filterSet = controller.populateFilterSet();

			state = navigationManager.createNavigationState(httpRequest.getRequestURL().toString(), filterSet);
			SessionUtils.setNavigationState(session, state);
		} else {
			// ... fixen: filterSet setzen in navigationManager , wenn wechsel
			// auf /tabSpeig
			// ... testen: wenn wechsel von tabDef auf tabSpeig, dann bislang
			// fehler, weil filterSet nicht besetzt war -> testen !
			navigationManager.applyUrlPath(httpRequest.getRequestURL().toString(), state);
			SessionUtils.setNavigationState(session, state);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
