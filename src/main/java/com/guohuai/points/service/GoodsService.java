package com.guohuai.points.service;

import java.math.BigDecimal;
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
import com.guohuai.basic.component.ext.web.BaseResp;
import com.guohuai.basic.component.ext.web.PageResp;
import com.guohuai.points.dao.GoodsDao;
import com.guohuai.points.entity.PointGoodsEntity;
import com.guohuai.points.entity.PointSettingEntity;
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
	public BaseResp saveGoods(GoodsForm req){
		logger.info("新增积分商品参数, createGoodsRequest:{}", JSON.toJSONString(req));
		BaseResp response = new BaseResp();
		PointGoodsEntity entity = new PointGoodsEntity();
		BeanUtils.copyProperties(req, entity);
		//默认已兑换数为0
		entity.setExchangedCount(new BigDecimal(0));
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		entity.setOid(StringUtil.uuid());
		entity.setState(0);
		goodsDao.save(entity);
		response.setErrorCode(0);
		return response;
	}
	
	/**
	 * 编辑积分商品
	 * @param req
	 */
	public BaseResp updateGoods(GoodsForm req){
		logger.info("编辑积分商品参数, createGoodsRequest:{}", JSON.toJSONString(req));
		BaseResp response = new BaseResp();
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
			response.setErrorCode(0);
		}else{
			response.setErrorCode(-1);
		}
		return response;
	}
	
	/**
	 * 上架、下架、删除积分商品
	 * @param req
	 */
	public BaseResp editGoods(GoodsForm req){
		logger.info("上架、下架积分商品参数, createGoodsRequest:{}", JSON.toJSONString(req));
		BaseResp response = new BaseResp();
		PointGoodsEntity entity = goodsDao.findOne(req.getOid());
		if(null != entity){
			entity.setState(req.getState());
			entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			goodsDao.save(entity);
			response.setErrorCode(0);
		}else{
			response.setErrorCode(-1);
		}
		
		return response;
	}
	
	/**
	 * 分页查询积分商品列表
	 * @param req
	 * @return
	 */
	public PageResp<PointGoodsEntity> page(GoodsForm req){
		Page<PointGoodsEntity> listPage = goodsDao.findAll(this.buildSpecification(req), new PageRequest(req.getPage() -1 , req.getRows()));
		PageResp<PointGoodsEntity> pagesRep = new PageResp<PointGoodsEntity>();
		if(null != listPage && listPage.getSize() > 0){
			pagesRep.setRows(listPage.getContent());
			pagesRep.setTotal(listPage.getTotalElements());
		}
		return pagesRep;
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
				if (!StringUtil.isEmpty(req.getName())){
					bigList.add(cb.like(root.get("name").as(String.class), "%" + req.getName() + "%"));					
				}
				if (!StringUtil.isEmpty(req.getType())){
					bigList.add(cb.equal(root.get("type").as(String.class), req.getType()));					
				}
				if (null != req.getState()){
					bigList.add(cb.equal(root.get("state"), req.getState()));
				} else {
					//默认查询非删除的商品
					bigList.add(cb.gt(root.get("state"), -1));
				}
				
				query.where(cb.and(bigList.toArray(new Predicate[bigList.size()])));
				query.orderBy(cb.desc(root.get("createTime")));
				return query.getRestriction();
			}
		};
		return spec;
	}
	
}
