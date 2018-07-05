package de.mw.mwdata.rest.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import de.mw.mwdata.rest.navigation.NavigationManager;
import de.mw.mwdata.rest.navigation.NavigationState;

public class SessionUtils {

	protected static Logger log = LoggerFactory.getLogger(SessionUtils.class);

	private static final String MW_SESSION_URLPATH = "urlPath";

	public static void logSession(final HttpSession session) {

		log.info(" ************** Session-Log Start ***************");

		Enumeration attributeNames = session.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String key = (String) attributeNames.nextElement();
			Object o = session.getAttribute(key);
			log.info("Entry: Key: " + key + ", Value: " + o.toString());

		}

		log.info(" ************** Session-Log Ende ***************");

	}

	public static void setAttribute(final HttpSession session, final String key, final Object value) {
		session.setAttribute(key, value);
	}

	public static Object getAttribute(final HttpSession session, final String key) {
		return session.getAttribute(key);
	}

	public static boolean isUserLoggedIn(final HttpSession session) {
		return null != session.getAttribute("benutzer");
	}

	public static NavigationState getNavigationState(final HttpSession session) {
		return (NavigationState) session.getAttribute(NavigationManager.SESSIONKEY_NAVIGATIONSTATE);
	}

	public static void setNavigationState(final HttpSession session, final NavigationState state) {
		session.setAttribute(NavigationManager.SESSIONKEY_NAVIGATIONSTATE, state);
	}

	public static void invalidate(final HttpSession session) {
		session.invalidate();
	}

	public static HttpServletRequest getHttpServletRequest() {
		HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		return curRequest;
	}

	public static String getLastUrlPath() {
		HttpSession httpSession = getHttpServletRequest().getSession();
		return (String) SessionUtils.getAttribute(httpSession, SessionUtils.MW_SESSION_URLPATH);
	}

	public static void setLastUrlPath(final String urlPath) {
		HttpSession httpSession = getHttpServletRequest().getSession();
		setAttribute(httpSession, MW_SESSION_URLPATH, urlPath);
	}

}
