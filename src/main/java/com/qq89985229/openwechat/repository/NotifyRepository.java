package com.qq89985229.openwechat.repository;

import com.qq89985229.openwechat.entity.Notify;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends MongoBaseRepository<Notify, String> {
}
