package com.ljt.study.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.ljt.study.mapper.UserMapper;
import com.ljt.study.tx.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiJingTang
 * @date 2021-06-11 15:19
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserMapper userMapper;

    @LcnTransaction
    @PostMapping("/lcn")
    public User lcn(String name) {
        User user = new User().setName(StringUtils.defaultIfBlank(name, appName));
        userMapper.insert(user);

        Assert.isTrue(!"lcn2".equalsIgnoreCase(name), "分布式事务LCN测试：" + appName);

        return user;
    }

    @LcnTransaction
    @PostMapping("/lcn2")
    public User lcn2(String name) {
        restTemplate.postForEntity("http://lcn-tc-three/user/lcn", null, User.class);
        return lcn(name);
    }

    @TccTransaction
    @Transactional
    @PostMapping("/tcc")
    public User tcc(String name) {
        User user = saveUser(name);

        Assert.isTrue(!"tcc2".equalsIgnoreCase(name), "分布式事务LCN-TCC测试：" + appName);

        return user;
    }

    private static final ConcurrentHashMap<String, Integer> IDS = new ConcurrentHashMap<>();

    private User saveUser(String name) {
        final String groupId = DTXLocalContext.getOrNew().getGroupId();
        log.info("{} tcc: {}", name, groupId);

        final User user = new User().setName(name);
        userMapper.insert(user);
        IDS.put(groupId, user.getId());

        return user;
    }

    public void confirmTcc(String name) {
        final String groupId = DTXLocalContext.getOrNew().getGroupId();
        log.info("{} tcc-confirm: {}", name, groupId);
        IDS.remove(groupId);
    }

    public void cancelTcc(String name) {
        final String groupId = DTXLocalContext.getOrNew().getGroupId();
        log.info("{} tcc-cancel: {}", name, groupId);

        userMapper.deleteById(IDS.get(groupId));
    }

}
