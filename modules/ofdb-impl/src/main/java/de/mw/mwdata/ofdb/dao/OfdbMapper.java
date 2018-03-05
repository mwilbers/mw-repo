package de.mw.mwdata.ofdb.dao;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import javax.persistence.ManyToMany;

import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.CustomType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import de.mw.mwdata.core.db.FxBooleanType;
import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.FxEnumType;
import de.mw.mwdata.ofdb.domain.ITabSpeig.DBTYPE;
import de.mw.mwdata.ofdb.exception.OfdbRuntimeException;
import de.mw.mwdata.ofdb.impl.OfdbEntityMapping;

public class OfdbMapper extends HibernateDaoSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(OfdbMapper.class);

	public OfdbMapper() {
	}

	/**
	 * Method is called when instantiating the spring-defined DAO-Beans in
	 * appContext-ofdb.xml
	 */
	public OfdbEntityMapping init(final Class<? extends AbstractMWEntity> type, final String tableName) {

		AbstractEntityPersister persister = getEntityPersister(type);
		ClassMetadata meta = this.getSessionFactory().getClassMetadata(type);
		// NOTE: the property with the @Id-Annotation stays undocumented here
		// this.props = meta.getPropertyNames();

		OfdbEntityMapping entityMapping = buildNameToIndexMapping(tableName, persister, meta.getPropertyNames());

		String[] keyColumnNames = persister.getKeyColumnNames();
		for (int i = 0; i < keyColumnNames.length; i++) {
			// OfdbPropMapper mapper = new OfdbPropMapper(tableName, keyColumnNames[i]);
			// mapper.setPropertyName(persister.getIdentifierPropertyName());
			// mapper.setDbType(DBTYPE.LONGINTEGER);

			// propMap.put(keyColumnNames[i], mapper);
			entityMapping.addMapping(keyColumnNames[i], persister.getIdentifierPropertyName(), 0, DBTYPE.LONGINTEGER);
		}

		return entityMapping;
	}

	/**
	 * Method builds a map in memory with key = columnName from DB and value = index
	 * of property in hibernate-persister-class for optimizing performance.<br>
	 * NOTE: The map does not contain the mapping of the primary key - column !
	 *
	 * @param props
	 */
	private OfdbEntityMapping buildNameToIndexMapping(final String tableName, final AbstractEntityPersister persister,
			final String[] props) {

		OfdbEntityMapping entityMapping = new OfdbEntityMapping(tableName);
		// Map<String, OfdbPropMapper> propMap = new HashMap<String, OfdbPropMapper>();

		for (int i = 0; i < props.length; i++) {

			if (persister.getPropertyColumnNames(i).length > 0 && !isAssociatedCollection(props[i])) {

				Type[] propertyTypes = persister.getPropertyTypes();

				if (propertyTypes[i] instanceof ManyToOneType) {
					continue; // we imply for manytoone-types there is an additional id property,
					// e.g.: for TabDef.Bereich there is TabDef.BereichsId
				}

				String columnNameDb = persister.getPropertyColumnNames(i)[0].toUpperCase();
				// OfdbPropMapper propMapper = new OfdbPropMapper(tableName, columnNameDb);
				// propMapper.setPropertyName(props[i]);
				// propMapper.setPropertyIndex(new Integer(i));

				DBTYPE dbType = convertTypeToDbType(propertyTypes[i]);
				// propMapper.setDbType(dbType);
				entityMapping.addMapping(columnNameDb, props[i], new Integer(i), dbType);

				// propMap.put(columnNameDb, propMapper);

			}
		}

		return entityMapping;
	}

	private DBTYPE convertTypeToDbType(final Type type) {

		if (type instanceof TimestampType) {

			return DBTYPE.DATE;

		} else if (type instanceof CustomType) {

			CustomType customType = (CustomType) type;
			if (customType.getUserType() instanceof FxBooleanType) {
				return DBTYPE.BOOLEAN;
			} else if (customType.getUserType() instanceof FxEnumType<?>) {
				return DBTYPE.ENUM;
			}

		} else if (type instanceof StringType) {
			return DBTYPE.STRING;
		} else if (type instanceof IntegerType) {
			return DBTYPE.LONGINTEGER;
		} else if (type instanceof LongType) {
			return DBTYPE.LONGINTEGER;
		} else if (isAssociationType(type)) {
			return DBTYPE.ENTITY;
		} else {
			throw new UnsupportedOperationException(
					"org.hibernate.type not supported by MWData: " + type.getClass().getName());
		}

		return null;

	}

	private boolean isAssociationType(final Type propType) {
		if (propType instanceof ManyToOneType) {
			return true;
		}
		if (propType instanceof javax.persistence.OneToMany) {
			return true;
		}
		if (propType instanceof javax.persistence.OneToOne) {
			return true;
		}
		if (propType instanceof ManyToMany) {
			return true;
		}

		return false;
	}

	private boolean isAssociatedCollection(final String propName) {
		boolean association = propName.startsWith("_");
		if (association) {
			LOGGER.error("Found invalid association-propName: " + propName);
		}
		return association;
	}

	private AbstractEntityPersister getEntityPersister(final Class<? extends AbstractMWEntity> entityClassType) {

		AbstractEntityPersister persister = null;
		ClassMetadata meta = this.getHibernateTemplate().getSessionFactory().getClassMetadata(entityClassType);
		if (meta instanceof AbstractEntityPersister) {
			persister = (AbstractEntityPersister) meta;

		} else {
			String msg = MessageFormat.format("Class {0} not mapped.", entityClassType.getName());
			LOGGER.error(msg);
			throw new OfdbRuntimeException(msg);
			// return null;
		}

		return persister;

	}

	public List<Object> getEnumValues(final Class<? extends AbstractMWEntity> entityClassType, final String propName) {

		// FIXME: enum constants could be also loaded in initializeMapper()

		AbstractEntityPersister persister = getEntityPersister(entityClassType);
		Type t = persister.getPropertyType(propName);
		Class<?> c = t.getReturnedClass();
		List<Object> ox = Arrays.asList((Object[]) c.getEnumConstants());

		return ox;
	}

	// TODO: wahnsinn ! warum geht das ?
	public Class<Enum> getEnumType(final String propName, final Class<? extends AbstractMWEntity> entityClassType) {

		AbstractEntityPersister persister = getEntityPersister(entityClassType);
		Type t = persister.getPropertyType(propName);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Class<Enum> c = t.getReturnedClass();
		return c;
	}

	public Object getPropertyValue(final AbstractMWEntity entity, final int index) {

		AbstractEntityPersister persister = getEntityPersister(entity.getClass());
		return persister.getPropertyValue(entity, index);
	}

	public void setPropertyValue(final AbstractMWEntity entity, final int index, final Object value,
			final Class<? extends AbstractMWEntity> entityClassType) {

		AbstractEntityPersister persister = getEntityPersister(entity.getClass());
		persister.setPropertyValue(entity, index, value);
	}

}
