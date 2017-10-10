package de.mw.mwdata.core.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.MapUtils;
import de.mw.mwdata.core.ofdb.MapValue;

/**
 * An {@link EntityTO} is an TO-object containing all data and informations of the primary underlying view and
 * additional data stored in a key-value-map from joined secondary views by ofdb-configuration
 *
 * @author mwilbers
 *
 * @param <E>
 */
public class EntityTO<E extends AbstractMWEntity> implements Serializable {

	// http://stackoverflow.com/questions/736186/can-a-spring-form-command-be-a-map?rq=1

	/**
	 *
	 */
	private static final long	serialVersionUID	= -4450059813957334370L;

	private E					item;

	public EntityTO(final E item) {
		this.item = item;
	}

	@SuppressWarnings("unchecked")
	private Map<String, MapValue> map = MapUtils.lazyMap( new HashMap<String, MapValue>(),
			FactoryUtils.instantiateFactory( MapValue.class ) );

	// public void setItem( final E entity ) {
	// this.item = entity;
	// }

	public E getItem() {
		return this.item;
	}

	public void setSuchWert( final String key, final MapValue value ) {
		this.map.put( key, value );
	}

	public MapValue getSuchWert( final String key ) {
		return this.map.get( key );
	}

	public Map<String, MapValue> getMap() {
		return this.map;
	}

	public void setMap( final Map<String, MapValue> map ) {
		this.map = map;
	}

	// ... nun in listEntity.jsp den verweis auf map['ofProp.propName'].value oder so ähnlich

}
