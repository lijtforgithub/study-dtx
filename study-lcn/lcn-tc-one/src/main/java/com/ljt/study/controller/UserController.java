package com.ljt.study.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.ljt.study.mapper.UserMapper;
import com.ljt.study.tx.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
    private UserMapper userMapper;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/lcn")
    @LcnTransaction
    public User lcn(String name1, String name2, String name3) {
        User user = new User().setName(StringUtils.defaultIfBlank(name1, appName));
        userMapper.insert(user);

        rpcTwo(ModeEnum.LCN, name2);
        rpcThree(ModeEnum.LCN, name3);

        Assert.isTrue(!"lcn1".equalsIgnoreCase(name1), "分布式事务LCN测试：" + appName);

        return user;
    }

    @PostMapping("/lcn2")
    @LcnTransaction
    public User lcn2(String name1, String name2) {
        User user = new User().setName(StringUtils.defaultIfBlank(name1, appName));
        userMapper.insert(user);

        rpcTwo(ModeEnum.LCN2, name2);

        return user;
    }

    private void rpcTwo(ModeEnum modeEnum, String name) {
        restTemplate.postForEntity("http://lcn-tc-two/user/" + modeEnum.getValue() + "?name=" + name, null, User.class);
    }

    private void rpcThree(ModeEnum modeEnum, String name) {
        restTemplate.postForEntity("http://lcn-tc-three/user/" + modeEnum.getValue() + "?name=" + name, null, User.class);
    }


    @TccTransaction
    @Transactional
    @PostMapping("/tcc")
    public User tcc(String name1, String name2, String name3) {
        User user = saveUser(name1);

        rpcTwo(ModeEnum.TCC, name2);
        rpcThree(ModeEnum.TCC, name3);

        Assert.isTrue(!"tcc1".equalsIgnoreCase(name1), "分布式事务LCN-TCC测试：" + appName);

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


    @Getter
    @AllArgsConstructor
    private enum ModeEnum {

        /**
         * 模式
         */
        LCN("lcn"), LCN2("lcn2"), TCC("tcc");

        private final String value;

    }

}
