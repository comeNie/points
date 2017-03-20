package com.guohuai.points.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.guohuai.points.account.entity.TransEntity;

public interface TransDao extends JpaRepository<TransEntity, String>, JpaSpecificationExecutor<TransEntity> {
	
}
