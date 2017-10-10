package de.mw.mwdata.core.web.tags;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import de.mw.mwdata.core.web.navigation.NavigationManager;
import de.mw.mwdata.core.web.util.UrlManager;

/**
 * MW-Tag-class for displaying the MW-specific URLs.
 * 
 * @author Wilbers, Markus
 * @version 1.0
 * @since Jan, 2012
 * 
 */
public class UrlTag extends SimpleTagSupport {

	private String	servletSubPath;

	private int		entityId;

	private String	action;

	private String	col;

	private String	asc;

	private String	menue;

	@Override
	public void doTag() throws JspException, IOException {

		JspWriter out = getJspContext().getOut();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

//		... 
//		1. navigationManager defined in admin-servlet but needed in core-web.app.context
//		2. handle eclipse problems view (wrong J2SE-1.5 JRE) and handle wrong formatting in GEnericEntiryCotnroller
//		
		final ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext( pageContext
				.getServletContext() );
		NavigationManager navManager = (NavigationManager) context.getBean( "navigationManager" );
		UrlManager urlManager = (UrlManager) context.getBean( "urlManager" );

		String url = urlManager.buildUrl( request, this.servletSubPath, this.action, this.entityId, this.col, this.asc,
				this.menue, 1l );
		out.write( url );

	}

	public void setServletSubPath( final String servletSubPath ) {
		this.servletSubPath = servletSubPath;
	}

	public void setEntityId( final int entityId ) {
		this.entityId = entityId;
	}

	public void setAction( final String action ) {
		this.action = action;
	}

	public void setCol( final String col ) {
		this.col = col;
	}

	public void setAsc( final String asc ) {
		this.asc = asc;
	}

	public void setMenue( final String menue ) {
		this.menue = menue;
	}

}
