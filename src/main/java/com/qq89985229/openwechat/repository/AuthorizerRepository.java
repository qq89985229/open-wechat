package com.qq89985229.openwechat.repository;

import com.qq89985229.openwechat.entity.Authorizer;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizerRepository extends MongoBaseRepository<Authorizer, String> {
}
