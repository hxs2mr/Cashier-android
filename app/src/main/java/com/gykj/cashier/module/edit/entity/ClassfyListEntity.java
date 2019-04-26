package com.gykj.cashier.module.edit.entity;

import java.util.List;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/2616:36
 * version: 1.0
 */
public class ClassfyListEntity {


    /**
     * next : [{"next":[],"name":"休闲食品","id":2},{"next":[],"name":"进口食品","id":4}]
     * name : 食品饮料
     * id : 1
     */

    private String name;
    private int id;
    private List<NextBean> next;

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

    public List<NextBean> getNext() {
        return next;
    }

    public void setNext(List<NextBean> next) {
        this.next = next;
    }

    public class NextBean {
        /**
         * next : []
         * name : 休闲食品
         * id : 2
         */

        private String name;
        private int id;
        private List<ClassifyEntity> next;

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

        public List<ClassifyEntity> getNext() {
            return next;
        }

        public void setNext(List<ClassifyEntity> next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
