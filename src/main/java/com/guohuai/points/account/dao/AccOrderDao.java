package com.guohuai.points.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.guohuai.points.account.entity.AccOrderEntity;

public interface AccOrderDao extends JpaRepository<AccOrderEntity, String>, JpaSpecificationExecutor<AccOrderEntity> {
	
	@Query(value = "SELECT * FROM T_ACCOUNT_ORDER WHERE orderNo = ?1", nativeQuery = true)
	public AccOrderEntity findByOrderNo(String orderNo);
	
}
