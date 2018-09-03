package com.linkingmed.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {

	private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);

	private static Properties props = new Properties();

	// FIXME delete

	public static Properties load(String name) {

		InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream(name + ".properties");
		try {
			props.load(is);
		} catch (IOException e) {
			LOG.error("load resource error .", e);
		} finally {
			IOUtils.closeQuietly(is);
		}
		return props;
	}

	public static String getProperty(Properties props, String key) {

		return props.getProperty(key);
	}
}
