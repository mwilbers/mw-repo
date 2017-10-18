package de.mw.mwdata.core.ofdb.def;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.mocks.DomainMockFactory;
import de.mw.mwdata.core.ofdb.domain.AnsichtDef;
import de.mw.mwdata.core.ofdb.domain.AnsichtSpalten;
import de.mw.mwdata.core.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.core.ofdb.domain.ITabSpeig.DBTYPE;
import de.mw.mwdata.core.ofdb.domain.TabDef;
import de.mw.mwdata.core.ofdb.domain.TabSpeig;

public class OfdbFieldTest {

	// private BenutzerBereich bereich;
	private TabDef			tabDef;
	private TabSpeig		tabSpeig;
	private AnsichtSpalten	ansichtSpalte;

	@BeforeMethod
	public void setUp() {

		BenutzerBereich b = new BenutzerBereich();
		this.tabDef = DomainMockFactory.createTabDefMock( "TabelleA", b, false );
		this.tabSpeig = DomainMockFactory.createTabSpeigMock( this.tabDef, "SpalteA", 1, DBTYPE.STRING );
		AnsichtDef viewDef = DomainMockFactory.createAnsichtDefMock( this.tabDef.getName(), b, false );
		IAnsichtTab viewTab = DomainMockFactory.createAnsichtTabMock( viewDef, this.tabDef, 0, "x", null, null, null,
				false );
		this.ansichtSpalte = DomainMockFactory.createAnsichtSpalteMock( viewDef, this.tabSpeig, viewTab );

	}

	@Test
	public void testOfdbFieldIsNullable() {

		// 1. eingabeNotwendig
		this.tabSpeig.setEingabeNotwendig( true );
		OfdbField ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertFalse( ofField.isNullable() );

	}

	@Test
	public void testOfdbFieldisEditable() {

		// 2. bearbErlaubt
		this.tabSpeig.setBearbErlaubt( false );
		OfdbField ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertFalse( ofField.isEditable() );

		this.tabSpeig.setBearbErlaubt( true );
		this.ansichtSpalte.setBearbZugelassen( false );
		ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertFalse( ofField.isEditable() );

		this.ansichtSpalte.setBearbZugelassen( true );
		ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertTrue( ofField.isEditable() );

		// 3. systemWert
		this.tabSpeig.setSystemWert( true );
		ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertFalse( ofField.isEditable() );

	}

	@Test
	public void testOfdbFieldIsFilterable() {

		this.ansichtSpalte.setFilter( true );
		OfdbField ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertTrue( ofField.isFilterable() );

	}

	@Test
	public void testOfdbFieldIsVisible() {

		// inGridLaden false
		this.ansichtSpalte.setInGridLaden( false );
		this.ansichtSpalte.setInGridAnzeigen( false );
		OfdbField ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertFalse( ofField.isVisible() );

		// inGridAnzeigen false
		this.ansichtSpalte.setInGridLaden( true );
		this.ansichtSpalte.setInGridAnzeigen( false );
		ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertFalse( ofField.isVisible() );

		this.ansichtSpalte.setInGridAnzeigen( true );
		ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertTrue( ofField.isVisible() );

	}

	@Test
	public void testOfdbFieldIsMapped() {

		OfdbField ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertFalse( ofField.isMapped() );

		ofField.setPropName( "xxx" );
		Assert.assertTrue( ofField.isMapped() );

	}

	@Test
	public void testOfdbFieldCurrency() {

		this.ansichtSpalte.setAnzahlNachkommastellen( 42 );
		OfdbField ofField = new OfdbField( this.tabSpeig, this.ansichtSpalte, CRUD.UPDATE );
		Assert.assertEquals( 42, ofField.getCurrency() );

	}

}
