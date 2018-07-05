package de.mw.mwdata.rest.uimodel.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.domain.DBTYPE;
import de.mw.mwdata.core.to.OfdbField;
import de.mw.mwdata.ofdb.domain.IAnsichtSpalte;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.ITabDef;
import de.mw.mwdata.ofdb.domain.ITabSpeig;
import de.mw.mwdata.ofdb.domain.impl.AnsichtDef;
import de.mw.mwdata.ofdb.domain.impl.AnsichtSpalten;
import de.mw.mwdata.ofdb.domain.impl.TabSpeig;
import de.mw.mwdata.ofdb.impl.OfdbUtils;
import de.mw.mwdata.ofdb.mocks.DomainMockFactory;
import de.mw.mwdata.rest.uimodel.UiInputConfig;

public class OfdbFieldTest {

	// private BenutzerBereich bereich;
	private ITabDef tabDef;
	private ITabSpeig tabSpeig;
	private IAnsichtSpalte ansichtSpalte;

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
		TabSpeig tabSpeigImpl = (TabSpeig) this.tabSpeig;
		tabSpeigImpl.setEingabeNotwendig(true);
		OfdbField ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		UiInputConfig uiItemConfig = new UiInputConfig(ofField);
		Assert.assertFalse(uiItemConfig.isNullable());

	}

	@Test
	public void testOfdbFieldisEditable() {

		// 2. bearbErlaubt
		TabSpeig tabSpeigImpl = (TabSpeig) this.tabSpeig;
		AnsichtSpalten viewColumn = (AnsichtSpalten) this.ansichtSpalte;
		tabSpeigImpl.setBearbErlaubt(false);
		OfdbField ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		UiInputConfig uiItemConfig = new UiInputConfig(ofField);
		Assert.assertFalse(uiItemConfig.isEditable());

		tabSpeigImpl.setBearbErlaubt(true);
		viewColumn.setBearbZugelassen(false);
		ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		uiItemConfig = new UiInputConfig(ofField);
		Assert.assertFalse(uiItemConfig.isEditable());

		viewColumn.setBearbZugelassen(true);
		ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		uiItemConfig = new UiInputConfig(ofField);
		Assert.assertTrue(uiItemConfig.isEditable());

		// 3. systemWert
		tabSpeigImpl.setSystemWert(true);
		ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		uiItemConfig = new UiInputConfig(ofField);
		Assert.assertFalse(uiItemConfig.isEditable());

	}

	@Test
	public void testOfdbFieldIsFilterable() {

		AnsichtSpalten viewColumn = (AnsichtSpalten) this.ansichtSpalte;
		viewColumn.setFilter(true);
		OfdbField ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		UiInputConfig uiItemConfig = new UiInputConfig(ofField);
		Assert.assertTrue(uiItemConfig.isFilterable());

	}

	@Test
	public void testOfdbFieldIsVisible() {

		AnsichtSpalten viewColumn = (AnsichtSpalten) this.ansichtSpalte;

		// inGridLaden false
		viewColumn.setInGridLaden(false);
		viewColumn.setInGridAnzeigen(false);
		OfdbField ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		UiInputConfig uiItemConfig = new UiInputConfig(ofField);
		Assert.assertFalse(uiItemConfig.isVisible());

		// inGridAnzeigen false
		viewColumn.setInGridLaden(true);
		viewColumn.setInGridAnzeigen(false);
		ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		uiItemConfig = new UiInputConfig(ofField);
		Assert.assertFalse(uiItemConfig.isVisible());

		viewColumn.setInGridAnzeigen(true);
		ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		uiItemConfig = new UiInputConfig(ofField);
		Assert.assertTrue(uiItemConfig.isVisible());

	}

	@Test
	public void testOfdbFieldIsMapped() {

		OfdbField ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		UiInputConfig uiItemConfig = new UiInputConfig(ofField);
		Assert.assertFalse(uiItemConfig.isMapped());

		uiItemConfig.setPropName("xxx");
		Assert.assertTrue(uiItemConfig.isMapped());

	}

	@Test
	public void testOfdbFieldCurrency() {

		AnsichtSpalten viewColumn = (AnsichtSpalten) this.ansichtSpalte;

		viewColumn.setAnzahlNachkommastellen(42);
		OfdbField ofField = OfdbUtils.createOfdbField(this.tabSpeig, this.ansichtSpalte);
		UiInputConfig uiItemConfig = new UiInputConfig(ofField);
		Assert.assertEquals(42, uiItemConfig.getCurrency());

	}

}
