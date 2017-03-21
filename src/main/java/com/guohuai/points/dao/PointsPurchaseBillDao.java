package com.guohuai.points.dao;

import com.guohuai.points.entity.PurchaseBillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface PointsPurchaseBillDao extends JpaRepository<PurchaseBillEntity, String>, JpaSpecificationExecutor<PurchaseBillEntity> {
	public List<PurchaseBillEntity> findByUserOid(String userOid);

}
