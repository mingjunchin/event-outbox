## EventOutbox Implementation

This repository consists of a working example of an event outbox pattern using Debezium event router

## Pre-requisite

- Make: `brew install make`
- Docker
- Java 17

## Getting Started

### Setup services

1. On the project root level, run `make package` to build the image
2. Then, run `docker-compose up` to bring up the stacks (Kafka, Kafka-UI, Kafka Connect, Postgresql, App)

### Setup Kafka Connect

1. Under the script folders, run `./register-kafka-connect.sh` to register a Debezium kafka connect service listening
   to `event_outbox` table changes.

### Setup mock data

1. Create an account instance using the cURL request below:

   ```bash
   curl --request POST \
     --url http://localhost:8080/account \
     --header 'Content-Type: application/json' \
     --data '{
       "id": "dfe964e0-4ee5-4d56-b7a8-8e8d9ef0a24e",
       "accountNumber": "1",
       "userId": "1",
       "balance": 10.2
   }'
   ```

### Update Mock Data

1. Trigger insertion of record to `event_outbox` table by updating the account's balance. We can use the cURL request
   below:

    ```bash
    curl --request PATCH \
      --url http://localhost:8080/accounts/dfe964e0-4ee5-4d56-b7a8-8e8d9ef0a24e/balance \
      --header 'Content-Type: application/json' \
      --data 2.25
    ```

   The cURL request above updates the account's balance to `2.25`.

2. Check the table `event_outbox` to see if the record is inserted.
3. Navigate to the kafka-ui through `localhost:8081` and look for the `outbox.event.AccountBalance` topic
4. See that there's event inserted to the topic that corresponds to the record inserted to the `event_outbox` table.

## TODO

- [ ] Demonstrate how we can add AVRO schema to the event payload.
    - [x] Serialize payload as byte using AVRO schema and store as `bytea`
    - [ ] Deserialize payload using AVRO schema.