package com.ljt.study.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @author LiJingTang
 * @date 2021-06-15 18:56
 */
@LocalTCC
public interface TccService {

    @TwoPhaseBusinessAction(name = "TccServiceRpcMethod", commitMethod = "rpcMethodCommit", rollbackMethod = "rpcMethodRollback")
    String rpcMethod();

    boolean rpcMethodCommit(BusinessActionContext actionContext);

    boolean rpcMethodRollback(BusinessActionContext actionContext);

}
