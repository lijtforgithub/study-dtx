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
    public String rpcMethod() {
        log.info("try 逻辑");
        return null;
    }

    @Override
    public boolean rpcMethodCommit(BusinessActionContext actionContext) {
        log.info("commit 逻辑");
        return true;
    }

    @Override
    public boolean rpcMethodRollback(BusinessActionContext actionContext) {
        log.info("cancel 逻辑");
        return true;
    }

}
