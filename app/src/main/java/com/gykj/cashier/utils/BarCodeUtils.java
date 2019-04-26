package com.gykj.cashier.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 下午3:05
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class BarCodeUtils {

    /**
     *  检查条形码是否匹配
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isBarCodeLegal(String str) throws PatternSyntaxException {
        String regExp = "^\\d{8,13}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     *  检查条形码是否匹配
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isBarCode(String str) throws PatternSyntaxException {
        String regExp = "^\\d{1,13}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
