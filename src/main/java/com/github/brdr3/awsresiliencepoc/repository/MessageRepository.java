package com.github.brdr3.awsresiliencepoc.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MessageRepository {
    @PersistenceContext
    private EntityManager entityManager;
}
