package com.friends.model.request;

 import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
 import org.springframework.format.annotation.DateTimeFormat;

 import java.io.Serializable;
 import java.time.LocalDate;
 import java.time.LocalDateTime;
 import java.util.Date;

/**
 * 用户添加队伍请求体
 *
 * @author yupi
 */
@Data
public class TeamAddRequest implements Serializable {


    private static final long serialVersionUID = 1251071277476067522L;
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    @DateTimeFormat(pattern = "yyyy,MM,dd")
    @JsonFormat(pattern = "yyyy,MM,dd")
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;
}