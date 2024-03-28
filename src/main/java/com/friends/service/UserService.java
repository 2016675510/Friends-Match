package com.friends.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.friends.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 用户服务
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @param checkCode    验证码
     * @return 新用户 ID
     */
    long userRegister(String userAccount, String userPassword, String checkCode, String planetCode);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户信息脱敏
     * @param user
     * @return
     */
    User getSafeUser(User user);
    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 用户标签搜索
     * @param tagNameList
     * @return
     */
    List<User> searchUsersByTags(List<String> tagNameList);
    /**
     * 用户信息修改
     */
    int updateUser(User user,User loginUser);

    /**
     * 获取当前用户信息
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);
    boolean isAdmin(HttpServletRequest request);
    boolean isAdmin(User loginUser);

    /**
     * 用户推荐匹配
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);

    }
