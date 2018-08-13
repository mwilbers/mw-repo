package de.mw.mwdata.core.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

/**
 * An {@link EntityTO} is an TO-object containing all data and informations of
 * the primary underlying view and additional data stored in a key-value-map
 * from joined secondary views by ofdb-configuration<br>
 * FIXME: EntityTO should have generic type IEntity for casting implicit types
 * to IMenue instead of Menue etc. -> MenuController ...
 *
 * @author mwilbers
 *
 * @param <E>
 */
public class EntityTO<E extends AbstractMWEntity> {

	// http://stackoverflow.com/questions/736186/can-a-spring-form-command-be-a-map?rq=1

	private E entity;

	@SuppressWarnings("unchecked")
	private Map<String, JoinedValue> map;

	/**
	 * Internal class for managing all joined table column values by key/value -
	 * pairs from ofdb queries<br>
	 * E.g. 'select TabDef as tDef, b.name as bName from TabDef , tDef.bereiche as b
	 * ...'
	 *
	 * @author WilbersM
	 *
	 */
	public class JoinedValue {

		private String value;

		public JoinedValue(final String value) {
			this.value = value;
		}

		public JoinedValue() {

		}

		private String getValue() {
			return this.value;
		}

		@Override
		public String toString() {
			return this.value;
		}

	}

	public EntityTO(final E entity) {
		this.entity = entity;
		this.map = new HashMap<>();
	}

	private EntityTO() {
		this.map = new HashMap<>();
	}

	public static EntityTO<AbstractMWEntity> createEmptyEntityTO() {
		return new EntityTO<>();
	}

	public boolean isEmpty() {
		return null == this.entity;
	}

	public E getItem() {
		return this.entity;
	}

	public String getJoinedValue(final String mapKey) {

		if (null == this.map.get(mapKey)) {
			return StringUtils.EMPTY;
		}

		return this.map.get(mapKey).getValue();
	}

	public boolean hasJoinedValues() {
		for (Entry<String, JoinedValue> entry : this.map.entrySet()) {
			if (!StringUtils.isBlank(entry.getValue().getValue())) {
				return true;
			}
		}

		return false;
	}

	public void addJoinedValue(final String mapKey, final String mapValue) {
		this.map.put(mapKey, new JoinedValue(mapValue));
	}

}
