package de.mw.mwdata.core.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mw.mwdata.core.domain.AbstractMWEntity;
import de.mw.mwdata.core.domain.EntityTO;
import de.mw.mwdata.core.domain.IEntity;

public class ClassNameUtils {

	protected static Logger log = LoggerFactory.getLogger(ClassNameUtils.class);

	// from OfdbService: replace all TabDef.getFullClassName()
	public static Class<? extends AbstractMWEntity> getClassType(final String fullQualifiedClassName)
			throws ClassNotFoundException {

		Class<?> class1 = null;
		class1 = Class.forName(fullQualifiedClassName);

		return (Class<? extends AbstractMWEntity>) class1;

	}

	/**
	 * Converts the full qualified classname to the simple classname from last
	 * dot.<br>
	 * Example: com.package.MyClass -> MyClass
	 *
	 * @param fullQualifiedClassName
	 * @return
	 */
	public static String getSimpleClassName(final String fullQualifiedClassName) {

		int lastDotPosition = StringUtils.lastIndexOf(fullQualifiedClassName, '.');
		if (lastDotPosition == -1) {
			return null;
		}

		return fullQualifiedClassName.substring(lastDotPosition + 1);

	}

	public static Class<IEntity> loadClass(final String fullClassName) {
		Class clazz = null;
		try {
			clazz = Class.forName(fullClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clazz;
	}

	public static EntityTO<? extends AbstractMWEntity> createEntityTO(final Class<AbstractMWEntity> clazz) {
		AbstractMWEntity entityTO = null;
		try {
			entityTO = clazz.newInstance();
		} catch (InstantiationException e) {
			// FIXME: do correct error-handling here
			log.error("Could not instantiate filterObject of class: " + clazz.getCanonicalName());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("Could not instantiate filterObject of class: " + clazz.getCanonicalName());
			e.printStackTrace();
		} catch (ClassCastException e) {
			log.error("Could not cast to AbstractMWEntity: " + clazz.getCanonicalName());
			return null;
		}

		EntityTO<AbstractMWEntity> entity = new EntityTO<AbstractMWEntity>(entityTO);
		// entity.setItem( entityTO );
		return entity;

	}

	/**
	 * Converts a full qualified classname to the MWData-based urlPath<br>
	 * Example: de.mw.mwdata.core.MyClassName -> myClassName
	 *
	 * @param className
	 * @return
	 */
	public static String convertClassNameToUrlPath(final AbstractMWEntity entity) {

		String simpleClassName = getSimpleClassName(entity.getClass().getName());
		if (null == simpleClassName) {
			return null;
		}

		return convertToCamelCase(simpleClassName);

	}

	/**
	 * Converts the given className to camelcase version.<br>
	 * Example: MyClass -> myClass
	 *
	 * @param simpleClassName
	 * @return
	 */
	private static String convertToCamelCase(final String simpleClassName) {

		if (null == simpleClassName) {
			return null;
		}

		return StringUtils.uncapitalize(simpleClassName);

	}

}
