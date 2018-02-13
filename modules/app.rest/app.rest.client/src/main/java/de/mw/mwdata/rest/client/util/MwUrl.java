package de.mw.mwdata.rest.client.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

/**
 * FIXME class can be removed ?
 * 
 * @author wilbersm
 *
 */
@Deprecated
public class MwUrl {

	/**
	 * Example: protocol host port contextpath servletpath servletsuppath action
	 * http:// localhost: 8080/ app.admin.web/ admin/ tabDef/ list.htm
	 * 
	 */

	private String protocol;
	private String host;
	private int port;
	private String contextPath; // /app.admin.web
	private String servletPath; // /admin
	private String servletSubPath; // /tabDef
	// private String action; // /list.htm

	public MwUrl(final String absoluteUrl) throws MalformedURLException {
		parse(absoluteUrl);
	}

	private void parse(final String absoluteUrl) throws MalformedURLException {

		// e.g. "http://localhost:8080/app.admin.web/admin/tabDef/list.htm"
		URL aURL = new URL(absoluteUrl);
		this.protocol = aURL.getProtocol();
		this.host = aURL.getHost();
		this.port = aURL.getPort();

		parsePath(aURL.getPath());

		// System.out.println("protocol = " + aURL.getProtocol());
		// System.out.println("authority = " + aURL.getAuthority());
		// System.out.println("host = " + aURL.getHost());
		// System.out.println("port = " + aURL.getPort());
		// System.out.println("path = " + aURL.getPath());
		// System.out.println("query = " + aURL.getQuery());
		// System.out.println("filename = " + aURL.getFile());
		// System.out.println("ref = " + aURL.getRef());

	}

	private void parsePath(String path) throws MalformedURLException {
		// e.g. /app.admin.web/admin/tabDef/list.htm

		if (StringUtils.isBlank(path)) {
			throw new MalformedURLException("Path is empty");
		}

		String[] pathTokens = StringUtils.split(path, "/");
		if (pathTokens.length != 3) {
			throw new MalformedURLException("Invalid number of path-tokens.");
		}

		this.contextPath = pathTokens[0];
		this.servletPath = pathTokens[1];
		this.servletSubPath = pathTokens[2];
		// this.action = pathTokens[3];

	}

	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getServletPath() {
		return servletPath;
	}

	public String getServletSubPath() {
		return servletSubPath;
	}

	// public String getAction() {
	// return action;
	// }

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("MwUrl [ protocol = '");
		b.append(this.getProtocol());
		b.append("', host = '");
		b.append(this.getHost());
		b.append("', port = '");
		b.append(this.getPort());
		b.append("', contextPath = '");
		b.append(this.getContextPath());
		b.append("', servletPath = '");
		b.append(this.getServletPath());
		b.append("', servletSubPath = '");
		b.append(this.getServletSubPath());
		// b.append("', action = '");
		// b.append(this.getAction());
		b.append("']");
		return b.toString();
	}

}
