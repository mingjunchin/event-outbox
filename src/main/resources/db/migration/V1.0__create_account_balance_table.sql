create table if not exists account_balance (
    id uuid not null primary key,
    account_number text,
    user_id text,
    balance decimal(19,2),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
)