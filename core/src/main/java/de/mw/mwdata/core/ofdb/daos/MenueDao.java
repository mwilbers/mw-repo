/**
 *
 */
package de.mw.mwdata.core.ofdb.daos;

import java.util.List;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import de.mw.mwdata.core.daos.GenericDao;
import de.mw.mwdata.core.ofdb.domain.IMenue;
import de.mw.mwdata.core.ofdb.domain.IMenue.MENUETYP;
import de.mw.mwdata.core.ofdb.domain.Menue;
import de.mw.mwdata.core.utils.ITree;
import de.mw.mwdata.core.utils.TreeItem;
import de.mw.mwdata.core.utils.TreeList;

/**
 * @author mwilbers
 *
 */
public class MenueDao extends GenericDao<Menue> implements IMenueDao {

	public MenueDao() {
		super( Menue.class );
	}

	// @Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ITree<IMenue> findMenues() {

		StringBuffer buf = new StringBuffer();
		buf.append( " SELECT menu0, menu1, menu2, menu3, menu4, " );
		buf.append( " 	ansichtDef1, ansichtDef2, ansichtDef3, ansichtDef4 from Menue menu0 " );
		// buf.append( " SELECT menu0, menu0.ansichtDef, menu1, as ansicht from Menue menu0 " );
		buf.append( "   LEFT OUTER JOIN menu0.unterMenues as menu1 " );
		buf.append( "   LEFT OUTER JOIN menu1.unterMenues as menu2 " );
		buf.append( "   LEFT OUTER JOIN menu2.unterMenues as menu3 " );
		buf.append( "   LEFT OUTER JOIN menu3.unterMenues as menu4 " );
		buf.append( "   LEFT JOIN menu1.ansichtDef as ansichtDef1 " );
		buf.append( "   LEFT JOIN menu2.ansichtDef as ansichtDef2 " );
		buf.append( "   LEFT JOIN menu3.ansichtDef as ansichtDef3 " );
		buf.append( "   LEFT JOIN menu4.ansichtDef as ansichtDef4 " );

		buf.append( "  WHERE menu0.ebene = 0 " );
		buf.append( "    AND (menu1.ebene = 1 ) " );
		buf.append( "    AND (menu0.typ = :node ) " );
		buf.append( "    AND (menu0.aktiv = :aktiv ) " );

		buf.append( "    AND (menu2.ebene = 2 or menu2.ebene is null) " );
		buf.append( "    AND (menu3.ebene = 3 or menu3.ebene is null) " );
		buf.append( "    AND (menu4.ebene = 4 or menu4.ebene is null) " );

		buf.append( "  ORDER BY menu0.ofdb asc, menu0.menuId asc, menu1.ofdb asc, menu1.menuId asc " );
		buf.append( " , menu1.ofdb asc, menu1.menuId asc " );
		buf.append( " , menu2.ofdb asc, menu2.menuId asc " );
		buf.append( " , menu3.ofdb asc, menu3.menuId asc " );

		Query query = this.getCurrentSession().createQuery( buf.toString() );
		query.setParameter( "node", MENUETYP.KNOTEN );
		query.setParameter( "aktiv", Boolean.TRUE );
		// query.setParameter( "ebene1", ebene + 1 );

		List<Object[]> items = query.list();
		return createMenuTree( items );

		// return tree;
	}

	private ITree<IMenue> createMenuTree( final List<Object[]> items ) {

		ITree<IMenue> tree = new TreeList( IMenue.class, new Menue() );

		IMenue lastMenu0 = null;
		IMenue lastMenu1 = null;
		IMenue lastMenu2 = null;
		IMenue lastMenu3 = null;

		TreeItem<IMenue> item0 = null;
		TreeItem<IMenue> item1 = null;
		TreeItem<IMenue> item2 = null;
		TreeItem<IMenue> item3 = null;
		TreeItem<IMenue> item4 = null;

		for ( Object[] row : items ) {

			IMenue menu0 = (IMenue) row[0];
			IMenue menu1 = getRowItemIfExists( row, 1 );
			IMenue menu2 = getRowItemIfExists( row, 2 );
			IMenue menu3 = getRowItemIfExists( row, 3 );
			IMenue menu4 = getRowItemIfExists( row, 4 );

			// ebene 0
			if ( menu0.equals( lastMenu0 ) ) {

				// ebene 1
				if ( null != menu1 && null != lastMenu1 ) {
					if ( menu1.equals( lastMenu1 ) ) {

						// ebene 2
						if ( null != menu2 && null != lastMenu2 ) {
							if ( menu2.equals( lastMenu2 ) ) {

								// ebene 3
								if ( null != menu3 && null != lastMenu3 ) {
									if ( menu3.equals( lastMenu3 ) ) {

										// ebene 4

									} else {
										item3 = item2.addChild( menu3 );
										item4 = addChildIfExists( menu4, item3 );
									}
								}

							} else { // ebene 2
								item2 = item1.addChild( menu2 );
								item3 = addChildIfExists( menu3, item2 );
								item4 = addChildIfExists( menu4, item3 );

							}
						}

					} else { // ebene 1
						item1 = item0.addChild( menu1 );
						item2 = addChildIfExists( menu2, item1 );
						item3 = addChildIfExists( menu3, item2 );
						item4 = addChildIfExists( menu4, item3 );

					}
				}

			} else { // ebene 0
				item0 = tree.getRootItem().addChild( menu0 );
				item1 = addChildIfExists( menu1, item0 );
				item2 = addChildIfExists( menu2, item1 );
				item3 = addChildIfExists( menu3, item2 );
				item4 = addChildIfExists( menu4, item3 );

			}

			lastMenu0 = menu0;
			lastMenu1 = menu1;
			lastMenu2 = menu2;
			lastMenu3 = menu3;
		}

		return tree;

	}

	private IMenue getRowItemIfExists( final Object[] row, final int rowIndex ) {

		if ( row.length >= rowIndex + 1 ) {
			return (IMenue) row[rowIndex];
		}
		return null;

	}

	private TreeItem<IMenue> addChildIfExists( final IMenue menuChild, final TreeItem<IMenue> mainItem ) {
		if ( null != menuChild ) {
			return mainItem.addChild( menuChild );
		}
		return null;
	}

}
