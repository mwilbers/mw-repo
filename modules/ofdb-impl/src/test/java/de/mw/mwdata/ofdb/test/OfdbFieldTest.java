package de.mw.mwdata.ofdb.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabSpeig.DBTYPE;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtSpalten;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.domain.impl.TabSpeig;
import de.mw.mwdata.ofdb.impl.OfdbField;
import de.mw.mwdata.ofdb.mocks.DomainMockFactory;

public class OfdbFieldTest {

	// private BenutzerBereich bereich;
	private TabDef tabDef;
	private TabSpeig tabSpeig;
	private AnsichtSpalten ansichtSpalte;

	@BeforeMethod
	public void setUp() {

		BenutzerBereich b = new BenutzerBereich();
		this.tabDef = DomainMockFactory.createTabDefMock("TabelleA", b, false);
		this.tabSpeig = DomainMockFactory.createTabSpeigMock(this.tabDef, "SpalteA", 1, DBTYPE.STRING);
		AnsichtDef viewDef = DomainMockFactory.createAnsichtDefMock(this.tabDef.getName(), b, false);
		IAnsichtTab viewTab = DomainMockFactory.createAnsichtTabMock(viewDef, this.tabDef, 0, "x", null, null, null,
				false);
		this.ansichtSpalte = DomainMockFactory.createAnsichtSpalteMock(viewDef, this.tabSpeig, viewTab);

	}

	@Test
	public void testOfdbFieldIsNullable() {

		// 1. eingabeNotwendig
		this.tabSpeig.setEingabeNotwendig(true);
		OfdbField ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertFalse(ofField.isNullable());

	}

	@Test
	public void testOfdbFieldisEditable() {

		// 2. bearbErlaubt
		this.tabSpeig.setBearbErlaubt(false);
		OfdbField ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertFalse(ofField.isEditable());

		this.tabSpeig.setBearbErlaubt(true);
		this.ansichtSpalte.setBearbZugelassen(false);
		ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertFalse(ofField.isEditable());

		this.ansichtSpalte.setBearbZugelassen(true);
		ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertTrue(ofField.isEditable());

		// 3. systemWert
		this.tabSpeig.setSystemWert(true);
		ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertFalse(ofField.isEditable());

	}

	@Test
	public void testOfdbFieldIsFilterable() {

		this.ansichtSpalte.setFilter(true);
		OfdbField ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertTrue(ofField.isFilterable());

	}

	@Test
	public void testOfdbFieldIsVisible() {

		// inGridLaden false
		this.ansichtSpalte.setInGridLaden(false);
		this.ansichtSpalte.setInGridAnzeigen(false);
		OfdbField ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertFalse(ofField.isVisible());

		// inGridAnzeigen false
		this.ansichtSpalte.setInGridLaden(true);
		this.ansichtSpalte.setInGridAnzeigen(false);
		ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertFalse(ofField.isVisible());

		this.ansichtSpalte.setInGridAnzeigen(true);
		ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertTrue(ofField.isVisible());

	}

	@Test
	public void testOfdbFieldIsMapped() {

		OfdbField ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertFalse(ofField.isMapped());

		ofField.setPropName("xxx");
		Assert.assertTrue(ofField.isMapped());

	}

	@Test
	public void testOfdbFieldCurrency() {

		this.ansichtSpalte.setAnzahlNachkommastellen(42);
		OfdbField ofField = new OfdbField(this.tabSpeig, this.ansichtSpalte);
		Assert.assertEquals(42, ofField.getCurrency());

	}

}
