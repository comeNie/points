package com.guohuai.points.dao;

import com.guohuai.points.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface DeliveryManageDao extends JpaRepository<DeliveryEntity, String>, JpaSpecificationExecutor<DeliveryEntity> {

	@Query(value = "SELECT oid, orderNumber, userName, address, orderedTime, goodsName, goodsCount, sendTime, sendOperater, logisticsCompany, logisticsNumber, state, cancelReason, cancelOperater, cancelTime FROM t_point_delivery WHERE oid=?1  FOR UPDATE", nativeQuery = true)
	DeliveryEntity updateByOid(String oid);
}
