package com.ljt.study.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author LiJingTang
 * @date 2021-06-11 15:16
 */
@Accessors(chain = true)
@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;

}
