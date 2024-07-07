create table kafka_production_history (
    id_kafka_production_history binary(16) not null primary key default (uuid_to_bin(uuid())),
    created_at                  datetime not null default current_timestamp,
    status                      text,
    id_received_sqs             binary(16) not null,
    foreign key (id_received_sqs) references received_sqs(id_received_sqs)
);