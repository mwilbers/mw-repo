/**
 * 
 */
package de.mw.mwdata.core.web.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.IEntity;
import de.mw.mwdata.core.utils.PaginatedList;
import de.mw.mwdata.core.web.navigation.NavigationManager;
import de.mw.mwdata.core.web.navigation.NavigationState;
import de.mw.mwdata.core.web.util.SessionUtils;
import de.mw.mwdata.core.web.util.UrlManager;

/**
 * Tag-class for displaying the paging-objects-Paginglist.
 * 
 * @author Wilbers, Markus
 * @version 1.0
 * @since Nov, 2010
 * 
 */
public class PagingTag<E extends AbstractMWEntity> extends SimpleTagSupport {

	// ... 1. <aop :spring-configured/>
	// 2. define PagingTag in admin-servlet as bean
	// 3. inject urlManager bean here

	private PaginatedList<IEntity[]>	pList;
	private String							servletSubPath;

	public PagingTag() {
	}

	public void setPaginatedList( final PaginatedList<IEntity[]> pList ) {
		this.pList = pList;
	}

	public void setServletSubPath( final String servletSubPath ) {
		this.servletSubPath = servletSubPath;
	}

	@Override
	public void doTag() {

		// FIXME: replace urlManager with mwUrl if possible
		
		// try {
		JspWriter out = getJspContext().getOut();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		NavigationState state = SessionUtils.getNavigationState(request.getSession());

		final ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext( pageContext
				.getServletContext() );
		UrlManager urlManager = (UrlManager) context.getBean( "urlManager" );
		NavigationManager navManager = (NavigationManager) context.getBean( "navigationManager" );
		String menu = state.getMenue();

		String col = StringUtils.EMPTY;
		if ( !CollectionUtils.isEmpty( state.getSorting() ) ) {
			col = state.getSorting().get( 0 ).getSortColumn();
		}

		String direction = StringUtils.EMPTY;
		if ( !CollectionUtils.isEmpty( state.getSorting() ) ) {
			direction = state.getSorting().get( 0 ).getSortDirection().getName();
		}

		StringBuffer buf = new StringBuffer();
		if ( !this.pList.isPaging() ) {
			return;
		}

		Long lIndex;

		buf.append( urlManager.buildLink( request, this.servletSubPath, "1", 1, menu, col, direction ) );
		if ( this.pList.isArrowSmaller() ) {
			lIndex = this.pList.getIndexCurrentStep() - (this.pList.getStepsSmaller() + 1);
			buf.append( urlManager.buildLink( request, this.servletSubPath, "<", lIndex, menu, col, direction ) );
		}

		long steps = this.pList.getStepsSmaller();
		if ( steps > 0 ) {
			for ( long i = steps; i > 0; i-- ) {
				// buf.append(pList.getIndexCurrentStep() - i + " ");
				lIndex = new Long( this.pList.getIndexCurrentStep() - i );
				buf.append( urlManager.buildLink( request, this.servletSubPath, lIndex.toString(), lIndex, menu, col,
						direction ) );
			}
		}

		if ( this.pList.getIndexCurrentStep() > 1 && this.pList.getIndexCurrentStep() < this.pList.getNumPages() ) {
			// buf.append(pList.getIndexCurrentStep() + " ");
			lIndex = new Long( this.pList.getIndexCurrentStep() );
			buf.append( lIndex.toString() + " " ); // no link for current step
		}

		steps = this.pList.getStepsGreater();
		if ( steps > 0 ) {
			for ( long i = this.pList.getIndexCurrentStep() + 1; i <= this.pList.getIndexCurrentStep() + steps; i++ ) {
				// buf.append(i + " ");
				lIndex = new Long( i );
				buf.append( urlManager.buildLink( request, this.servletSubPath, lIndex.toString(), lIndex, menu, col,
						direction ) );
			}
		}

		if ( this.pList.isArrowGreater() ) {
			// buf.append("> ");
			lIndex = this.pList.getIndexCurrentStep() + this.pList.getStepsGreater() + 1;
			buf.append( urlManager.buildLink( request, this.servletSubPath, ">", lIndex, menu, col, direction ) );
		}

		if ( this.pList.getNumPages() > 1 ) {
			// buf.append(pList.getNumPages() + " ");
			lIndex = new Long( this.pList.getNumPages() );
			buf.append( urlManager.buildLink( request, this.servletSubPath, lIndex.toString(), lIndex, menu, col,
					direction ) );
		}

		try {
			out.write( buf.toString() );
		} catch ( IOException ioEx ) {

		}

	}

}
