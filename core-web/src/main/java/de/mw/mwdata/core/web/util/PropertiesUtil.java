package de.mw.mwdata.core.web.util;

import java.io.IOException;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import de.mw.mwdata.core.web.Config;

public class PropertiesUtil {

	public static Properties getIconProperties() throws IOException {
		Resource resource = new ClassPathResource( Config.PROPERTIES_PATH_ICONS );
		Properties props = PropertiesLoaderUtils.loadProperties( resource );

		return props;
	}

}
