package de.mw.mwdata.rest.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

public class UrlUtils {

	/**
	 * 
	 * @param url
	 * @param position
	 *            0-based
	 * @return
	 * @throws MalformedURLException
	 */
	public static String getPathToken(final String url, final int position) throws MalformedURLException {

		String path = new URL(url).getPath();
		if (StringUtils.isEmpty(path)) {
			return StringUtils.EMPTY;
		}

		String[] tokens = StringUtils.split(path, "/");
		if (position > tokens.length - 1) {
			return StringUtils.EMPTY;
		} else {
			return tokens[position];
		}

	}

}
