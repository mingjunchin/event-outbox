package com.example.eventoutbox.usecase;

import com.example.eventoutbox.entity.AccountBalance;
import com.example.eventoutbox.entity.Event;
import com.example.eventoutbox.repository.AccountBalanceRepository;
import com.example.eventoutbox.repository.EventRepository;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClientFactory;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class UpdateAccountBalance {

    private final AccountBalanceRepository accountBalanceRepository;
    private final EventRepository eventRepository;
    private final KafkaAvroSerializer kafkaAvroSerializer;

    public UpdateAccountBalance(
            AccountBalanceRepository accountBalanceRepository,
            EventRepository eventRepository
    ) {
        this.accountBalanceRepository = accountBalanceRepository;
        this.eventRepository = eventRepository;

        SchemaRegistryClient schemaRegistryClient =
                SchemaRegistryClientFactory.newClient(
                        List.of("localhost:8082"),
                        1000,
                        Collections.emptyList(),
                        Collections.emptyMap(),
                        Collections.emptyMap());
        this.kafkaAvroSerializer = new KafkaAvroSerializer(schemaRegistryClient);
    }

    @Transactional
    public AccountBalance byId(UUID id, BigDecimal balance) {
        System.out.println("Id: " + id + "\tbalance: " + balance);
        AccountBalance accountBalance = accountBalanceRepository.findById(id)
                .orElseThrow();

        accountBalance.updateBalance(balance);
        AccountBalance updatedAccountBalance = accountBalanceRepository.save(accountBalance);
        var factEventPayload = new com.example.eventoutbox.AccountBalance(
                updatedAccountBalance.getId().toString(),
                updatedAccountBalance.getAccountNumber(),
                updatedAccountBalance.getUserId(),
                updatedAccountBalance.getBalance().doubleValue(),
                updatedAccountBalance.getCreatedAt(),
                updatedAccountBalance.getUpdatedAt()
        );
        eventRepository.save(
                new Event(
                        UUID.randomUUID().toString(),
                        "BalanceUpdate",
                        UUID.randomUUID(),
                        false,
                        "deposit",
                        updatedAccountBalance.getId(),
                        AccountBalance.class.getSimpleName(),
                        kafkaAvroSerializer.serialize(AccountBalance.class.getSimpleName(), factEventPayload),
                        Instant.now(),
                        Instant.now(),
                        Instant.now()
                )
        );
        return updatedAccountBalance;
    }
}
