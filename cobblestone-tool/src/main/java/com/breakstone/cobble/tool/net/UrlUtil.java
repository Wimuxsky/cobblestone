package com.breakstone.cobble.tool.net;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Author: siqigang
 * @Date: 2020/8/6 下午2:52
 **/
public class UrlUtil {

    private static BASE64Encoder encoder = new BASE64Encoder();

    public static String getUrlBase64AndEncode(String url) throws UnsupportedEncodingException {
        // TODO 参数校验
        String base64String = encoder.encode(url.getBytes());
        return URLEncoder.encode(base64String, "utf-8");
    }
}
