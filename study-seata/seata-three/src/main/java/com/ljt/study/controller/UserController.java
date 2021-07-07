package com.ljt.study.controller;

import com.ljt.study.mapper.UserMapper;
import com.ljt.study.tx.model.User;
import com.ljt.study.service.TccService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiJingTang
 * @date 2021-06-11 15:19
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TccService tccService;

    @PostMapping("/at")
    public User at(String name) {
        User user = new User().setName(StringUtils.defaultIfBlank(name, appName));
        userMapper.insert(user);

        Assert.isTrue(!"at3".equalsIgnoreCase(name), "分布式事务Seata-AT测试：" + appName);

        return user;
    }

    @PostMapping("/tcc")
    public String tcc(String name) {
        String result = tccService.rpcMethod();

        Assert.isTrue(!"tcc3".equalsIgnoreCase(name), "分布式事务Seata-TCC测试：" + appName);

        return result;
    }

}
