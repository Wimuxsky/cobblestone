package com.breakstone.cobble.tool.image;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Author siqigang
 * @Date 2018/10/18 15:52
 */
public class ImageMagickUtil {

    public static void main(String[] args) throws InterruptedException, IOException, IM4JavaException {
//        rotate();
//        Map<String,Object> ma = new HashMap<>();
//        ma.put("1","123");
//        ma.put("2","123");
//
//        ma.remove("abc");
//        System.out.println(ma.toString());

        String atring = "/adunion/3759d416d07ce905f969b2eb6f0e53e41402.png@2w_200h_2e";

        System.out.println(getUrlBase64AndEncode(atring));
    }

    private static BASE64Encoder encoder = new BASE64Encoder();

    private static String getUrlBase64AndEncode(String url) throws UnsupportedEncodingException {
        String base64String = encoder.encode(url.getBytes());
        return URLEncoder.encode(base64String, "utf-8");
    }

    private static void rotate() throws InterruptedException, IOException, IM4JavaException {
        IMOperation operation = new IMOperation();
        operation.addImage("/Users/Wimux/Downloads/image_201810181426381120/0_b745305d70964ef20fe304bc82058c51139332.jpg");
        operation.rotate(90d);
        operation.addImage("/Users/Wimux/Downloads/image_201810181426381120/0_b745305d70964ef20fe304bc82058c51139332b.jpg");

        ConvertCmd cmd = new ConvertCmd(true);
        cmd.run(operation);
    }
}
