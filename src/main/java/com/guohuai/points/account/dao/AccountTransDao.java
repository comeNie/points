package com.guohuai.points.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.guohuai.points.account.entity.AccountTransEntity;

public interface AccountTransDao extends JpaRepository<AccountTransEntity, String>, JpaSpecificationExecutor<AccountTransEntity> {
	
}
