package com.qq89985229.openwechat.service;

import com.querydsl.core.types.Predicate;
import org.reactivestreams.Publisher;
import org.springframework.data.repository.query.FluentQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.function.Function;

public interface MongoService<T> {
    Mono<T> save(T t);
    Flux<T> saveAll(Iterable<T> entities);
    Mono<T> findOne(Predicate predicate);
    Flux<T> findAll();
    Flux<T> findAll(Predicate predicate);
    Mono<Void> deleteById(String id);
    Mono<Void> deleteAllById(List<String> ids);
    Mono<Void> deleteAll(Publisher<? extends T> entityStream);
    Mono<Void> deleteAll();
    <S extends T, R, P extends Publisher<R>> P findBy(Predicate predicate, Function<FluentQuery.ReactiveFluentQuery<S>, P> queryFunction);
}

