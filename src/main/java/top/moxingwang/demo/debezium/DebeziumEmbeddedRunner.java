package top.moxingwang.demo.debezium;

import io.debezium.embedded.EmbeddedEngine;
import io.debezium.util.Clock;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Order(2)
@Component
public class DebeziumEmbeddedRunner implements CommandLineRunner {
    @Autowired
    private io.debezium.config.Configuration embeddedConfig;

    @Autowired
    private JsonConverter keyConverter;
    @Autowired
    private JsonConverter valueConverter;

    @Override
    public void run(String... args) throws Exception {
        EmbeddedEngine engine = EmbeddedEngine.create()
                .using(embeddedConfig)
//                .using(this.getClass().getClassLoader())
//                .using(Clock.SYSTEM)
                .notifying(this::handleRecord)
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(engine);

    }

    /**
     * For every record this method will be invoked.
     */
    private void handleRecord(SourceRecord record) {
        logRecord(record);


    }


    /**
     * 打印消息
     *
     * @param record
     */
    private void logRecord(SourceRecord record) {
        final byte[] payload = valueConverter.fromConnectData("dummy", record.valueSchema(), record.value());
        final byte[] key = keyConverter.fromConnectData("dummy", record.keySchema(), record.key());
        System.out.println("Publishing Topic --> {}" + record.topic());
        System.out.println("Key --> {}" + new String(key));
        System.out.println("Payload --> {}" + new String(payload));
    }


}
