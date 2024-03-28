package com.friends.service.impl;

import com.friends.UserCenterApplication;
import com.friends.model.domain.User;
import com.friends.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {UserCenterApplication.class})
class UserServiceImplTest {

//    @Resource
//    UserService userService;
//    @Test
//    void userRegister() {
//        String account ="1234567890";
//        String psd="12345678";
//        String code="12345678";
//       long result=  userService.userRegister(account,psd,code);
//       Assertions.assertEquals(-1,result);
////        account="1234";
////        long result1=  userService.userRegister(account,psd,code);
////        Assertions.assertEquals(-1,result1);
////        account="12344";
////        long result2=  userService.userRegister(account,psd,code);
////        Assertions.assertEquals(-1,result2);
//    }
//    @Test
//    public void testSearchUsersByTags() {
//        List<String> tagNameList = Arrays.asList("java", "python");
//        List<User> userList = userService.searchUsersByTags(tagNameList);
//        System.out.println(userList.toString());
//        Assert.assertNotNull(userList);
//    }
}