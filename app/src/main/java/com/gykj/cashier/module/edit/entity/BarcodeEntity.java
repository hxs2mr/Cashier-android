package com.gykj.cashier.module.edit.entity;

import java.util.List;

/**
 * desc   : 商品条形码返回实体类
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/12/1014:47
 * version: 1.0
 */
public class BarcodeEntity {

    /**
     * result : {"note":"办公学生用抄写本14x20cm","manuName":"河南弘都文化用品有限公司","img":"","code":"6928772300378","flag":true,"remark":"查询成功！","manuAddress":"","sptmImg":"http://app2.showapi.com/img/barCode_img/20161012/1476243541752.png","spec":"32开23页","goodsType":"食品、饮料和烟草>>肉和家禽产品>>加工和处理过的肉>>冷冻加工和处理过的肉","price":"","trademark":"弘都","ycg":"中国","goodsName":"32开40型卡面抄写本","ret_code":"0","imgList":[]}
     * flag : 0
     */

    private ResultBean result;
    private int flag;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public class ResultBean {
        /**
         * note : 办公学生用抄写本14x20cm
         * manuName : 河南弘都文化用品有限公司
         * img :
         * code : 6928772300378
         * flag : true
         * remark : 查询成功！
         * manuAddress :
         * sptmImg : http://app2.showapi.com/img/barCode_img/20161012/1476243541752.png
         * spec : 32开23页
         * goodsType : 食品、饮料和烟草>>肉和家禽产品>>加工和处理过的肉>>冷冻加工和处理过的肉
         * price :
         * trademark : 弘都
         * ycg : 中国
         * goodsName : 32开40型卡面抄写本
         * ret_code : 0
         * imgList : []
         */

        private String note;
        private String manuName;
        private Object img;
        private String code;
        private boolean flag;
        private String remark;
        private String manuAddress;
        private String sptmImg;
        private String spec;
        private String goodsType;
        private String price;
        private String trademark;
        private String ycg;
        private String goodsName;
        private String ret_code;
        private List<?> imgList;

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getManuName() {
            return manuName;
        }

        public void setManuName(String manuName) {
            this.manuName = manuName;
        }

        public Object getImg() {
            return img;
        }

        public void setImg(Object img) {
            this.img = img;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getManuAddress() {
            return manuAddress;
        }

        public void setManuAddress(String manuAddress) {
            this.manuAddress = manuAddress;
        }

        public String getSptmImg() {
            return sptmImg;
        }

        public void setSptmImg(String sptmImg) {
            this.sptmImg = sptmImg;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public String getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(String goodsType) {
            this.goodsType = goodsType;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTrademark() {
            return trademark;
        }

        public void setTrademark(String trademark) {
            this.trademark = trademark;
        }

        public String getYcg() {
            return ycg;
        }

        public void setYcg(String ycg) {
            this.ycg = ycg;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getRet_code() {
            return ret_code;
        }

        public void setRet_code(String ret_code) {
            this.ret_code = ret_code;
        }

        public List<?> getImgList() {
            return imgList;
        }

        public void setImgList(List<?> imgList) {
            this.imgList = imgList;
        }
    }
}
