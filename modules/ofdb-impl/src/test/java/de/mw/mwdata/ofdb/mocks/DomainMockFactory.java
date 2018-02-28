/**
 *
 */
package de.mw.mwdata.ofdb.mocks;

import java.util.Date;
import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.Sequence;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.ITabDef.DATENBANK;
import de.mw.mwdata.ofdb.domain.ITabSpeig.DBTYPE;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtSpalten;
import de.mw.mwdata.ofdb.domain.impl.AnsichtTab;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.domain.impl.TabSpeig;

/**
 * Class for creating Mock-Objects of MW-Domain-classes.
 *
 * @author Wilbers, Markus
 * @version 1.0
 * @since Mar, 2011
 *
 */
public class DomainMockFactory {

	/**
	 * Creates a TabSpeig-object with the given tableName, spalteName, reihenfolge. Uses default-values for DbDatentyp,
	 * PrimSchluessel, EingabeNotwendig, BearbErlaubt and SystemWert
	 *
	 * @param tableName
	 * @param spalteName
	 * @param reihenfolge
	 * @return
	 */
	public static TabSpeig createTabSpeigMock( final TabDef tabDef, final String spalteName, final long reihenfolge,
			final DBTYPE dbType ) {

		TabSpeig tabSpeig = new TabSpeig();
		tabSpeig.setSpalte( spalteName );

		tabSpeig.setTabDef( tabDef );
		tabSpeig.setSpaltenkopf( spalteName );
		tabSpeig.setReihenfolge( reihenfolge );
		tabSpeig.setDbDatentyp( dbType );

		tabSpeig.setPrimSchluessel( false );
		tabSpeig.setEingabeNotwendig( false );
		tabSpeig.setBearbErlaubt( true );
		tabSpeig.setSystemWert( false );

		tabSpeig.setOfdb( "X" );
		tabSpeig.setSystem( Boolean.FALSE );

		// setDefaultCreationProperties( tabSpeig );

		return tabSpeig;

	}

	public static AnsichtSpalten createAnsichtSpalteMock( final AnsichtDef viewDef, final ITabSpeig tableProp,
			final IAnsichtTab viewTab ) {

		AnsichtSpalten aSpalte = new AnsichtSpalten();
		aSpalte.setAnsichtDef( viewDef );

		// aSpalte.setAnsicht( viewDef.getName() );
		aSpalte.setIndexGrid( 1 );
		aSpalte.setTabAKey( viewDef.getName() );
		aSpalte.setEingabeNotwendig( false );

		aSpalte.setSpalteAKey( tableProp.getSpalte() );
		aSpalte.setName( tableProp.getSpalte() );
		aSpalte.setTabSpEig( tableProp );
		aSpalte.setSystem( false );

		aSpalte.setInGridLaden( true );
		aSpalte.setInGridAnzeigen( true );
		aSpalte.setFilter( true );
		aSpalte.setOfdb( "X" );

		aSpalte.setBearbHinzufZugelassen( true );
		aSpalte.setBearbZugelassen( true );

		aSpalte.setViewTab( viewTab );

		// setDefaultCreationProperties( aSpalte );

		return aSpalte;

	}

	public static AnsichtDef createAnsichtDefMock( final String ansichtName, final BenutzerBereich bereich,
			final boolean setDefaultCredentials ) {

		AnsichtDef ansichtDefMock = new AnsichtDef();
		ansichtDefMock.setName( ansichtName );
		ansichtDefMock.setBearbeiten( Boolean.TRUE );
		ansichtDefMock.setEntfernen( Boolean.TRUE );
		ansichtDefMock.setHinzufuegen( Boolean.TRUE );
		ansichtDefMock.setLesen( Boolean.TRUE );

		ansichtDefMock.setBereich( bereich );
		ansichtDefMock.setOfdb( "X" );
		ansichtDefMock.setSystem( Boolean.FALSE );

		if ( setDefaultCredentials ) {
			ansichtDefMock.setAngelegtAm( new Date() );
			ansichtDefMock.setAngelegtVon( Constants.SYS_USER_DEFAULT );
		}

		// setDefaultCreationProperties( ansichtDefMock );

		return ansichtDefMock;

	}

