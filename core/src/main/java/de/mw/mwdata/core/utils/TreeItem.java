/**
 * 
 */
package de.mw.mwdata.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * TODO: vgl. org.richfaces.model.TreeNode
 * 
 * @author mwilbers
 * 
 */
public class TreeItem<T> implements Collection<TreeItem<T>> {

	private List<TreeItem<T>>	children	= new ArrayList<TreeItem<T>>();

	private TreeItem<T>			parent;

	private T					entity;

	final Class<? extends T>	type;

	public TreeItem(final T entity, final Class<? extends T> type) {
		this.entity = entity;
		this.type = type;
	}

	public boolean isRoot() {
		return (null == this.parent);
	}

	public boolean isHasChildren() {
		return (this.children.size() != 0);
	}

	// TODO: vgl. org.richfaces.model.TreeNode:
	// dort: public Iterator<Map.Entry<Object, TreeNode<T>>> getChildren();
	public List<TreeItem<T>> getChildren() {
		return this.children;
	}

	public TreeItem<T> addChild( final T childEntity ) {

		TreeItem<T> childItem = new TreeItem<T>( childEntity, this.type );
		childItem.setParent( this );
		this.children.add( childItem );
		return childItem;

	}

	public void setParent( final TreeItem<T> parent ) {
		this.parent = parent;
	}

	public TreeItem<T> getParent() {
		return this.parent;
	}

	public T getEntity() {
		return this.entity;
	}

	// ....................... collection-methods .........................

	public int size() {
		return this.getChildren().size();
	}

	public boolean isEmpty() {
		return this.getChildren().isEmpty();
	}

	public boolean contains( final Object o ) {
		return this.getChildren().contains( o );
	}

	public Iterator<TreeItem<T>> iterator() {
		return this.getChildren().iterator();
	}

	public Object[] toArray() {
		return this.getChildren().toArray();
	}

	public <T> T[] toArray( final T[] a ) {
		return this.getChildren().toArray( a );
	}

	public boolean add( final TreeItem<T> e ) {
		return this.getChildren().add( e );
	}

	public boolean remove( final Object o ) {
		return this.getChildren().remove( o );
	}

	public boolean containsAll( final Collection<?> c ) {
		return this.getChildren().containsAll( c );
	}

	public boolean addAll( final Collection<? extends TreeItem<T>> c ) {
		return this.getChildren().addAll( c );
	}

	public boolean removeAll( final Collection<?> c ) {
		return this.getChildren().removeAll( c );
	}

	public boolean retainAll( final Collection<?> c ) {
		return this.getChildren().retainAll( c );
	}

	public void clear() {
		this.getChildren().clear();
	}

}
