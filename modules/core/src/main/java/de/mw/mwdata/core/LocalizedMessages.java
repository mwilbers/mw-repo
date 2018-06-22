package de.mw.mwdata.core;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContext;

public class LocalizedMessages {

	public static final String CONFIG_NO_BUNDLE_SET = "%No bundlename provided%"; //$NON-NLS-1$
	public static final String CONFIG_NO_KEY_SET = "%No key provided for bundle%"; //$NON-NLS-1$

	/**
	 * Protected default constructor to allow sub-classing
	 */
	protected LocalizedMessages() {
		// protected default constructor to allow sub-classing
	}

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
			return CONFIG_NO_BUNDLE_SET;
		}
		if (StringUtils.isBlank(key)) {
			return CONFIG_NO_KEY_SET;
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
			return '%' + key + '%';
		}
	}

}
