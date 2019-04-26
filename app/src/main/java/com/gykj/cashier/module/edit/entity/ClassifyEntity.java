package com.gykj.cashier.module.edit.entity;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 22/8/18 上午9:16
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class ClassifyEntity implements Serializable{


    private static final long serialVersionUID = -6382128213532624758L;
    /**
     * name : da
     * id : 2
     */

    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
