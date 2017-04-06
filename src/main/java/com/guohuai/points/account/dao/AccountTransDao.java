package com.guohuai.points.account.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.guohuai.points.account.entity.AccountTransEntity;

public interface AccountTransDao extends JpaRepository<AccountTransEntity, String>, JpaSpecificationExecutor<AccountTransEntity> {

	@Query(value = "SELECT * FROM t_point_account_trans WHERE userOid = ?1 AND orderNo = ?2", nativeQuery = true)
	List<AccountTransEntity> findByUserAndOrderNo(String userOid,
			String oldOrderNo);
	
}
