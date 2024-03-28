package com.friends.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 6568765137744534245L;
    /**
     * 页面大小
     */
    protected int pageSize=10;
    /**
     * 当前页
     */

    protected  int pageNum=1;
}
