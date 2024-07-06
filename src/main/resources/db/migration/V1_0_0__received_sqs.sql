create table received_sqs (
    id_received_sqs binary(16) not null primary key default (uuid_to_bin(uuid())),
    created_at datetime not null default current_timestamp,
    content    text
);