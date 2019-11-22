package top.moxingwang.demo.debezium;

import java.util.Enumeration;
import java.util.Properties;

public class PropertyLoader {
    public static void loadEnvironmentValues(Properties properties) {
        Enumeration<?> keys = properties.propertyNames();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String envSafeKey = key.replace(".", "_");
            envSafeKey = envSafeKey.replace("-", "_");
            String envValue = System.getenv(envSafeKey.toUpperCase());
            String systemPropValue = System.getProperty(key.toUpperCase());
            String oldValue = properties.getProperty(key);

            if (envValue != null) {
                properties.setProperty(key, envValue);
            } else if (systemPropValue != null) {
                properties.setProperty(key, systemPropValue);

            }
        }
    }
}
