package de.mw.mwdata.core.domain;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import de.mw.mwdata.core.query.InvalidQueryConfigurationException;

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
	private List<JoinedValue> joinedValues;

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
		this.joinedValues = new ArrayList<>();
	}

	private EntityTO() {
		this.joinedValues = new ArrayList<>();
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

	public void addJoinedValue(final String value) {
		this.joinedValues.add(new JoinedValue(value));
	}

	/**
	 * 
	 * @param joinedValuesIndex
	 * @return the value of all joined properties given by the 1-based index
	 */
	public String getJoinedValue(final int joinedValuesIndex) {

		if (joinedValuesIndex > this.joinedValues.size()) {
			String msg = MessageFormat.format(
					"Called invalid joined property of configured entity based view for index {0}", joinedValuesIndex);
			throw new InvalidQueryConfigurationException(msg);
		}

		return this.joinedValues.get(joinedValuesIndex - 1).getValue();
	}

}
