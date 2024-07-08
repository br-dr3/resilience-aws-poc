package com.github.brdr3.awsresiliencepoc.repository;

import com.github.brdr3.awsresiliencepoc.domain.DomainMessage;
import com.github.brdr3.awsresiliencepoc.domain.enuns.Status;
import com.github.brdr3.awsresiliencepoc.entity.KafkaProductionHistoryEntity;
import com.github.brdr3.awsresiliencepoc.entity.ReceivedSqsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MessageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveReceived(final DomainMessage message) {
        final ReceivedSqsEntity receivedSqsEntity = new ReceivedSqsEntity();
        receivedSqsEntity.setIdReceivedSqsMessage(message.getId());
        receivedSqsEntity.setContent(message.getContent());
        receivedSqsEntity.setCreatedAt(LocalDateTime.now());

        entityManager.merge(receivedSqsEntity);
    }

    @Transactional
    public void saveProduced(final DomainMessage domainMessage, final Status status) {
        final ReceivedSqsEntity receivedSqsEntity = entityManager.find(ReceivedSqsEntity.class, domainMessage.getId());

        final KafkaProductionHistoryEntity kafkaProductionHistoryEntity = new KafkaProductionHistoryEntity();
        kafkaProductionHistoryEntity.setIdProductionHistory(UUID.randomUUID());
        kafkaProductionHistoryEntity.setStatus(status.name());
        kafkaProductionHistoryEntity.setReceivedSqsMessage(receivedSqsEntity);
        kafkaProductionHistoryEntity.setCreatedAt(LocalDateTime.now());

        entityManager.merge(kafkaProductionHistoryEntity);
    }
}
