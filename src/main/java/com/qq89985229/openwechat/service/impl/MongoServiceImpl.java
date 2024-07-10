package com.qq89985229.openwechat.service.impl;

import com.qq89985229.openwechat.repository.MongoBaseRepository;
import com.qq89985229.openwechat.service.MongoService;
import com.querydsl.core.types.Predicate;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.FluentQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.function.Function;

public abstract class MongoServiceImpl<T, R extends MongoBaseRepository<T, String>> implements MongoService<T> {
    @Autowired(required = false)
    protected R repository;

    @Override
    public Mono<T> save(T t) {
        return repository.save(t);
    }

    @Override
    public Flux<T> saveAll(Iterable<T> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public Mono<T> findOne(Predicate predicate) {
        return repository.findOne(predicate);
    }

    @Override
    public Flux<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Flux<T> findAll(Predicate predicate) {
        return repository.findAll(predicate);
    }

    @Override
    public <S extends T, R, P extends Publisher<R>> P findBy(Predicate predicate, Function<FluentQuery.ReactiveFluentQuery<S>, P> queryFunction) {
        return repository.findBy(predicate, queryFunction);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteAllById(List<String> ids) {
        return repository.deleteAllById(ids);
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends T> entityStream) {
        return repository.deleteAll(entityStream);
    }

    @Override
    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }


}

