package com.guohuai.points.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.points.account.dao.AccountTransDao;

/**
 * @ClassName: AccountTransService
 * @Description: 积分交易相关
 * @author CHENDONGHUI
 * @date 2017年3月21日 下午4:11:12
 */
@Service
public class AccountTransService {
	private final static Logger log = LoggerFactory.getLogger(AccountTransService.class);
	
	@Autowired
	private AccountTransDao transDao;


}