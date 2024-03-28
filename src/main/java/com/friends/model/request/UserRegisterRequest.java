package com.friends.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data  //生成set get方法
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -7666353024877063880L;
    private String UserAccount;
    private String UserPassword;
    private String checkPassword;
    private String planetCode;
}
