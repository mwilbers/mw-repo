package de.mw.mwdata.ofdb.service.test;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.query.QueryResult;
import de.mw.mwdata.core.test.data.TestConstants;
import de.mw.mwdata.ofdb.domain.IAnsichtTab;
import de.mw.mwdata.ofdb.domain.IMenue;
import de.mw.mwdata.ofdb.domain.IMenue.MENUETYP;
import de.mw.mwdata.ofdb.domain.impl.Menue;
import de.mw.mwdata.ofdb.domain.impl.TabDef;
import de.mw.mwdata.ofdb.service.IMenuService;
import de.mw.mwdata.ofdb.test.AbstractOfdbInitializationTest;

@Test(enabled = true)
@ContextConfiguration(locations = { "classpath:/appContext-ofdb.xml", "classpath:/appContext-ofdb-test.xml",
		"classpath:/appContext-common-test-db.xml" })
public class OfdbMenuServiceTest extends AbstractOfdbInitializationTest {

	@Autowired
	private IMenuService ofdbMenuService;

	@BeforeMethod
	public void prepareOfdb() {

		this.applicationFactory.configure();
		IAnsichtTab ansichtTab = this.setUpAnsichtAndTab(TestConstants.TABLENAME_TABDEF, "tabDef", TabDef.class);
		saveForTest(ansichtTab.getTabDef());

		ansichtTab = this.setUpAnsichtAndTab(TestConstants.TABLENAME_MENU, "menu", Menue.class);
		saveForTest(ansichtTab.getTabDef());

		Menue parentMenu = new Menue();
		parentMenu.setMenuId(42l);
		parentMenu.setName("ParentMenu");
		parentMenu.setAnzeigeName("ParentMenu");
		parentMenu.setTyp(MENUETYP.KNOTEN);
		parentMenu.setEbene(1);
		parentMenu.setBereich(this.getTestBereich());
		parentMenu.setAngelegtAm(new Date());
		parentMenu.setAngelegtVon(Constants.SYS_USER_DEFAULT);
		parentMenu.setSystem(false);
		this.saveForTest(parentMenu);

		Menue childMenu = new Menue();
		childMenu.setMenuId(43l);
		childMenu.setName("ChildMenu");
		childMenu.setAnzeigeName("ChildMenu");
		childMenu.setTyp(MENUETYP.ANSICHT);
		childMenu.setEbene(2);
		childMenu.setBereich(this.getTestBereich());
		childMenu.setHauptMenue(parentMenu);
		childMenu.setAngelegtAm(new Date());
		childMenu.setAngelegtVon(Constants.SYS_USER_DEFAULT);
		childMenu.setSystem(false);
		this.saveForTest(childMenu);

		this.applicationFactory.init();

	}

	@Test
	public void testFindChildMenus() {

		IMenue parentMenu = this.ofdbMenuService.findMenuById(1l);
		QueryResult childMenuResult = this.ofdbMenuService.findChildMenus(parentMenu, "Administrator");
		Assert.assertNotNull(childMenuResult);

	}

}