	public static AnsichtTab createAnsichtTabMock( final AnsichtDef viewDef, final TabDef tabDef, final int reihenfolge,
			final String joinTyp, final String join1SpalteAKey, final String join2SpalteAKey, final String join2TabAKey,
			final boolean setDefaultCredentials ) {

		AnsichtTab viewTab = new AnsichtTab();
		viewTab.setName( viewDef.getName() );
		viewTab.setAnsichtDef( viewDef );
		viewTab.setTabAKey( tabDef.getName() );

		if ( joinTyp.equalsIgnoreCase( "x" ) ) {
			viewTab.setJoinTyp( "x" );
			viewTab.setJoin1SpalteAKey( "x" );
			viewTab.setJoin2SpalteAKey( "x" );
			viewTab.setJoin2TabAKey( "x" );
		} else {
			viewTab.setJoinTyp( "=" );
			viewTab.setJoin1SpalteAKey( join1SpalteAKey );
			viewTab.setJoin2SpalteAKey( join2SpalteAKey );
			viewTab.setJoin2TabAKey( join2TabAKey );
		}
		viewTab.setReihenfolge( reihenfolge );
		viewTab.setOfdb( "X" );
		viewTab.setSystem( Boolean.FALSE );
		viewTab.setTabDef( tabDef );

		if ( setDefaultCredentials ) {
			viewTab.setAngelegtAm( new Date() );
			viewTab.setAngelegtVon( Constants.SYS_USER_DEFAULT );
		}

		// setDefaultCreationProperties( viewTab );

		return viewTab;
	}

	public static BenutzerBereich createBenutzerBereichMock( final String bereichName ) {
		BenutzerBereich bereichMock = new BenutzerBereich();
		bereichMock.setName( bereichName );
		bereichMock.setOfdb( "X" );
		bereichMock.setSystem( Boolean.TRUE );

		setDefaultCreationProperties( bereichMock );

		return bereichMock;
	}

	public static Sequence createSequenceMock( final String sequenceName, final long inkrement,
			final long letzteBelegteNr ) {

		Sequence sequence = new Sequence();
		sequence.setName( sequenceName ); // FX_TabDef_K:DSID
		sequence.setInkrement( inkrement );
		sequence.setLetzteBelegteNr( letzteBelegteNr );
		sequence.setOfdb( "X" );
		sequence.setSystem( Boolean.TRUE );

		setDefaultCreationProperties( sequence );

		return sequence;

	}

	/**
	 * Credentials have to be set in test scope manally if we save per DAO object and not per ofdbService with rulesets
	 *
	 * @param entity
	 */
	private static void setDefaultCreationProperties( final AbstractMWEntity entity ) {

		entity.setAngelegtAm( new Date() );
		entity.setAngelegtVon( Constants.SYS_USER_DEFAULT );

	}

	public static TabDef createTabDefMock( final String tableName, final BenutzerBereich bereich,
			final boolean setDefaultCredentials ) {

		TabDef tabDef = new TabDef();
		tabDef.setName( tableName );
		tabDef.setOfdb( "X" );
		tabDef.setSystem( Boolean.FALSE );
		tabDef.setAlias( tableName );
		tabDef.setBereichsId( bereich.getId() );
		tabDef.setDatenbank( DATENBANK.X );
		tabDef.setEindeutigerSchluessel( "DSID" );

		if ( setDefaultCredentials ) {
			tabDef.setAngelegtAm( new Date() );
			tabDef.setAngelegtVon( Constants.SYS_USER_DEFAULT );
		}

		// setDefaultCreationProperties( tabDef );

		return tabDef;
	}

}
