package de.mw.mwdata.core.web.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

/**
 * RestbasedMwUrl belongs following patterns:<br>
 * 
 * <code>
 * <nobr>http://localhost:8080/app.admin.angwebrest/admin/tabDef/ -> HTTP GET,
 * listAllEntities()</nobr>
 * <nobr>http://localhost:8080/app.admin.angwebrest/admin/tabDef/{entityId} -> HTTP
 * GET, MEDIATYPE-PRODUCE json, getEntity(entityId)</nobr>
 * <nobr>http://localhost:8080/app.admin.angwebrest/admin/tabDef/ -> HTTP POST,
 * createEntiy(entity)</nobr>
 * <nobr>http://localhost:8080/app.admin.angwebrest/admin/tabDef/{entityId} -> HTTP PUT,
 * updateEntiy(entityId)</nobr>
 * <nobr>http://localhost:8080/app.admin.angwebrest/admin/tabDef/{entityId} -> HTTP DELETE,
 * deleteEntiy(entityId)</nobr>
 * </code>
 * 
 * @author wilbersm
 *
 */
public class RestbasedMwUrl {

	/**
	 * Example: protocol | host | port | contextpath | servletpath |
	 * servletsuppath http:// localhost: | 8080/ | app.admin.web/ | admin/ |
	 * tabDef/ | {tabDefId}, with HTTP-METHOD
	 * 
	 */

	private String protocol;
	private String host;
	private int port;
	private String contextPath; // /app.admin.web
	private String servletPath; // /admin
	private String entityName; // /tabDef
	private int entityId;

	// private String action; // /list.htm

	public RestbasedMwUrl(final String absoluteUrl) throws MalformedURLException {
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
		// e.g. /app.admin.web/admin/tabDef/{tabDefId}

		if (StringUtils.isBlank(path)) {
			throw new MalformedURLException("Path is empty");
		}

		String[] pathTokens = StringUtils.split(path, "/");
		if (pathTokens.length < 2) {
			throw new MalformedURLException(
					"Invalid number of path-tokens. Minimal 3 tokens for app, servletname and entityname are necessary.");
		}

		this.contextPath = pathTokens[0];
		this.servletPath = pathTokens[1];
		if (pathTokens.length >= 3) {
			this.entityName = pathTokens[2];
			if (pathTokens.length == 3) {
				return;
			}

			try {
				this.entityId = Integer.parseInt(pathTokens[3]);
			} catch (NumberFormatException e) {
				throw new MalformedURLException("Invalid format of entityId. EntityId should be numeric.");
			}

		}

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
		b.append("', entityName = '");
		b.append(this.getEntityName());
		b.append("', entityId = '");
		b.append(this.getEntityId());
		b.append("']");
		return b.toString();
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public boolean hasEntityName() {
		return !StringUtils.isEmpty(this.entityName);
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

}
