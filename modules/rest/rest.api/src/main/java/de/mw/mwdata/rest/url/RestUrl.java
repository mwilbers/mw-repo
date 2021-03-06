package de.mw.mwdata.rest.url;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

/**
 * RestbasedMwUrl belongs following patterns:<br>
 * 
 * <code>
 * <nobr>http://localhost:8080/app.admin.client/admin/tabDef/ -> HTTP GET,
 * listAllEntities()</nobr>
 * <nobr>http://localhost:8080/app.admin.client/admin/tabDef/{entityId} -> HTTP
 * GET, MEDIATYPE-PRODUCE json, getEntity(entityId)</nobr>
 * <nobr>http://localhost:8080/app.admin.client/admin/tabDef/ -> HTTP POST,
 * createEntiy(entity)</nobr>
 * <nobr>http://localhost:8080/app.admin.client/admin/tabDef/{entityId} -> HTTP PUT,
 * updateEntiy(entityId)</nobr>
 * <nobr>http://localhost:8080/app.admin.client/admin/tabDef/{entityId} -> HTTP DELETE,
 * deleteEntiy(entityId)</nobr>
 * </code>
 * 
 * @author wilbersm
 *
 */
public class RestUrl {

	/**
	 * Example: protocol | host | port | applicationname | servletname | entityname
	 * http:// localhost: | 8080/ | app.admin.client/ | admin/ | tabDef/ |
	 * {tabDefId}, with HTTP-METHOD
	 * 
	 */

	private String protocol;
	private String host;
	private int port;
	private String applicationName; // /app.admin.web
	private String servletName; // /admin
	private String entityName; // /tabDef
	private int entityId;

	public RestUrl(final String absoluteUrl) throws InvalidRestUrlException {
		parse(absoluteUrl);
	}

	private void parse(final String absoluteUrl) throws InvalidRestUrlException {

		// e.g. "http://localhost:8080/app.admin.web/admin/tabDef/list.htm"
		URL aURL;
		try {
			aURL = new URL(absoluteUrl);
		} catch (MalformedURLException e) {
			String msg = MessageFormat.format("URL {0} not valid for usage in MWDATA REST API", absoluteUrl);
			throw new InvalidRestUrlException(msg, e);
		}
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

	private void parsePath(String path) throws InvalidRestUrlException {
		// e.g. /app.admin.web/admin/tabDef/{tabDefId}

		if (StringUtils.isBlank(path)) {
			throw new InvalidRestUrlException("Path is empty");
		}

		String[] pathTokens = StringUtils.split(path, "/");
		if (pathTokens.length < 2) {
			String msg = MessageFormat.format(
					"Invalid number of path-tokens in {0}. Minimal 3 tokens for app, servletname and entityname are necessary.",
					path);
			throw new InvalidRestUrlException(msg);
		}

		this.applicationName = pathTokens[0];
		this.servletName = pathTokens[1];
		if (pathTokens.length >= 3) {

			this.entityName = pathTokens[2];
			if (pathTokens.length == 3) {
				return;
			}

			try {
				this.entityId = Integer.parseInt(pathTokens[3]);
			} catch (NumberFormatException e) {
				String msg = MessageFormat.format("Invalid format of entityId. EntityId should be numeric.",
						pathTokens[3]);
				throw new InvalidRestUrlException(msg);
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
		return applicationName;
	}

	public String getServletPath() {
		return servletName;
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
