package edu.thu.thss.twe.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.MappingException;
import org.hibernate.Session;

import edu.thu.thss.twe.TweContext;
import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.util.HibernateUtility;

public class Configuration {
	private static Configuration config;

	private Map<String, String> properties = new HashMap<String, String>();

	Session session;
	TweContext tweContext;

	public static Configuration getConfiguration() {
		if (config == null) {
			config = configure("/twe.cfg.xml");
		}
		return config;
	}

	private Configuration(Map<String, String> properties) {
		this.properties = properties;
		Map<String, String> hibernateProperties = getHibernateProperties();
		HibernateUtility.configure(hibernateProperties);
		session = HibernateUtility.currentSession();
		tweContext = new TweContext(this, session);
	}

	private Map<String, String> getHibernateProperties() {
		Map<String, String> map = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			if (name.equals("database.driver_class")) {
				map.put("hibernate.connection.driver_class", value);
			} else if (name.equals("database.username")) {
				map.put("hibernate.connection.username", value);
			} else if (name.equals("database.password")) {
				map.put("hibernate.connection.password", value);
			} else if (name.equals("database.url")) {
				map.put("hibernate.connection.url", value);
			} else if (name.equals("database.dialect")) {
				map.put("hibernate.dialect", value);
			}
		}
		return map;
	}

	public TweContext getTweContext() {
		return tweContext;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public String getProperty(String name) {
		return properties.get(name);
	}

	// ///////////////////////
	// private methods
	// ///////////////////////
	private static Configuration configure(String cfgfile) {
		InputStream stream = getResourceAsStream(cfgfile);
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(stream);
			return doConfigure(document);
		} catch (Exception e) {
			throw new TweException("Failed to Convert the configuration file",
					e);
		}
	}

	private static InputStream getResourceAsStream(String resource)
			throws MappingException {
		String stripped = resource.startsWith("/") ? resource.substring(1)
				: resource;

		InputStream stream = null;
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(stripped);
		}
		if (stream == null) {
			throw new TweException(resource + " not found");
		}
		return stream;
	}

	private static Configuration doConfigure(Document doc) {
		Map<String, String> properties = new HashMap<String, String>();
		for (Object o : doc.getRootElement().elements("property")) {
			Element ele = (Element) o;
			String propertyName = ele.attributeValue("name").trim();
			String propertyValue = ele.getTextTrim().trim();
			properties.put(propertyName, propertyValue);
		}
		return new Configuration(properties);
	}

}
