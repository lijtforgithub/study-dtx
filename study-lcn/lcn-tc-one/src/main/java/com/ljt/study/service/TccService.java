package com.ljt.study.service;

import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.ljt.study.mapper.UserMapper;
import com.ljt.study.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiJingTang
 * @date 2021-06-15 18:59
 */
@Slf4j
@Service
public class TccService {

    private static final ConcurrentHashMap<String, Integer> IDS = new ConcurrentHashMap<>();

    @Autowired
    private UserMapper userMapper;

    @TccTransaction
    public User rpcMethod(String name) {
        final String groupId = DTXLocalContext.getOrNew().getGroupId();
        log.info("{} tcc: {}", name, groupId);

        final User user = new User().setName(name);
        userMapper.insert(user);
        IDS.put(groupId, user.getId());

        return user;
    }

    public void confirmRpcMethod(String name) {
        final String groupId = DTXLocalContext.getOrNew().getGroupId();
        log.info("{} tcc-confirm: {}", name, groupId);
        IDS.remove(groupId);
    }

    public void cancelRpcMethod(String name) {
        final String groupId = DTXLocalContext.getOrNew().getGroupId();
        log.info("{} tcc-cancel: {}", name, groupId);

        userMapper.deleteById(IDS.get(groupId));
    }

}
