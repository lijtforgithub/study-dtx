package com.ljt.study.dtx;

import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.support.DTXUserControls;
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
public class FeignLcnAspect {

    @Pointcut("@annotation(com.ljt.study.dtx.FeignLcnDTX)")
    public void manuallyDtx() {}

    @SneakyThrows
    @AfterThrowing
    public void rollbackDtx(Throwable e) {
        log.error("方法执行异常", e);
        final String groupId = DTXLocalContext.getOrNew().getGroupId();
        if (StringUtils.isNotBlank(groupId)) {
            DTXUserControls.rollbackGroup(groupId);
        }
    }

}
