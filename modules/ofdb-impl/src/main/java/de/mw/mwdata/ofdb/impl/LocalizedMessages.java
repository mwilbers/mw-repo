package de.mw.mwdata.ofdb.impl;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

public class LocalizedMessages {

	/**
	 * Protected default constructor to allow sub-classing
	 */
	protected LocalizedMessages() {
		// protected default constructor to allow sub-classing
	}

	// /**
	// * Resolves the String for the passed key in the context of the user
	// locale.
	// * <p>
	// * Call is equivalent to
	// * <code>getString(bundleName, key, false, EMPTY_ARGS)</code>.
	// *
	// * @param bundleName
	// * @param key
	// * @return
	// * @see #getString(String, String, boolean, Object...)
	// */
	// public static String getString( final String bundleName, final String key
	// ) {
	// return getString( bundleName, key, false );
	// }

	// /**
	// * Resolves the String for the passed key in the context of the user
	// locale.
	// * The variable list of arguments is parsed into the resulting string via
	// * MessageFormat.
	// * <p>
	// * Call is equivalent to
	// * <code>getString(bundleName, key, false, arguments)</code>.
	// *
	// * @param bundleName
	// * @param key
	// * @param arguments
	// * @return
	// * @see MessageFormat
	// * @see #getString(String, String, boolean, Object...)
	// */
	// public static String getString( final String bundleName, final String
	// key, final Object... arguments ) {
	// return getString( bundleName, key, false, arguments );
	// }

	// /**
	// * Resolves the String for the passed key in the context of the user
	// locale.
	// *
	// * @param bundleName
	// * @param key
	// * @param nullIfNotExist
	// * if true <code>null</code> is returned if no text for the given
	// * key could be found
	// * @return
	// *
	// * @see #getString(String, String, boolean, Object...)
	// */
	// public static String getString( final String bundleName, final String
	// key, final boolean nullIfNotExist ) {
	// return getString( bundleName, key, nullIfNotExist, EMPTY_ARGS );
	// }

	/**
	 * Resolves the String for the passed key in the context of the user locale.
	 *
	 * @param bundleName
	 *            has to be a resolvable bundlename
	 * @param key
	 *            a non blank string
	 * @param nullIfNotExist
	 *            returns null if no message was found for given key
	 * @param arguments
	 *            arguments to be parsed into the resulting string
	 * @return
	 * @see MessageFormat
	 * @see ResourceBundle
	 * @see LocaleContext
	 */
	public static String getString(final String bundleName, final String key, final Object... arguments) {
		if (StringUtils.isBlank(bundleName)) {
			return "%No bundlename provided%";
		}
		if (StringUtils.isBlank(key)) {
			return "%No key provided for bundle%";
		}
		try {
			final Locale locale = Locale.getDefault();
			final String msg = ResourceBundle.getBundle(bundleName, locale).getString(key);
			if ((arguments == null) || (arguments.length == 0)) {
				return msg;
			} else {
				return new MessageFormat(msg != null ? msg : StringUtils.EMPTY, Locale.getDefault()).format(arguments);
			}

		} catch (final MissingResourceException e) {
			// if ( nullIfNotExist ) {
			// return null;
			// } else {
			return '%' + key + '%';
			// }
		}
	}

}
