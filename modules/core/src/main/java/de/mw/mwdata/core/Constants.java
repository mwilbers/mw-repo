/**
 *
 */
package de.mw.mwdata.core;

import org.apache.commons.lang.StringUtils;

/**
 * Main class for configuring globals, definitions and pathes for MWData.
 *
 * @author mwilbers
 *
 */
public final class Constants {

	// /**
	// * constant for core-Spring-Application-Context
	// */
	// public static final String APPCONTEXT_CORE = "appContext-core.xml";
	//
	// /**
	// * constant for fx-model-Spring-Application-Context
	// */
	// public static final String APPCONTEXT_OFDB = "appContext-ofdb.xml";

	// /**
	// * method for getting xml-based core-application-context
	// */
	// public static ApplicationContext getAppContextCore() {
	// ApplicationContext context = new ClassPathXmlApplicationContext(new String[]
	// { APPCONTEXT_CORE });
	// return context;
	// }
	//
	// /**
	// * method for getting xml-based ofdb-application-context
	// */
	// public static ApplicationContext getAppContextOfdb() {
	// ApplicationContext context = new ClassPathXmlApplicationContext(new String[]
	// { APPCONTEXT_OFDB });
	// return context;
	// }

	/* ************************ SYS-TABLE-COLUMNS ************************** */

	public static final String SYS_TAB_MENUS = "FX_Menues_K";

	/**
	 * global constant for sys-column IMPORTID
	 */
	public static final String SYS_COL_IMPORT_ID = "IMPORTID";
	/**
	 * global constant for sys-column TRANSAKTIONSID
	 */
	public static final String SYS_COL_TRANSAKTIONS_ID = "transaktionsId";

	/* ************************ PROPERTY-MAPPING ************************** */

	/**
	 * global constant for property id of entities<br>
	 * FIXME: usage should be checked if necessary ?
	 */
	public static final String SYS_PROP_ID = "id";

	/* ************************ USERS ************************** */

	/**
	 * global constant for User KHEIS
	 */
	public static final String SYS_USER_KHEIS = "kheis";
	public static final String USERNAME_ADMIN = "Administrator";
	public static final String USERNAME_GUEST = "Gast";

	/** constant for default user that datasets have been created by */
	public static final String SYS_USER_DEFAULT = "1";

	/* ************************ BEREICHE ************************** */

	/**
	 * global constant for BenutzerBereicheDef
	 */
	public static final String BEREICH_FREE = "Frei";
	public static final String BEREICH_ADMIN = "Administrator";

	/* ************************ CONSTANTS ************************** */

	public static final int SYS_VAL_TRUE = -1;
	public static final int SYS_VAL_FALSE = 0;

	public static final String SYS_VAL_TRUE_STRING = "true";
	public static final String SYS_VAL_FALSE_STRING = "false";
	public static final String SYS_VAL_NULL_STRING = StringUtils.EMPTY;

	public static final String BUNDLE_NAME_COMMON = "de.mw.mwdata.core.common.messages";

	/**
	 * Constants for FX_TabSpEig_K.defaultwert
	 *
	 * @author mwilbers
	 *
	 */
	public static enum MWDATADEFAULT {
		NOW("#mwdata#now"), // #FirstX#jetzt
		USERID("#mwdata#userid") //
		;

		private String name;

		private MWDATADEFAULT(final String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	// /**
	// * global constant for sys-column OFDB
	// */
	// public static final String SYS_COL_OFDB = "ofdb";

	/**
	 * global constant for sys-column SYSTEM
	 */
	public static final String SYS_COL_SYSTEM = "system";

	/**
	 * global constant for sys-column ANGELEGTAM
	 */
	public static final String SYS_COL_ANGELEGT_AM = "angelegtAm";

	/**
	 * global constant for sys-column ANGELEGTVON
	 */
	public static final String SYS_COL_ANGELEGT_VON = "angelegtVon";

}
