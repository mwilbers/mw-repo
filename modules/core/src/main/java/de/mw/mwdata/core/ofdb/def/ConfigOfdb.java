/**
 *
 */
package de.mw.mwdata.core.ofdb.def;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Wilbers, Markus
 * @since 2011
 *
 */
public class ConfigOfdb {

	/**
	 * constant for fx-ofdb-Spring-Application-Context
	 */
	@Deprecated
	public static final String APPCONTEXT_OFDB = "appContext-ofdb.xml";

	/**
	 * method for getting xml-based fx-ofdb-application-context
	 */
	@Deprecated
	public static ApplicationContext getAppContextOfdb() {
		ApplicationContext context = new ClassPathXmlApplicationContext( new String[] { APPCONTEXT_OFDB } );
		return context;
	}

	public static final String	SYS_COL_ZEITTYP_VONBIS	= "VonBis";

	public static final String	T_VIEWDEF				= "AnsichtDef";
	public static final String	T_VIEWTAB				= "AnsichtTab";
	public static final String	T_VIEWPROPS				= "AnsichtSpalten";
	public static final String	T_VIEWORDERBY			= "AnsichtOrderBy";
	public static final String	T_TABDEF				= "TabDef";
	public static final String	T_TABPROPS				= "TabSpeig";
	public static final String	T_MENU					= "Menue";

}
