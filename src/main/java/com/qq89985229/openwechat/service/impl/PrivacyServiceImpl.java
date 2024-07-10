package com.qq89985229.openwechat.service.impl;

import com.qq89985229.openwechat.entity.Privacy;
import com.qq89985229.openwechat.repository.PrivacyRepository;
import com.qq89985229.openwechat.service.PrivacyService;
import org.springframework.stereotype.Service;

@Service
public class PrivacyServiceImpl extends MongoServiceImpl<Privacy, PrivacyRepository> implements PrivacyService {
}
