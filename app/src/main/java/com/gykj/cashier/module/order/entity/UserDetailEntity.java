package com.gykj.cashier.module.order.entity;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/11/714:00
 * version: 1.0
 */
public class UserDetailEntity {


    /**
     * gradeName : 冠宇科技
     * sex : 男
     * className : 技术部
     * pic : http://guanyukeji-static.oss-cn-hangzhou.aliyuncs.com/smart_head_pic/smart_head_pic_b33d06c4f64a4566b00aa1a065abcde320180817160954.jpg
     * userName : 孙军
     * schoolName : 贵州冠宇科技有限公司
     */

    private String gradeName;
    private String sex;
    private String className;
    private String pic;
    private String userName;
    private String schoolName;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
