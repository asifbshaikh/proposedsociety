package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Play;

import com.typesafe.config.ConfigUtil;

public final class ConfigUtils {
	private static final Logger LOG = LoggerFactory.getLogger(ConfigUtil.class);
	
	private ConfigUtils() {
	
	}

	public static String getConfigString(String property, boolean required) {
	    String value = Play.application().configuration().getString(property);
		if (!required) {
			return value == null ? null : value.trim();
		}
	
		if (value == null || value.trim().length() == 0) {
			throw new RuntimeException("Could not read mandatory configuration property: " + property);
		}
		
		LOG.info("value Class: " + value.getClass().getName() + " value: " + value);
		
		return value.trim();
	} 
}
