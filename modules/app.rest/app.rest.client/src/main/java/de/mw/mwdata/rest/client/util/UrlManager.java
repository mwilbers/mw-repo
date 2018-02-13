package de.mw.mwdata.rest.client.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import de.mw.mwdata.rest.client.Config;

/**
 * FIXME: methods should be consolidated with {@link MwUrl}
 * 
 * @author mwilbers
 * 
 */
@Deprecated
public class UrlManager {


	public UrlManager() {

	}


	private StringBuffer buildRequestUrl( final HttpServletRequest request, final String servletSubPath ) {

		StringBuffer buf = new StringBuffer();
		buf.append( request.getScheme() );
		buf.append( "://" );
		buf.append( request.getServerName() );
		buf.append( ":" );
		buf.append( request.getServerPort() );
		// buf.append( "/" );
		buf.append( request.getContextPath() );
		buf.append( "/" );
		buf.append( Config.SERVLET_PATH );
		buf.append( "/" );
		buf.append( servletSubPath );
		buf.append( "/" );

		return buf;

	}

	/**
	 * Builds an urlpath including the application-based path, the action and all request-params, e.g.<br>
	 * http://localhost:8080/app.admin.web/admin/tabDef/list.htm?pi=42
	 * 
	 * @param request
	 * 
	 * @param action
	 *            can be every available actiontype in the application (e.g. list, save, edit, filter)
	 * @return
	 */
	public String buildUrl( final HttpServletRequest request, final String servletSubPath, final String action,
			final int entityId, final String col, final String asc, final String menue, final long pageIndex ) {

		StringBuffer buf = buildRequestUrl( request, servletSubPath );
		buf.append( action );
		buf.append( ".htm" );
		buf.append( "?" );

		buf.append( "id=" + (entityId == 0 ? StringUtils.EMPTY : entityId) );
		buf.append( "&" );

		buf.append( "col=" + ObjectUtils.defaultIfNull( col, StringUtils.EMPTY ) );
		buf.append( "&" );

		buf.append( "asc=" + ObjectUtils.defaultIfNull( asc, StringUtils.EMPTY ) );
		buf.append( "&" );

		buf.append( "pi=" + ObjectUtils.defaultIfNull( pageIndex, "" ) );
		buf.append( "&" );

		buf.append( "menue=" + menue );
		buf.append( "&" );

		buf.deleteCharAt( buf.length() - 1 );

		return buf.toString();

	}

	/**
	 * Build a simple application-based relative urlpath to application-icons
	 * 
	 * @param request
	 * @param iconName
	 * @return
	 * @throws IllegalArgumentException
	 *             if the given iconName is not defined as enum-value
	 * @throws IOException
	 */
	public String buildIconUrl( final String iconKey, final HttpServletRequest request )
			throws IllegalArgumentException, IOException {

		Properties iconProps = PropertiesUtil.getIconProperties();

		// e.g. : /admin-web/icons/sortarrow_up.jpg
		StringBuffer buf = new StringBuffer();

		String applicationName = this.extractApplicationName( request );
		if ( !applicationName.startsWith( "/" ) ) {
			buf.append( "/" );
		}

		buf.append( applicationName );
		buf.append( "/icons/" );
		buf.append( iconProps.get( iconKey ) );

		return buf.toString();

	}

	private String extractApplicationName( final HttpServletRequest request ) {
		return request.getContextPath();
	}
	
	public String extractServletSubPath(final String requestUrl) throws MalformedURLException {
		MwUrl url = new MwUrl(requestUrl);
		
		return url.getServletSubPath();
	}

	/**
	 * Builds the simple HTML-code for an html-anchor embedding the list-action-link
	 * 
	 * @param text
	 * @param index
	 * @return
	 */
	public String buildLink( final HttpServletRequest request, final String servletSubPath, final String text,
			final long index, final String menu, final String col, final String asc ) {

		// this.addRequestParam( URL_PARAM.PAGE_INDEX, Long.toString( index ) );
		String url = this.buildUrl( request, servletSubPath, "list", 0, col, asc, menu, index );

		StringBuffer buf = new StringBuffer();
		buf.append( "<a href=\"" );
		buf.append( url );
		buf.append( "\" >" );
		buf.append( text );
		buf.append( "</a> " );

		return buf.toString();

	}
}
