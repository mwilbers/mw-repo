/**
 * 
 */
package de.mw.mwdata.core.utils;

/**
 * @author mwilbers
 * 
 */
public interface ITree<T> /* extends Collection<T> */{

	public boolean isEmpty();

	// /**
	// * This method adds an item on the root layer of the tree.
	// *
	// * @param item
	// * @return
	// */
	// public TreeItem<T> addItem( final T entity );

	//
	// public boolean isEmpty();

	public TreeItem<T> getRootItem();

	// public TreeItem<T> addItem( final T entity );

	// public TreeItem<T> getCurrentItem();
	//
	// public void setCurrentItem( final TreeItem<T> item );

	// public TreeItem<T> createTreeItem( final T entity );

}
