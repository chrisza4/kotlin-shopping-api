create table purchase_requests (
    id serial not null PRIMARY KEY,
    reason varchar,
    createdAt timestamp with time zone,
    owner_id bigint
);