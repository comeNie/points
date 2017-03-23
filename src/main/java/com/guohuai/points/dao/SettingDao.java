package com.guohuai.points.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.guohuai.points.entity.PointSettingEntity;

@RepositoryRestResource
public interface SettingDao extends JpaRepository<PointSettingEntity, String>, JpaSpecificationExecutor<PointSettingEntity> {
	
}
