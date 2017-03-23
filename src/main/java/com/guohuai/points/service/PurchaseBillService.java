package com.guohuai.points.service;

import com.guohuai.basic.common.StringUtil;
import com.guohuai.basic.component.ext.web.PageResp;
import com.guohuai.points.dao.PurchaseBillDao;
import com.guohuai.points.entity.PurchaseBillEntity;
import com.guohuai.points.form.PurchaseBillFrom;
import com.guohuai.points.res.PurchaseBillRes;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PurchaseBillService {
	@Autowired
	private PurchaseBillDao purchaseBillDao;

	/**
	 * 分页查询
	 *
	 * @param req
	 * @return
	 */
	public PageResp<PurchaseBillRes> page(PurchaseBillFrom req) {

		Page<PurchaseBillEntity> pages = purchaseBillDao.findAll(buildSpecification(req), new PageRequest(req.getPage() - 1, req.getRows()));
		PageResp<PurchaseBillRes> resPage = new PageResp<>();

		for (PurchaseBillEntity page : pages) {
			PurchaseBillRes res = new PurchaseBillRes();
			BeanUtils.copyProperties(page, res);
			resPage.getRows().add(res);
		}
		resPage.setTotal(pages.getTotalElements());
		log.info("积分购买记录查询：返回数据条数：{} ,数据总条数：{}", resPage.getRows().size(), pages.getTotalElements());
		return resPage;
	}

	private Specification<PurchaseBillEntity> buildSpecification(PurchaseBillFrom req) {
		return new Specification<PurchaseBillEntity>() {
			@Override
			public Predicate toPredicate(Root<PurchaseBillEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (!StringUtil.isEmpty(req.getUserOid())) {
					list.add(cb.like(root.get("userOid").as(String.class), "%" + req.getUserOid() + "%"));
				}
				if (!StringUtil.isEmpty(req.getSettingOid())) {
					list.add(cb.like(root.get("settingOid").as(String.class), "%" + req.getSettingOid() + "%"));
				}
				if (null != req.getStartTime()) {
					list.add(cb.greaterThanOrEqualTo(root.get("purchaseTime").as(Date.class), req.getStartTime()));
				}
				if (null != req.getEndTime()) {
					list.add(cb.lessThan(root.get("purchaseTime").as(Date.class), req.getEndTime()));
				}
				if (null != req.getState()) {
					list.add(cb.equal(root.get("state").as(Integer.class), req.getState()));
				}

				query.where(cb.and(list.toArray(new Predicate[list.size()])));
				query.orderBy(cb.desc(root.get("createTime").as(Date.class)));
				return query.getRestriction();
			}
		};
	}

	public PurchaseBillRes findById(String oid) {

		PurchaseBillEntity entity = purchaseBillDao.findOne(oid);
		PurchaseBillRes billRes = new PurchaseBillRes();
		BeanUtils.copyProperties(entity, billRes);

		return billRes;
	}
}
