package de.mw.mwdata.core.ofdb.intercept;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.ofdb.def.CRUD;
import de.mw.mwdata.core.ofdb.exception.OfdbInvalidCheckException;

public interface CrudChain {

	// protected abstract boolean hasActionBeforeCheck( final AbstractMWEntity entity );
	//
	// protected abstract void doActionsBeforeCheck( final AbstractMWEntity entity );

	public void setNextChainItem( final CrudChain item );

	// public void doActionsBeforeCheckChain( final AbstractMWEntity entity );

	public void doChainActionsBeforeCheck( final AbstractMWEntity entity, final CRUD crud );

	public void doChainCheck( final AbstractMWEntity entity, final CRUD crud ) throws OfdbInvalidCheckException;

}
