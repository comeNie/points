package com.guohuai.points.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.guohuai.points.entity.PointFileEntity;

@RepositoryRestResource
public interface FileDao extends JpaRepository<PointFileEntity, String>, JpaSpecificationExecutor<PointFileEntity> {
	
	public List<PointFileEntity> findByGoodsOid(String goodsOid);
}
