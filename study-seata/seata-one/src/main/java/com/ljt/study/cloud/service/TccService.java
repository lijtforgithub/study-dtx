package com.ljt.study.cloud.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @author LiJingTang
 * @date 2021-06-15 18:56
 */
@LocalTCC
public interface TccService {

    @TwoPhaseBusinessAction(name = "TccServiceRpcMethod", commitMethod = "rpcMethodCommit", rollbackMethod = "rpcMethodRollback")
    String rpcMethod(@BusinessActionContextParameter(paramName = "ID") String id);

    boolean rpcMethodCommit(BusinessActionContext actionContext);

    boolean rpcMethodRollback(BusinessActionContext actionContext);

}
