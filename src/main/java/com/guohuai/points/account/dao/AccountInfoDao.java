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
	
	@Query(value = "SELECT * FROM t_point_account WHERE userOid = ?1", nativeQuery = true)
	public List<AccountInfoEntity> findByUserOid(String userOid);
	
	@Query(value = "SELECT * FROM t_point_account WHERE accountNo = ?1", nativeQuery = true)
	public AccountInfoEntity findByAccountNo(String accountNo);
	
	@Query(value = "SELECT * FROM t_point_account WHERE oid = ?1 for update", nativeQuery = true)
	public AccountInfoEntity findByOidForUpdate(String oid);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE t_point_account SET balance = balance + ?1 , updateTime = NOW() WHERE accountNo = ?2", nativeQuery = true)
	public int updateBalance(BigDecimal balance,String accountNo);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE t_point_account SET balance = balance + ?1 , updateTime = NOW() WHERE accountNo = ?2", nativeQuery = true)
	public int addBalance(BigDecimal balance,String accountNo);

	@Query(value = "SELECT * FROM t_point_account WHERE userOid = ?1 and accountType = ?2", nativeQuery = true)
	public List<AccountInfoEntity> findByUserOidAndAccountType(String userOid,
			String accountType);

	@Query(value = "SELECT * FROM t_point_account WHERE userOid = ?1 and relationTicketCode = ?2 and accountType = ?3", nativeQuery = true)
	public List<AccountInfoEntity> findByUserOidAndAccountTypeAndProductNo(
			String userOid, String relationProduct, String accountType);

	@Query(value = "SELECT * FROM t_point_account WHERE accountType = ?1 and userOid = ?2 Limit 1", nativeQuery = true)
	public AccountInfoEntity findByTypeAndUser(String accountType, String userOid);

	@Query(value = "SELECT * FROM t_point_account WHERE accountType IN('02','03','04') AND balance > 0 AND frozenStatus='N' AND userOid = ?1 ORDER BY overdueTime DESC", nativeQuery = true)
	public List<AccountInfoEntity> findChildAccountListByUser(String userOid);

}
