package com.ljt.study.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author LiJingTang
 * @date 2021-06-15 18:59
 */
@Slf4j
@Service
public class TccServiceImpl implements TccService {

    @Override
    public String rpcMethod(String id) {
        log.info("try 逻辑");
        return id;
    }

    @Override
    public boolean rpcMethodCommit(BusinessActionContext actionContext) {
        log.info("commit 逻辑：{}", actionContext.getActionContext());
        return true;
    }

    @Override
    public boolean rpcMethodRollback(BusinessActionContext actionContext) {
        log.info("rollback 逻辑：{}", actionContext.getActionContext());
        return true;
    }

}
