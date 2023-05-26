package com.lty.common.constant;

import java.nio.charset.StandardCharsets;

/**
 * 通用常量
 * @author lty
 */
public interface BaseConstant {
    /**
     * 用于DB中的密码加密解密(KEY要十六位)
     */
    byte[] AES_KEY = "123456789abcdefg".getBytes(StandardCharsets.UTF_8);

    /**
     * linux系统分隔符
     */
    String SEPARATOR_SPRIT = "/";
    /**
     * win系统分隔符
     */
    String SEPARATOR_BACKSLASH = "\\\\";

    /**
     * 获取项目根目录
     */
    String PROJECT_ROOT_DIRECTORY = System.getProperty("user.dir")
            .replaceAll("\\\\", SEPARATOR_SPRIT);
}
