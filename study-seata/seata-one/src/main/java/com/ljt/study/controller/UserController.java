package com.ljt.study.controller;

import com.ljt.study.mapper.UserMapper;
import com.ljt.study.model.User;
import com.ljt.study.service.TccService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

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
    private RestTemplate restTemplate;
    @Autowired
    private TccService tccService;

    @PostMapping("/at")
    @GlobalTransactional
    public User at(String name1, String name2, String name3) {
        User user = new User().setName(StringUtils.defaultIfBlank(name1, appName));
        userMapper.insert(user);

        rpcTwo(ModeEnum.AT, name2);
        rpcThree(ModeEnum.AT, name3);

        Assert.isTrue(!"at1".equalsIgnoreCase(name1), "分布式事务Seata-AT测试：" + appName);

        return user;
    }

    @SneakyThrows
    @PutMapping("/at/{id}")
    @GlobalTransactional
    public User at(@PathVariable Integer id, String name1, String name2) {
        User user = new User().setId(id).setName(StringUtils.defaultIfBlank(name1, appName));
        userMapper.updateById(user);
        // 如果这个过程中 数据被其他事务修改 则回滚失败
        TimeUnit.SECONDS.sleep(30);
        rpcTwo(ModeEnum.AT, name2);
        return user;
    }

    @PostMapping("/at2")
    @GlobalTransactional
    public User at2(String name1, String name2) {
        User user = new User().setName(StringUtils.defaultIfBlank(name1, appName));
        userMapper.insert(user);

        rpcTwo(ModeEnum.AT2, name2);

        return user;
    }

    private void rpcTwo(ModeEnum modeEnum, String name) {
        restTemplate.postForEntity("http://seata-two/user/" + modeEnum.getValue() + "?name=" + name, null, User.class);
    }

    private void rpcThree(ModeEnum modeEnum, String name) {
        restTemplate.postForEntity("http://seata-three/user/" + modeEnum.getValue() + "?name=" + name, null, User.class);
    }

    @PostMapping("/tcc")
    @GlobalTransactional
    public String tcc(String name1, String name2, String name3) {
        String result = tccService.rpcMethod(name1);

        rpcTwo(ModeEnum.TCC, name2);
        rpcThree(ModeEnum.TCC, name3);

        Assert.isTrue(!"tcc1".equalsIgnoreCase(name1), "分布式事务Seata-TCC测试：" + appName);

        return result;
    }

    @Getter
    @AllArgsConstructor
    private enum ModeEnum {

        /**
         * 模式
         */
        AT("at"), AT2("at2"), TCC("tcc");

        private final String value;

    }

}
