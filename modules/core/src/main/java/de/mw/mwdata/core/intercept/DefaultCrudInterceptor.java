package de.mw.mwdata.core.intercept;

import java.util.Date;

import de.mw.mwdata.core.CRUD;
import de.mw.mwdata.core.Constants;
import de.mw.mwdata.core.domain.AbstractMWEntity;

public class DefaultCrudInterceptor extends AbstractCrudChain {

	// @Override
	public void presetDefaultValues(AbstractMWEntity entity) {

		if (null == entity.getAngelegtAm()) {
			entity.setAngelegtAm(new Date());
		}
		if (null == entity.getAngelegtVon()) {
			entity.setAngelegtVon(Constants.SYS_USER_DEFAULT);
			// FIXME: add user specific informations
		}

	}

	@Override
	public void doChainActionsBeforeCheck(AbstractMWEntity entity, CRUD crud) {

		presetDefaultValues(entity);

	}

	@Override
	public void doChainCheck(AbstractMWEntity entity, CRUD crud) throws InvalidChainCheckException {
		// TODO Auto-generated method stub

	}

}
