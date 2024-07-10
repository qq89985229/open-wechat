package com.qq89985229.openwechat.service.impl;

import com.qq89985229.openwechat.entity.Authorizer;
import com.qq89985229.openwechat.repository.AuthorizerRepository;
import com.qq89985229.openwechat.service.AuthorizerService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizerServiceImpl extends MongoServiceImpl<Authorizer, AuthorizerRepository> implements AuthorizerService {
}
