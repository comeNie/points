package com.guohuai.points.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.guohuai.basic.common.StringUtil;
import com.guohuai.points.dao.GoodsDao;
import com.guohuai.points.entity.PointGoodsEntity;
import com.guohuai.points.form.GoodsForm;
import com.guohuai.points.res.GoodsRes;

@Service
public class GoodsService {
	
	private final static Logger logger = LoggerFactory.getLogger(GoodsService.class);
	
	@Autowired
	private GoodsDao goodsDao;
	
	/**
	 * 新增积分商品
	 * @param req
	 */
	public void saveGoods(GoodsForm req){
		logger.info("新增积分商品参数, createGoodsRequest:{}", JSON.toJSONString(req));
		PointGoodsEntity entity = new PointGoodsEntity();
		BeanUtils.copyProperties(req, entity);
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		entity.setOid(StringUtil.uuid());
		entity.setState(0);
		goodsDao.save(entity);
	}
	
	/**
	 * 编辑积分商品
	 * @param req
	 */
	public void updateGoods(GoodsForm req){
		logger.info("编辑积分商品参数, createGoodsRequest:{}", JSON.toJSONString(req));
		PointGoodsEntity entity = goodsDao.findOne(req.getOid());
		if(null != entity){
			entity.setName(req.getName());
			entity.setType(req.getType());
			entity.setNeedPoints(req.getNeedPoints());
			entity.setTotalCount(req.getTotalCount());
			entity.setExchangedCount(req.getTotalCount());
			entity.setRemark(req.getRemark());
			entity.setFileOid(req.getFileOid());
			entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			goodsDao.save(entity);
		}
	}
	
	/**
	 * 上架、下架、删除积分商品
	 * @param req
	 */
	public void editGoods(GoodsForm req){
		logger.info("上架、下架积分商品参数, createGoodsRequest:{}", JSON.toJSONString(req));
		PointGoodsEntity entity = goodsDao.findOne(req.getOid());
		if(null != entity){
			entity.setState(req.getState());
			entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			goodsDao.save(entity);
		}
	}
	
	/**
	 * 分页查询积分商品列表
	 * @param req
	 * @return
	 */
	public GoodsRes page(GoodsForm req){
		Page<PointGoodsEntity> listPage = goodsDao.findAll(this.buildSpecification(req), new PageRequest(req.getPage() -1 , req.getRows()));
		GoodsRes res = null;
		if(null != listPage && listPage.getSize() > 0){
			res = new GoodsRes();
			res.setRows(listPage.getContent());
			res.setTotalPage(listPage.getTotalPages());
			res.setPage(req.getPage());
			res.setRow(req.getRows());
			res.setTotal(listPage.getTotalElements());
		}
		return res;
	}
	
	/**
	 * 商品的查询条件
	 * @param req
	 * @return
	 */
	public Specification<PointGoodsEntity> buildSpecification(final GoodsForm req){
		Specification<PointGoodsEntity> spec = new Specification<PointGoodsEntity>() {
			@Override
			public Predicate toPredicate(Root<PointGoodsEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> bigList = new ArrayList<Predicate>();
				if (!StringUtil.isEmpty(req.getName()))
					bigList.add(cb.like(root.get("name").as(String.class), req.getName()));
				if (!StringUtil.isEmpty(req.getType()))
					bigList.add(cb.equal(root.get("type").as(String.class), req.getType()));
				if (null != req.getState())
					bigList.add(cb.equal(root.get("state"), req.getState()));
				
				query.where(cb.and(bigList.toArray(new Predicate[bigList.size()])));
				query.orderBy(cb.desc(root.get("createTime")));
				return query.getRestriction();
			}
		};
		return spec;
	}
	
}
