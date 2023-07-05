package com.example.eventoutbox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Event {
    // delta eventId = UUID
    // fact eventId = <domain>.<name>.<entityId>.<version>
    // TODO: turn it into valueObject
    @Id
    private String eventId;
    private String eventType;
    private UUID customerId;
    private boolean pii;
    private String origin;
    private UUID aggregateId;
    private String aggregateType;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> payload;
    private Instant eventTimestamp;
    private Instant createdAt;
    private Instant updatedAt;

    public Event() {
    }

    public Event(String eventId, String eventType, UUID customerId, boolean pii, String origin, UUID aggregateId, String aggregateType, Map<String, Object> payload, Instant eventTimestamp, Instant createdAt, Instant updatedAt) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.customerId = customerId;
        this.pii = pii;
        this.origin = origin;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.payload = payload;
        this.eventTimestamp = eventTimestamp;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public boolean isPii() {
        return pii;
    }

    public void setPii(boolean pii) {
        this.pii = pii;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public Instant getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Instant eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return pii == event.pii && Objects.equals(eventId, event.eventId) && Objects.equals(eventType, event.eventType) && Objects.equals(customerId, event.customerId) && Objects.equals(origin, event.origin) && Objects.equals(aggregateId, event.aggregateId) && Objects.equals(aggregateType, event.aggregateType) && Objects.equals(payload, event.payload) && Objects.equals(eventTimestamp, event.eventTimestamp) && Objects.equals(createdAt, event.createdAt) && Objects.equals(updatedAt, event.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventType, customerId, pii, origin, aggregateId, aggregateType, payload, eventTimestamp, createdAt, updatedAt);
    }
}
