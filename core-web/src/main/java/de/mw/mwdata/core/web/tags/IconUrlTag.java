package de.mw.mwdata.core.web.tags;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import de.mw.mwdata.core.web.util.UrlManager;

public class IconUrlTag extends SimpleTagSupport {

	private String				iconName;
	private HttpServletRequest	request;

	@Override
	public void doTag() throws JspException, IOException {

		// /admin-web/icons/sortarrow_up.jpg

		JspWriter out = getJspContext().getOut();
		PageContext pageContext = (PageContext) getJspContext();
		this.request = (HttpServletRequest) pageContext.getRequest();

		UrlManager urlManager = new UrlManager();
		String url = urlManager.buildIconUrl( this.iconName, this.request );
		out.write( url );

	}

	public void setIconName( final String iconName ) {
		this.iconName = iconName;
	}

}
