/**
 * 
 */
package de.mw.mwdata.core.utils;

/**
 * TODO: vgl. org.richfaces.model.TreeNode
 * 
 * @author mwilbers
 * 
 */
public class TreeList<T> implements ITree<T> {

	final Class<T>		type;

	private TreeItem<T>	rootItem;

	public TreeList(final Class<T> type, final T rootEntity) {
		this.type = type;
		this.rootItem = new TreeItem<T>( rootEntity, this.type );
	}

	public TreeItem<T> getRootItem() {
		return this.rootItem;
	}

	// public TreeItem<T> addItem( final T entity ) {
	// return this.getRootItem().addChild( entity );
	// }

	public boolean isEmpty() {
		return !this.rootItem.isHasChildren();
	}

	// public TreeItem<T> createTreeItem( final T entity ) {
	// return new TreeItem<T>( entity, this.type );
	// }

	// public <T> T[] toArray( final T[] a ) {
	// throw new UnsupportedOperationException( "Method toArray not supported by TreeList." );
	// }
	//

}
