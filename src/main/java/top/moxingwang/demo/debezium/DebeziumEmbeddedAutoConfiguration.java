package top.moxingwang.demo.debezium;

import org.apache.kafka.connect.json.JsonConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class DebeziumEmbeddedAutoConfiguration {

    @Bean
    public Properties embeddedProperties() {
        Properties propConfig = new Properties();
        try (InputStream propsInputStream = getClass().getClassLoader().getResourceAsStream("debezium.properties")) {
            propConfig.load(propsInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PropertyLoader.loadEnvironmentValues(propConfig);
        return propConfig;
    }

    @Bean
    public io.debezium.config.Configuration embeddedConfig(Properties embeddedProperties) {
        return io.debezium.config.Configuration.from(embeddedProperties);
    }

    @Bean
    public JsonConverter keyConverter(io.debezium.config.Configuration embeddedConfig) {
        JsonConverter converter = new JsonConverter();
        converter.configure(embeddedConfig.asMap(), true);
        return converter;
    }

    @Bean
    public JsonConverter valueConverter(io.debezium.config.Configuration embeddedConfig) {
        JsonConverter converter = new JsonConverter();
        converter.configure(embeddedConfig.asMap(), false);
        return converter;
    }


}
