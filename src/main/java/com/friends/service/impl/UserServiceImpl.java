package com.friends.service.impl;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.friends.common.ErrorCode;
import com.friends.exception.BusinessException;
import com.friends.mapper.UserMapper;
import com.friends.model.domain.User;
import com.friends.service.UserService;
import com.friends.utils.AlgorithmUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.apache.commons.math3.util.Pair;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.friends.constant.UserConstant.ADMIN_ROLE;
import static com.friends.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    //盐值，MD5混淆密码
    private static final String salt = "md5";
    //用户登录态键

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 4 || checkPassword.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }

        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编号重复");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(Integer.parseInt(planetCode));
         boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 4) {
            return null;
        }
        //账号不能包含特殊字符
        String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";
        if (!Pattern.matches(REGEX_PASSWORD, userPassword)) {
            return null;
        }

        //加密
        String newPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", newPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("User login failed,userAccount cannot match userPassword");
            return null;
        }

        //用户脱敏 ,避免前端直接查到用户隐私信息
        User safeUser=getSafeUser(user);
        //记录用户登陆态
        request.getSession().setAttribute(USER_LOGIN_STATE,safeUser);
        return safeUser;
    }
    @Override
     public   User getSafeUser(User user){
        User safeUser=new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setTags(user.getTags());
        safeUser.setPlanetCode(user.getPlanetCode());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(0);
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setUpdateTime(LocalDateTime.now());
        safeUser.setRole(user.getRole());
        return safeUser;
    }
    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
    /**
     * 根据标签搜索用户（内存过滤）
     *
     * @param tagNameList 用户要拥有的标签
     * @return
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1. 先查询所有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        // 2. 在内存中判断是否包含要求的标签，只要满足一个tag就返回
        return userList.stream().filter(user -> {
            String tagsStr = user.getTags();
            //反序列化Json为set集合
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {}.getType());
           //通过调用Optional的ofNullable方法，将可能为null的tempTagNameSet对象包装成Optional对象。如果tempTagNameSet为null，则使用orElse方法提供的默认值new HashSet<>()，也就是创建一个空的HashSet，确保tempTagNameSet不为null。
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            return tagNameList.stream().anyMatch(tempTagNameSet::contains);
        }).map(this::getSafeUser).collect(Collectors.toList());
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getRole() == ADMIN_ROLE;
    }
    /**
     * 是否为管理员
     *
     * @param loginUser
     * @return
     */
    public boolean isAdmin(User loginUser) {
        return loginUser != null && loginUser.getRole() == ADMIN_ROLE;
    }


    @Override
    public int updateUser(User user, User loginUser) {
        long userId = user.getId();
        if (userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //如果是管理员，允许更新任意用户
        //如果不是管理员，只允许更新自己的信息
        if (!isAdmin(loginUser) && userId != loginUser.getId()){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User userole = userMapper.selectById(userId);
        if (userole == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null){
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return (User) userObj;
    }
    /**
     * 推荐匹配用户
     * @param num
     * @param loginUser
     * @return
     */
    @Override
    public List<User> matchUsers(long num, User loginUser) {
//        这里我因为电脑内存问题，没有办法像鱼皮电脑那样可以存放100万数据，可以直接运行。所以我选择了运行5万条数据。
//        不然的话会报 OOM（内存）的问题
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.last("limit 50000");
//        List<User> userList = this.list(queryWrapper);

//         或者用page分页查询，自己输入或默认数值，但这样匹配就有限制了
//        List<User> userList = this.page(new Page<>(pageNum,pageSize),queryWrapper);
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.isNotNull("tags");
        queryWrapper.select("id","tags");
        List<User> userList=this.list(queryWrapper);
        String tags = loginUser.getTags();

        System.out.println(tags);
        if(tags==null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {}.getType());
        System.out.println(tagList);

        if(tagList==null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 用户列表的下表 => 相似度
        List<Pair<User,Long>> list=new ArrayList<>();
        //依次计算当前用户和所有用户的匹配度
        for(int i=0;i<userList.size();i++){
            User user=userList.get(i);
            String userTags=user.getTags();
            //去除无标签的和自己
            if(StringUtils.isBlank(userTags)||user.getId()==loginUser.getId()){
                continue;
            }
            //tags转字符串
            List<String> userTagList=gson.fromJson(userTags,new TypeToken<List<String>>(){}.getType());
            //计算分数
            long distance=AlgorithmUtils.minDistance(tagList,userTagList);
            list.add(new Pair<>(user,distance));
        }
        //按匹配距离升序,使用Comparator比较逻辑进行
        List<Pair<User,Long>> topUserPairList=list.stream()
                .sorted((a,b)->(int) (a.getValue()-b.getValue()))
                .limit(num)
                .collect(Collectors.toList());
        //
        //有顺序的userID列表
        List<Long> userListVo = topUserPairList.stream().map(pair -> pair.getKey().getId()).collect(Collectors.toList());
        //根据ID查询User
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.in("id",userListVo);
        Map<Long,List<User>> userIdUserListMap= this.list(userQueryWrapper).stream().map(user->getSafeUser(user)).collect(Collectors.groupingBy(User::getId));
        // 因为上面查询打乱了顺序，这里根据上面有序的userID列表赋值
        List<User> finalUserList = new ArrayList<>();
        for (Long userId : userListVo){
            finalUserList.add(userIdUserListMap.get(userId).get(0));
        }
        return finalUserList;
    }

}




