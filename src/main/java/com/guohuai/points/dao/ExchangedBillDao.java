package com.guohuai.points.dao;

import com.guohuai.points.entity.ExchangedBillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ExchangedBillDao extends JpaRepository<ExchangedBillEntity, String>, JpaSpecificationExecutor<ExchangedBillEntity> {

	public List<ExchangedBillEntity> findByUserOid(String userOid);

}
