/**
 * 
 */
package de.mw.mwdata.core.ofdb.daos;

import de.mw.mwdata.core.daos.IGenericDao;
import de.mw.mwdata.core.ofdb.domain.Menue;
import de.mw.mwdata.core.utils.ITree;

/**
 * @author mwilbers
 * 
 */
public interface IMenueDao extends IGenericDao<Menue> {

	/**
	 * This method returns all menu-items in a tree-structure. Four tree-layers are provided.
	 * 
	 * 
	 * @param ebene
	 * @return
	 */
	public ITree findMenues();

}
