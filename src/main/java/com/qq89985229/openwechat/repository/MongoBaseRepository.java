package com.qq89985229.openwechat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MongoBaseRepository<T, TD> extends ReactiveMongoRepository<T, TD>, ReactiveQuerydslPredicateExecutor<T> {
}