package com.github.brdr3.awsresiliencepoc.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@EqualsAndHashCode
@Table(name = "received_sqs")
public class ReceivedSqsEntity {
    @Id
    @Column(name = "id_received_sqs", nullable = false, updatable = false, unique = true)
    private UUID idReceivedSqsMessage;

    @Column(name = "created_at", columnDefinition = "datetime default current_timestamp", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "content", columnDefinition = "text")
    private String content;
}
