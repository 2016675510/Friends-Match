package com.friends.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.friends.common.BaseResponse;
import com.friends.common.ErrorCode;
import com.friends.common.ResultUtils;
import com.friends.exception.BusinessException;
import com.friends.model.domain.User;
import com.friends.model.request.UserLoginRequest;
import com.friends.model.request.UserRegisterRequest;
import com.friends.service.TeamService;
import com.friends.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.friends.constant.UserConstant.ADMIN_ROLE;
import static com.friends.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://127.0.0.1:5173/"},allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String checkCode = userRegisterRequest.getCheckPassword();
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String planetCode = userRegisterRequest.getPlanetCode();

        if (StringUtils.isAnyBlank(checkCode, userAccount, userPassword,planetCode)) {
            return null;
        }
         long result = userService.userRegister(userAccount, userPassword, checkCode,planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }
    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }
    /**
     * 获取当前用户
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafeUser(user);
        return ResultUtils.success(safetyUser);
    }

    //查询
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        //仅管理员可查询
        if(!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList= userService.list(queryWrapper);
        List<User> list = userList.stream().map(users -> userService.getSafeUser(users)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }
    @PostMapping("/delete")
    public boolean deleteUsers(@RequestBody  User userToId,HttpServletRequest request){
        Long id=userToId.getId();
        //仅管理员可删除
        if(!userService.isAdmin(request)){
            throw  new BusinessException(ErrorCode.NO_AUTH);
        }
        if(id<= 0){
            return false;
        }
        return userService.removeById(id);   //逻辑删除可自动更新
    }
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList)){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList=userService.searchUsersByTags(tagNameList);
        return ResultUtils.success(userList);
    }
    //所有用户查询
  /*  @GetMapping("/recommend")
    public BaseResponse<List<User>> recommendUsers(HttpServletRequest request) {

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        List<User> userList= userService.list(queryWrapper);
        List<User> list = userList.stream().map(users -> userService.getSafeUser(users)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }*/
    //分页查询，用户数量较高时，得分页查询
    /*@GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize,long pageNum,HttpServletRequest request) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        Page<User> userList= userService.page(new Page<>(pageNum,pageSize));
        return ResultUtils.success(userList);
    }*/
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
        User logininUser = userService.getLoginUser(request);
        String redisKey = String.format("shayu:user:recommend:%s",logininUser.getId());
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //如果有缓存，直接读取
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        if (userPage != null){
            return ResultUtils.success(userPage);
        }
        //无缓存，查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum,pageSize),queryWrapper);
        //写缓存,10s过期
        try {
            valueOperations.set(redisKey,userPage,30000, TimeUnit.MILLISECONDS);
        } catch (Exception e){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userPage);
    }


    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request){
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        User loginUser=userService.getLoginUser(request);
        int result=userService.updateUser(user,loginUser);
        return ResultUtils.success(result);
    }
    @GetMapping("/match")
    public BaseResponse<List<User>> matchUser( long  num, HttpServletRequest request){
        if (num<=0||num>20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser=userService.getLoginUser(request);
        //重新查询一次，避免用户信息被修改，而session没有
        loginUser=userService.getById(loginUser.getId());

        return ResultUtils.success(userService.matchUsers(num,loginUser));
    }



}
