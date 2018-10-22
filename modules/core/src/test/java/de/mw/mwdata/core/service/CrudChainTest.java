package de.mw.mwdata.core.service;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.mw.mwdata.core.CRUD;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.BenutzerBereich;
import de.mw.mwdata.core.intercept.AbstractCrudChain;
import de.mw.mwdata.core.intercept.CrudChain;
import de.mw.mwdata.core.intercept.ICrudInterceptable;
import de.mw.mwdata.core.intercept.InvalidChainCheckException;
import de.mw.mwdata.core.mocks.CoreMockFactory;

public class CrudChainTest {

	@Test
	public void testCrudChain() {

		ICrudInterceptable crudService = new CrudService<>();
		BenutzerBereich testBereich = CoreMockFactory.createBenutzerBereichMock("Testbereich");

		// 1. test empty crud chain list
		crudService = new CrudService<>();
		List<CrudChain> chainItems = new ArrayList<>();
		crudService.setCrudInterceptors(chainItems);
		crudService.doActionsBeforeCheck(testBereich, CRUD.UPDATE);
		Assert.assertEquals(testBereich.getName(), "Testbereich");

		// 2. test with one crud chain item
		crudService = new CrudService<>();
		testBereich.setName("");
		chainItems.clear();
		chainItems.add(new SetNameTestbereichCrudChain("1"));

		crudService.setCrudInterceptors(chainItems);
		crudService.doActionsBeforeCheck(testBereich, CRUD.UPDATE);
		Assert.assertEquals(testBereich.getName(), "1");

		// 3. test with two crud chain items
		crudService = new CrudService<>();
		testBereich.setName("");
		chainItems.clear();
		chainItems.add(new SetNameTestbereichCrudChain("1"));
		chainItems.add(new SetNameTestbereichCrudChain("2"));

		crudService.setCrudInterceptors(chainItems);
		crudService.doActionsBeforeCheck(testBereich, CRUD.UPDATE);
		Assert.assertEquals(testBereich.getName(), "12"); // first in first wins

		// 4. test with additional registered chain item at the end of the chain and no
		// default item
		crudService = new CrudService<>();
		testBereich.setName("");
		chainItems.clear();

		crudService.setCrudInterceptors(chainItems);
		crudService.registerCrudInterceptor(new SetNameTestbereichCrudChain("1"), -1);
		crudService.doActionsBeforeCheck(testBereich, CRUD.UPDATE);
		Assert.assertEquals(testBereich.getName(), "1");

		// 5. test with one default chain item and additional registered item at the
		// start of the chain
		crudService = new CrudService<>();
		testBereich.setName("");
		chainItems.clear();
		chainItems.add(new SetNameTestbereichCrudChain("1"));
		crudService.setCrudInterceptors(chainItems);
		crudService.registerCrudInterceptor(new SetNameTestbereichCrudChain("2"), 0);
		crudService.doActionsBeforeCheck(testBereich, CRUD.UPDATE);
		Assert.assertEquals(testBereich.getName(), "21");

		// 6. test with one default chain item and additional registered item
		crudService = new CrudService<>();
		testBereich.setName("");
		chainItems.clear();
		chainItems.add(new SetNameTestbereichCrudChain("1"));
		chainItems.add(new SetNameTestbereichCrudChain("2"));
		chainItems.add(new SetNameTestbereichCrudChain("3"));
		chainItems.add(new SetNameTestbereichCrudChain("4"));
		crudService.setCrudInterceptors(chainItems);
		crudService.registerCrudInterceptor(new SetNameTestbereichCrudChain("5"), 1);
		crudService.doActionsBeforeCheck(testBereich, CRUD.UPDATE);
		Assert.assertEquals(testBereich.getName(), "15234");

		// 7. test with one default chain item and additional registered item at the end
		// of the chain
		crudService = new CrudService<>();
		testBereich.setName("");
		chainItems.clear();
		chainItems.add(new SetNameTestbereichCrudChain("1"));
		chainItems.add(new SetNameTestbereichCrudChain("2"));
		chainItems.add(new SetNameTestbereichCrudChain("3"));
		chainItems.add(new SetNameTestbereichCrudChain("4"));
		crudService.setCrudInterceptors(chainItems);
		crudService.registerCrudInterceptor(new SetNameTestbereichCrudChain("5"), 42);
		crudService.doActionsBeforeCheck(testBereich, CRUD.UPDATE);
		Assert.assertEquals(testBereich.getName(), "12345");

	}

	private class SetNameTestbereichCrudChain extends AbstractCrudChain {

		private String nameToken;

		SetNameTestbereichCrudChain(final String nameToken) {
			this.nameToken = nameToken;
		}

		@Override
		public void doChainActionsBeforeCheck(AbstractMWEntity entity, CRUD crud) {
			// if (null == entity.getName()) {
			entity.setName(entity.getName() + this.nameToken);
			// } else {
			CrudChain nextItem = this.getNextChainItem();
			if (null != nextItem) {
				nextItem.doChainActionsBeforeCheck(entity, crud);
			}
			// }

		}

		@Override
		public void doChainCheck(AbstractMWEntity entity, CRUD crud) throws InvalidChainCheckException {
			// TODO Auto-generated method stub

		}

	}

	// private class SetNameTestbereich3CrudChain extends AbstractCrudChain {
	//
	// @Override
	// public void doChainActionsBeforeCheck(AbstractMWEntity entity, CRUD crud) {
	// if (null == entity.getName()) {
	// entity.setName("Testbereich3");
	// } else {
	// CrudChain nextItem = this.getNextChainItem();
	// if (null != nextItem) {
	// nextItem.doChainActionsBeforeCheck(entity, crud);
	// }
	// }
	//
	// }
	//
	// @Override
	// public void doChainCheck(AbstractMWEntity entity, CRUD crud) throws
	// InvalidChainCheckException {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// }

}
