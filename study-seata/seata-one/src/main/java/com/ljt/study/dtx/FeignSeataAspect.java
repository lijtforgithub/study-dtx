package com.ljt.study.dtx;

import io.seata.core.context.RootContext;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author LiJingTang
 * @date 2021-06-15 17:42
 */
@Slf4j
@Aspect
public class FeignSeataAspect {

    @Pointcut("@annotation(com.ljt.study.dtx.FeignSeataDTX)")
    public void manuallyDtx() {}

    @SneakyThrows
    @AfterThrowing
    public void rollbackDtx(Throwable e) {
        log.error("方法执行异常", e);
        if (StringUtils.isNotBlank(RootContext.getXID())) {
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
        }
    }

}
