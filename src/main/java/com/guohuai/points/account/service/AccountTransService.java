package com.guohuai.points.account.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.guohuai.points.account.dao.AccountTransDao;
import com.guohuai.points.account.entity.AccountTransEntity;
import com.guohuai.points.component.AccountTypeEnum;
import com.guohuai.points.component.Constant;
import com.guohuai.points.request.AccountTransRequest;
import com.guohuai.points.res.AccountTransResponse;

/**
 * @ClassName: AccountTransService
 * @Description: 积分交易流水相关
 * @author CHENDONGHUI
 * @date 2017年3月22日 上午11:41:12
 */
@Service
public class AccountTransService {
	private final static Logger log = LoggerFactory.getLogger(AccountTransService.class);
	
	@Autowired
	private AccountTransDao transDao;

	/**
	 * 新增积分交易流水
	 * @param req
	 * @return
	 */
	public AccountTransResponse addAccTrans(AccountTransRequest req) {
		log.info("新增积分账户交易记录：{}",JSONObject.toJSONString(req));
		AccountTransResponse resp  = new AccountTransResponse();
		resp.setReturnCode(Constant.SUCCESS);
		try {
			AccountTransEntity transEntity = new AccountTransEntity();
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String orderNo = req.getOrderNo();
            transEntity.setAccountType(req.getAccountType());
            transEntity.setAccountName(AccountTypeEnum.getEnumName(req.getAccountType()));
            if("add".equals(req.getDirection())){
            	transEntity.setDirection(AccountTransEntity.ADD);
            }else{
            	transEntity.setDirection(AccountTransEntity.REDUCE);
            }
            transEntity.setOrderPoint(req.getOrderPoint());
            transEntity.setPoint(req.getBalance());
            transEntity.setTransAccountNo(req.getTransAccountNo());
        	transEntity.setRequestNo(req.getRequestNo());
        	transEntity.setSystemSource(req.getSystemSource());
        	transEntity.setOrderNo(orderNo);
        	transEntity.setUserOid(req.getUserOid());
        	transEntity.setOrderType(req.getOrderType());
        	transEntity.setRelationProductCode(req.getRelationProductNo());
        	transEntity.setRelationProductName(req.getRelationProductName());
        	transEntity.setPoint(req.getBalance());
        	transEntity.setRemark(req.getRemark());
        	transEntity.setOrderDesc(req.getOrderDesc());
        	transEntity.setOrderDesc(req.getOrderDesc());
        	transEntity.setUpdateTime(time);
            transEntity.setCreateTime(time);
            log.info("保存积分交易流水");
            transEntity = transDao.save(transEntity);
            log.info("保存积分交易流水结束");
            if (transEntity != null) {
                resp.setReturnCode(Constant.SUCCESS);
                resp.setErrorMessage("成功");
            }

        } catch (Exception e) {
            log.error("保存积分交易流水失败", e);
            resp.setReturnCode(Constant.FAIL);
            resp.setErrorMessage("保存积分交易流水失败");
            return resp;
        }
		return resp;
	}

	/**
	 * 批量新增账户交易流水
	 * @param list
	 * @return
	 */
	@Transactional
	public AccountTransResponse addAccTransList(List<AccountTransRequest> list) {
		AccountTransResponse resp  = new AccountTransResponse();
		for(AccountTransRequest req : list){
			resp = addAccTrans(req);
			if(!"Constant.SUCCESS".equals(resp.getReturnCode())){
				return resp;
			}
		}
		return resp;
	}
}