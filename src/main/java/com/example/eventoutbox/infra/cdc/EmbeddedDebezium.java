package com.example.eventoutbox.infra.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//@Component
public class EmbeddedDebezium {

    private final DebeziumEngine<ChangeEvent<String, String>> debeziumEngine;
    private final Executor executor;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmbeddedDebezium() {
        Configuration config = Configuration.create()
                /* begin engine properties */
                .with("connector.class",
                        "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage",
                        "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename",
                        "/opt/offset.dat")
                .with("offset.flush.interval.ms", 1000)
                /* begin connector properties */
                .with("name", "pg-connector")
                .with("database.hostname", "postgres")
                .with("database.dbname", "postgres")
                .with("database.port", 5432)
                .with("database.user", "postgres")
                .with("database.password", "postgres")
                .with("topic.prefix", "events")
                .with("table.include.list", "public.event")
                .with("plugin.name", "pgoutput")
                /* Event router configuration */
                .with("transforms", "outbox")
                .with("transforms.outbox.type", "io.debezium.transforms.outbox.EventRouter")
                .with("transforms.outbox.table.field.event.id", "event_id")
                .with("transforms.outbox.table.field.event.key", "aggregate_id")
                .with("transforms.outbox.route.by.field", "aggregate_type")
                .with("transforms.outbox.table.fields.additional.placement", "pii:header,origin:header:customer_id:header,event_type::header")
//                .with("schema.history.internal",
//                        "io.debezium.storage.file.history.FileSchemaHistory")
//                .with("schema.history.internal.file.filename",
//                        "/path/to/storage/schemahistory.dat")
                .build();
        this.debeziumEngine = DebeziumEngine.create(Json.class)
                .using(config.asProperties())
                .notifying(this::publishToTopic)
                .build();

        this.executor = Executors.newSingleThreadExecutor();
        executor.execute(this.debeziumEngine);

        // Configure kafka
        this.kafkaTemplate = new KafkaTemplate<>(
                new DefaultKafkaProducerFactory<>(senderProps())
        );
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //...
        return props;
    }

    private void publishToTopic(ChangeEvent<String, String> event) {
        System.out.println("PROCESSING ITEM:\n" + event);
        System.out.println("Event Key:\n" + event.key());
        System.out.println("Event Value:\n" + event.value());
        try {
            Map<String, Object> eventValue = objectMapper.readValue(event.value(), Map.class);
            Map<String, Object> eventDetails = (Map<String, Object>) ((Map<String, Object>) eventValue.get("payload")).get("after");
            String eventId = (String) eventDetails.get("event_id");
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                    "deposit.events",
                    null,
                    eventId,
                    objectMapper.writeValueAsString(eventDetails),
                    List.of(
                            new RecordHeader("customerId", eventDetails.get("customer_id").toString().getBytes()),
                            new RecordHeader("pii", eventDetails.get("pii").toString().getBytes()),
                            new RecordHeader("origin", eventDetails.get("origin").toString().getBytes())
                    )
            );
//            this.kafkaTemplate.send(producerRecord);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
