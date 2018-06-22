package de.mw.mwdata.core.service;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

/**
 * Abstract base class for all usage of application specific configuration
 * settings at initialization time done by property bundles.
 * 
 * @author WilbersM
 *
 */
public abstract class AbstractApplicationConfigService implements ApplicationConfigService {

	private static final String CONFIG_NO_BUNDLE_SET = "%No bundlename provided%"; //$NON-NLS-1$
	private static final String CONFIG_NO_KEY_SET = "%No key provided for bundle%"; //$NON-NLS-1$

	private String bundleName;

	protected abstract ResourceBundle getResourceBundle();

	public AbstractApplicationConfigService(final String bundleName) {
		this.bundleName = bundleName;
	}

	protected String getBundleName() {
		return this.bundleName;
	}

	@Override
	public String getPropertyValue(final String key) {
		return getString(this.bundleName, key);
	}

	protected String getString(final String bundleName, final String key, final Object... arguments) {
		if (StringUtils.isBlank(bundleName)) {
			return CONFIG_NO_BUNDLE_SET;
		}
		if (StringUtils.isBlank(key)) {
			return CONFIG_NO_KEY_SET;
		}
		try {

			final String msg = getResourceBundle().getString(key);
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
