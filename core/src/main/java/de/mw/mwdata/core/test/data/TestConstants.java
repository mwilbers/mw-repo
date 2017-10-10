/**
 *
 */
package de.mw.mwdata.core.test.data;

/**
 * This class is only needed for tests and contains all java-constants for tables, db-columns and other objects which
 * are referenced in the unit-tests.
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 *
 */
public class TestConstants {

	// tablenames
	public static final String	TABLENAME_TABDEF			= "FX_TabDef_K";
	public static final String	TABLENAME_TABSPEIG			= "FX_TabSpEig_K";
	public static final String	TABLENAME_ANSICHTSPALTEN	= "FX_AnsichtSpalten_K";
	public static final String	TABLENAME_ANSICHTDEF		= "FX_AnsichtDef_K";
	public static final String	TABLENAME_SYSSEQUENZ		= "SysSequenz";
	public static final String	TABLENAME_TESTPERSON		= "TEST_PERSON";
	public static final String	TABLENAME_TESTGRUPPE		= "TEST_GRUPPE";
	public static final String	TABLENAME_TESTGRUPPENTYP	= "TEST_GRUPPENTYP";
	public static final String	TABLENAME_BENUTZERBEREICH	= "BenutzerBereicheDef";

	// // testuser
	// public static final String TESTPERSON = "Mustermann";

	// sequence-keys
	public static final String	SEQUENCEKEY_SYSSEQUENZ		= "SysSequenz:SequenzID";
	public static final String	SEQUENCEKEY_TABDEF			= "FX_TabDef_K:DSID";
	public static final String	SEQUENCEKEY_TABSPEIG		= "FX_TabSpEig_K:DSID";
	public static final String	SEQUENCEKEY_MENUE			= "FX_Menues_K:DSID";
	public static final String	SEQUENCEKEY_BENUTZERBEREICH	= "BenutzerBereicheDef:BereichsID";
	public static final String	SEQUENCEKEY_ANSICHT			= "FX_AnsichtDef_K:DSID";
	public static final String	SEQUENCEKEY_ANSICHTORDER	= "FX_AnsichtOrderBy_K:DSID";
	public static final String	SEQUENCEKEY_ANSICHTTAB		= "FX_AnsichtTab_K:DSID";
	public static final String	SEQUENCEKEY_BENUTZER		= "Benutzerdef:DSID";
	public static final String	SEQUENCEKEY_ANSICHTSPALTEN	= "FX_AnsichtSpalten_K:DSID";

	// testbereiche
	public static final String	BEREICH_TEST				= "Testbereich";

	// testuser
	public static final String	TESTBENUTZER				= "Mustermann";

}
