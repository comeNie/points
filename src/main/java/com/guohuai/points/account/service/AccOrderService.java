package com.guohuai.points.account.service;


import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guohuai.basic.common.StringUtil;
import com.guohuai.basic.component.ext.web.BaseResp;
import com.guohuai.points.account.dao.AccOrderDao;
import com.guohuai.points.account.entity.AccOrderEntity;
import com.guohuai.points.component.Constant;
import com.guohuai.points.component.TradeEventCodeEnum;
import com.guohuai.points.request.CreateAccOrderRequest;
import com.guohuai.points.res.CreateAccOrderResponse;

/**
 * @ClassName: AccOrderService
 * @Description: 积分订单相关
 * @author CHENDONGHUI
 * @date 2017年3月21日 下午2:19:22
 */
@Service
public class AccOrderService {
    private final static Logger log = LoggerFactory.getLogger(AccOrderService.class);
    @Autowired
    private AccOrderDao orderDao;

    /**
     * 新增积分订单
     * @param req
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CreateAccOrderResponse addAccOrder(CreateAccOrderRequest req) {
        CreateAccOrderResponse resp = new CreateAccOrderResponse();
        resp.setReturnCode(Constant.SUCCESS);
        try {
            AccOrderEntity orderEntity = null;
            orderEntity = this.getOrderByNo(req.getOrderNo());
            //判断订单是否已存在
            if (orderEntity != null) {
                if (AccOrderEntity.ORDERSTATUS_SUCC.equals(orderEntity.getOrderStatus())) {//存在且为成功的订单
                    resp.setReturnCode(TradeEventCodeEnum.TRADE_2004.getCode());
                    resp.setErrorMessage(TradeEventCodeEnum.TRADE_2004.getName());//订单号已经存在
                    log.debug("订单号已存在orderNo:[" + orderEntity.getOrderNo() + "]");
                    return resp;
                }
            }
            log.info("订单交易:[" + JSONObject.toJSONString(req) + "]");
            Timestamp time = new Timestamp(System.currentTimeMillis());
            if (null == orderEntity) {
                orderEntity = new AccOrderEntity();
            }
            String orderNo = req.getOrderNo();
            orderEntity.setRequestNo(req.getRequestNo());
            orderEntity.setSystemSource(req.getSystemSource());
            orderEntity.setOrderNo(orderNo);
            orderEntity.setUserOid(req.getUserOid());
            orderEntity.setOrderType(req.getOrderType());
            orderEntity.setRelationProductCode(req.getRelationProductNo());
            orderEntity.setRelationProductName(req.getRelationProductName());
            orderEntity.setPoint(req.getBalance());
            orderEntity.setOrderStatus(AccOrderEntity.ORDERSTATUS_INIT);
            orderEntity.setRemark(req.getRemark());
            orderEntity.setOrderDesc(req.getOrderDesc());
            orderEntity.setUpdateTime(time);
            if (null == orderEntity.getCreateTime()) {
                orderEntity.setCreateTime(time);
            }
            log.info("保存积分定单");
            orderEntity = orderDao.save(orderEntity);
            log.info("保存积分定单结束");
            if (orderEntity != null) {
                resp.setReturnCode(Constant.SUCCESS);
                resp.setErrorMessage("成功");
                resp.setOrderOid(orderEntity.getOid());
                resp.setOrderNo(orderNo);
            }

        } catch (Exception e) {
            log.error("订单插入失败", e);
            resp.setReturnCode(Constant.FAIL);
            resp.setErrorMessage("订单保存失败");
            return resp;
        }
        return resp;
    }

    /**
     * 根据orderNo获取订单
     * @param orderNo
     * @return
     */
    public AccOrderEntity getOrderByNo(String orderNo) {
        return orderDao.findByOrderNo(orderNo);
    }

    /**
     * 根据orderNo获取订单
     * @param oid
     * @return
     */
    public AccOrderEntity getOrderOid(String oid) {
        return orderDao.findOne(oid);
    }
    
    /**
     * 更新订单状态
     * @param oid
     * @param orderStatus
     * @param errorMessage 
     * @return
     */
    public BaseResp updateOrderStatus(String oid, String orderStatus, String errorMessage) {
    	BaseResp response = new BaseResp();
    	log.info("{}积分订单状态变动,orderStatus:{}", oid, orderStatus);
    	//验证参数
 		if(StringUtil.isEmpty(oid)) {
 			response.setErrorCode(-1);
 			response.setErrorMessage("OID不能为空");
 			return response;
 		}
 		AccOrderEntity orderEntity = this.getOrderOid(oid);
 		
 		if(orderEntity!=null) {
 			orderEntity.setOrderStatus(orderStatus);
 			orderEntity.setErrorMessage(errorMessage);
 			orderEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
 			orderEntity = this.orderDao.saveAndFlush(orderEntity);
            
            log.info("更新AccOrderEntity,AccOrderEntity:{}", JSON.toJSONString(orderEntity));
            response.setErrorCode(0);
        } else {
        	response.setErrorCode(-1);
        	response.setErrorMessage("找不到订单");
        }
		return response;
    }

    String nullToStr(Object str) {
        if (null == str) {
            return "";
        }
        return str.toString();
    }


}