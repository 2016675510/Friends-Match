package com.friends.utils;

import com.friends.UserCenterApplication;
import com.friends.mapper.UserMapper;
import com.friends.model.domain.User;
import com.friends.service.UserService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = {UserCenterApplication.class})
class InsertUserTest {
   /* @Resource
    private UserService userService;
    @Test
    public  void doInsertUser(){
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        final int INSERT_NUM=1000;
        List<User> userList=new ArrayList<>();
        Long startTime=System.currentTimeMillis();
        for(int i=0;i< INSERT_NUM;i++){
            User user=new User();
            user.setUsername("咸鱼");
            user.setUserAccount("xianyu");
            user.setAvatarUrl("https://img1.baidu.com/it/u=467212011,1034521901&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=500");
            user.setGender(0L);
            user.setProfile("一条咸鱼");
            user.setUserPassword("123454565");
            user.setPlanetCode(345432);
            user.setPhone("1234354545");
            user.setTags("[]");
            user.setEmail("12334434");
            user.setUserStatus(0);
            user.setRole(0);
            userList.add(user);
        }
        Long lastTime=System.currentTimeMillis();
        System.out.println(lastTime-startTime);
        stopWatch.stop();

    }*/
}