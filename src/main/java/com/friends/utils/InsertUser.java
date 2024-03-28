package com.friends.utils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import com.friends.mapper.UserMapper;
import com.friends.model.domain.User;
import com.friends.service.UserService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//@Component
public class InsertUser {
    @Resource
    private UserMapper userMapper;

    /**
     * 定时循环插入用户  耗时：9720ms
     * 5秒，频率为 fixedRate = Long.MAX_VALUE
     */
//    @Scheduled(fixedDelay = 5000)
    public  void doInsertUser(){
        StopWatch  stopWatch=new StopWatch();
        stopWatch.start();
        final int INSERT_NUM=1000;
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
            userMapper.insert(user);
        }
         stopWatch.stop();
     }

    /**
     * 批量插入用户  耗时：4751ms
     */
    @Resource
    UserService userService;
    public void doInsertUserInBatch() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            user_t(userList);
        }
        userService.saveBatch(userList,100);
        stopWatch.stop();
//        System.out.println( stopWatch.getLastTaskTimeMillis());
    }

    private void user_t(List<User> userList) {
        User user = new User();
        user.setUsername("假沙鱼");
        user.setUserAccount("yusha");
        user.setAvatarUrl("shanghai.myqcloud.com/shayu931/shayu.png");
        user.setProfile("一条咸鱼");
        user.setGender(0L);
        user.setUserPassword("12345678");
        user.setPhone("123456789108");
        user.setEmail("shayu-yusha@qq.com");
        user.setUserStatus(0);
        user.setRole(0);
        user.setPlanetCode(123);
        user.setTags("[]");
        userList.add(user);
    }

    //线程设置
    private ExecutorService executorService = new ThreadPoolExecutor(16, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));

    /**
     * 并发批量插入用户   100000  耗时： 26830ms
     */
    public void doConcurrencyInsertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        // 分十组
        int j = 0;
        //批量插入数据的大小
        int batchSize = 5000;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        // i 要根据数据量和插入批量来计算需要循环的次数。（鱼皮这里直接取了个值，会有问题,我这里随便写的）
        for (int i = 0; i < INSERT_NUM/batchSize; i++) {
            List<User> userList = new ArrayList<>();
            while (true){
                j++;
                user_t(userList);
                if (j % batchSize == 0 ){
                    break;
                }
            }
            //异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->{
                System.out.println("ThreadName：" + Thread.currentThread().getName());
                userService.saveBatch(userList,batchSize);
            },executorService);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();

        stopWatch.stop();
//        System.out.println( stopWatch.getLastTaskTimeMillis());
    }




}
