package com.qq89985229.openwechat.repository;

import com.qq89985229.openwechat.entity.Privacy;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivacyRepository extends MongoBaseRepository<Privacy, String> {
}
