package com.friends.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
@Cacheable(value="user",key="'getAll'")
public class User implements Serializable {
    /**Serializable
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Long gender;
    /**
     * 用户简介
     */
    private String profile;

    /**
     * 密码
     */
    private String userPassword;
    /**
     * 认证ID

     */
    private Integer planetCode;
    /**
     * 电话
     */
    private String phone;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态
     */
    private Integer userStatus;

    /**
     * 创建日期
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Long isDelete;

    /**
     * 
     */
    private Integer role;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}