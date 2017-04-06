package com.guohuai.points.service;

import com.guohuai.basic.common.StringUtil;
import com.guohuai.basic.component.ext.web.BaseResp;
import com.guohuai.basic.component.ext.web.PageResp;
import com.guohuai.points.dao.DeliveryManageDao;
import com.guohuai.points.entity.DeliveryEntity;
import com.guohuai.points.form.DeliveryForm;
import com.guohuai.points.form.ExchangedBillForm;
import com.guohuai.points.res.DeliveryRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DeliveryManageService {
	@Autowired
	private DeliveryManageDao deliveryManageDao;

	/**
	 * 分页查询
	 *
	 * @param req
	 * @return
	 */
	public PageResp<DeliveryRes> page(DeliveryForm req) {

		Page<DeliveryEntity> pages = deliveryManageDao.findAll(buildSpecification(req), new PageRequest(req.getPage() - 1, req.getRows()));
		PageResp<DeliveryRes> resPage = new PageResp<>();

		for (DeliveryEntity page : pages) {
			DeliveryRes res = new DeliveryRes();
			BeanUtils.copyProperties(page, res);
			resPage.getRows().add(res);
		}
		resPage.setTotal(pages.getTotalElements());
		log.info("发货管理查询：返回数据条数：{} ,数据总条数：{}", resPage.getRows().size(), pages.getTotalElements());
		return resPage;
	}

	private Specification<DeliveryEntity> buildSpecification(final DeliveryForm req) {
		return new Specification<DeliveryEntity>() {
			@Override
			public Predicate toPredicate(Root<DeliveryEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != req.getStartTime()) {
					list.add(cb.greaterThanOrEqualTo(root.get("orderedTime").as(Date.class), req.getStartTime()));
				}
				if (null != req.getEndTime()) {
					list.add(cb.lessThan(root.get("orderedTime").as(Date.class), req.getEndTime()));
				}
				if (null != req.getState()) {
					list.add(cb.equal(root.get("state").as(Integer.class), req.getState()));
				}
				if (!StringUtil.isEmpty(req.getOrderNumber())) {
					list.add(cb.like(root.get("orderNumber").as(String.class), "%" + req.getOrderNumber() + "%"));
				}
				query.where(cb.and(list.toArray(new Predicate[list.size()])));
				query.orderBy(cb.asc(root.get("state").as(Integer.class)), cb.desc(root.get("orderedTime").as(Date.class)));

				return query.getRestriction();
			}
		};
	}

	public DeliveryRes findById(String oid) {

		DeliveryEntity entity = deliveryManageDao.findOne(oid);
		DeliveryRes billRes = new DeliveryRes();
		BeanUtils.copyProperties(entity, billRes);

		return billRes;
	}

	@Transactional
	public DeliveryRes save(DeliveryForm req) {

		DeliveryEntity entity = deliveryManageDao.updateByOid(req.getOid());
		entity.setLogisticsCompany(req.getLogisticsCompany());
		entity.setLogisticsNumber(req.getLogisticsNumber());
		entity.setSendOperater(req.getSendOperater());
		entity.setSendTime(new Date());
		entity.setState(1);
		log.info("新增或修改发货记录持久化数据：id={} {}", entity.getOid(), entity);

		deliveryManageDao.save(entity);
		return new DeliveryRes();
	}

	@Transactional
	public BaseResp cancel(DeliveryForm req) {

		DeliveryEntity entity = deliveryManageDao.updateByOid(req.getOid());
		entity.setCancelOperater(req.getCancelOperater());
		entity.setCancelReason(req.getCancelReason());
		entity.setCancelTime(new Date());
		entity.setState(2);
		log.info("取消发货记录持久化数据：id={} {}", entity.getOid(), entity);

		deliveryManageDao.save(entity);

		// TODO 调用积分接口退积分
		return new DeliveryRes();
	}
}
