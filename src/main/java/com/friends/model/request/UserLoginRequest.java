package com.friends.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data  //生成set get方法
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -1863014272449100255L;
    private String userAccount;
    private String userPassword;
 }
