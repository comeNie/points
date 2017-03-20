package com.guohuai.points.account.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.guohuai.points.account.entity.AccountInfoEntity;

public interface AccountInfoDao extends JpaRepository<AccountInfoEntity, String>, JpaSpecificationExecutor<AccountInfoEntity> {
	
	@Query(value = "SELECT * FROM T_ACCOUNT_INFO WHERE userOid = ?1", nativeQuery = true)
	public List<AccountInfoEntity> findByUserOid(String userOid);
	
	@Query(value = "SELECT * FROM T_ACCOUNT_INFO WHERE accountNo = ?1", nativeQuery = true)
	public AccountInfoEntity findByAccountNo(String accountNo);
	
	@Query(value = "SELECT * FROM T_ACCOUNT_INFO WHERE oid = ?1 for update", nativeQuery = true)
	public AccountInfoEntity findByOidForUpdate(String oid);
	
	@Query(value = "SELECT * FROM T_ACCOUNT_INFO WHERE userOid = ?1 and userType = ?2", nativeQuery = true)
	public List<AccountInfoEntity> findByUserOidAndUserType(String userOid,String userType);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE T_ACCOUNT_INFO SET balance = balance + ?1 , updateTime = NOW() WHERE accountNo = ?2", nativeQuery = true)
	public int updateBalance(BigDecimal balance,String accountNo);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE T_ACCOUNT_INFO SET balance = balance + ?1 , updateTime = NOW() WHERE accountNo = ?2", nativeQuery = true)
	public int addBalance(BigDecimal balance,String accountNo);
	
}
