package com.ezone.devops.pipeline.clients.util;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Base64;

/**
 * 大数据工具类
 */
public class EncryptUtil {
    /**
     * @Description 获取sign值
     * @Param [strSrc]：参数值是由分配的密钥字符串+当前时间戳字符串（时间戳字格式：yyyyMMddHHmmss)
     */
    public static String sign(String strSrc) {
        String encryptStr = encrypt(strSrc);
        String sign = "";
        // 进行Base64编码
        try {
            byte[] encryptBytes = encryptStr.getBytes(Charsets.UTF_8);
            sign = Base64.getEncoder().encodeToString(encryptBytes);
        } catch (Exception e) {
            System.out.println("sign failure , strSrc=" + strSrc);
        }
        return sign;
    }

    /**
     * Md5加密
     */
    public static String encrypt(String strSrc) {
        String md5Des = DigestUtils.md5Hex(strSrc);
        return md5Des;
    }
}
