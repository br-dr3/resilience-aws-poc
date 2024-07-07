package com.github.brdr3.awsresiliencepoc.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "kafka_production_history")
public class KafkaProductionHistoryEntity {

    @Id
    @Column(name = "id_kafka_production_history", nullable = false, updatable = false, unique = true)
    private UUID idProductionHistory;

    @Column(name = "created_at", columnDefinition = "datetime default current_timestamp", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", columnDefinition = "text")
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_received_sqs")
    private ReceivedSqsEntity receivedSqsMessage;
}
