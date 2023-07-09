create table if not exists event (
    event_id text not null primary key,
    event_type text,
    customer_id uuid,
    pii boolean,
    origin text,
    aggregate_id uuid,
    aggregate_type text,
    payload bytea,
    event_timestamp timestamp without time zone,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
)